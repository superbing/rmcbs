package com.bfd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.BusinessUnitsMapper;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.dao.mapper.LogStatisticsMapper;
import com.bfd.dao.mapper.StatisticsUserMapper;
import com.bfd.enums.EnabledEnum;
import com.bfd.param.vo.BusinessUnitsVO;
import com.bfd.param.vo.CompanyVO;
import com.bfd.param.vo.IpRangeVO;
import com.bfd.param.vo.TimeRangeVO;
import com.bfd.param.vo.edgesighvo.CompanyTreeDTO;
import com.bfd.param.vo.edgesighvo.CompanyTreeVO;
import com.bfd.param.vo.edgesighvo.home.InformationCountDTO;
import com.bfd.param.vo.edgesighvo.home.InvokeTotalDTO;
import com.bfd.param.vo.edgesighvo.home.TotalDistributeDTO;
import com.bfd.param.vo.edgesighvo.home.TotalDistributeVO;
import com.bfd.param.vo.statisticsuser.CustomerQueryVO;
import com.bfd.param.vo.statisticsuser.DeveloperQueryVO;
import com.bfd.param.vo.statisticsuser.UserQueryQO;
import com.bfd.service.StatisticsUserService;
import com.bfd.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * 统计查询--用户使用查询serviceImpl
 *
 * @author jiang.liu
 * @date 2018-09-14
 */
@Service
public class StatisticsUserServiceImpl implements StatisticsUserService {

    @Autowired
    StatisticsUserMapper statisticsUserMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private LogStatisticsMapper logStatisticsMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Value("${es.rms_log_index.indexName}")
    private String indexName;

    @Value("${es.rms_log_index.indexType}")
    private String indexType;

    @Value("${es.rms_device_info.indexName}")
    private String indexDeviceName;

    @Value("${es.rms_device_info.indexType}")
    private String indexDeviceType;


    @Override
    public List<CompanyBean> getCustomersName(long id) {
        return companyMapper.getCustomersName(id);
    }

    @Override
    public PageVO<DeveloperQueryVO> getDeveloperQueryPage(UserQueryQO userQueryQO) {

        PageHelper.startPage(userQueryQO.getCurrent(),userQueryQO.getPageSize());
        //查询平台商相关信息
        List<DeveloperQueryVO> developerQuery = companyMapper.getDeveloperQuery(userQueryQO);
        //平台商相关信息Map
        Map<Long,DeveloperQueryVO> DVOMap = Maps.newHashMap();
        developerQuery.forEach(developerQueryVO -> DVOMap.put(developerQueryVO.getId(),developerQueryVO));
        //判断是否查询出平台商
        if(developerQuery != null && developerQuery.size() > Constants.ZERO){
            //客户单位与平台商对应Map
            Map<Long,Long> DCMap = Maps.newHashMap();
            //创建平台商集合
            List<Long> dList = Lists.newArrayList();
            developerQuery.forEach(developerQueryVO -> dList.add(developerQueryVO.getId()));
            //创建客户单位集合
            List<Long> cList = Lists.newArrayList();
            //获取平台商的树结构
            List<CompanyTreeDTO> companyIdTree = companyMapper.getCompanyIdTree(dList);
            for(CompanyTreeDTO companyTreeDTO : companyIdTree){
                for(CompanyTreeVO companyTreeVO : companyTreeDTO.getCompanyTreeVOS()){
                    cList.add(companyTreeVO.getId());
                    DCMap.put(companyTreeVO.getId(),companyTreeDTO.getId());
                }
            }
            //从ES中查询所有的客户单位
            Map<Long,Long> CESMap = getTerminal(cList);
            for (Long businessId : CESMap.keySet()) {
                //获取平台商ID
                Long companyId = DCMap.get(businessId);
                //获取平台商信息
                DeveloperQueryVO developerQueryVO = DVOMap.get(companyId);
                //将该客户单位的终端数量写入对应的平台商
                if(CESMap.containsKey(businessId)){
                    long num = developerQueryVO.getEntryTerminal() + CESMap.get(businessId);
                    developerQueryVO.setEntryTerminal(num);
                }
            }
        }
        //遍历拼接比例
        for (DeveloperQueryVO developerQueryVO: developerQuery){
            //如果都为0 拼接成 -- / --
            if(Constants.ZERO_STRING.equals(developerQueryVO.getTerminalProportion())&& developerQueryVO.getEntryTerminal() == Constants.ZERO){
                developerQueryVO.setTerminalProportion("-- / --");
            }else {
                developerQueryVO.setTerminalProportion(developerQueryVO.getEntryTerminal() + " / " + developerQueryVO.getTerminalProportion());
            }
        }
        // 通过PageHelper获取PageInfo返回对象
        PageInfo<DeveloperQueryVO> pageInfo = new PageInfo<DeveloperQueryVO>(developerQuery);
        return new PageVO<DeveloperQueryVO>(userQueryQO.getCurrent(), userQueryQO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public PageVO<CustomerQueryVO> getCustomerQueryPage(UserQueryQO userQueryQO) {
        PageHelper.startPage(userQueryQO.getCurrent(),userQueryQO.getPageSize());
        //查询客户单位相关信息
        List<CustomerQueryVO> customerQuery = companyMapper.getCustomerQuery(userQueryQO);
        //制作 id - 对象 Map
        Map<Long,CustomerQueryVO> CVOMap = Maps.newHashMap();
        customerQuery.forEach(customerQueryVO -> CVOMap.put(customerQueryVO.getId(),customerQueryVO));
        //制作客户单位ID集合
        List<Long> businessIds = Lists.newArrayList();
        customerQuery.forEach(customerQueryVO -> businessIds.add(customerQueryVO.getId()));
        //从ES中查询所有的
        Map<Long,Long> CESMap = getTerminal(businessIds);
        for(CustomerQueryVO customerQueryVO : customerQuery){
            //如果都为0 拼接成 -- / --
            if(Constants.ZERO_STRING.equals(customerQueryVO.getTerminalProportion()) && !CESMap.containsKey(customerQueryVO.getId())){
                customerQueryVO.setTerminalProportion("-- / --");
            }else {
                if(CESMap.get(customerQueryVO.getId()) == null){
                    customerQueryVO.setTerminalProportion( "-- / " + customerQueryVO.getTerminalProportion());
                }else {
                    customerQueryVO.setTerminalProportion(CESMap.get(customerQueryVO.getId()) + " / " + customerQueryVO.getTerminalProportion());
                }
            }
        }
        // 通过PageHelper获取PageInfo返回对象
        PageInfo<CustomerQueryVO> pageInfo = new PageInfo<CustomerQueryVO>(customerQuery);
        return new PageVO<CustomerQueryVO>(userQueryQO.getCurrent(), userQueryQO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 获取终端数
     * @param businessIds 客户单位Id集合
     * @returns
     */
    private Map<Long,Long> getTerminal(List<Long> businessIds){

        //创建返回的Map集合
        Map<Long,Long> map = Maps.newHashMap();
        //聚合
        TermsBuilder termAggs =  AggregationBuilders
                .terms(Constants.BUSINESSID)
                .field(Constants.BUSINESSID)
                .size(Constants.ES_ZERO);
        // 创建查询语句
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexDeviceName)
                .withTypes(indexDeviceType)
                .withFields(Constants.BUSINESSID)
                .withQuery(boolQuery()
                        .must(termsQuery(Constants.BUSINESSID,businessIds)))
                .withPageable(PageRequest.of(0, 1))
                .addAggregation(termAggs)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        StringTerms stringTerms = (StringTerms)aggregations.asMap().get(Constants.BUSINESSID);
        List<Terms.Bucket> buckets = stringTerms.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            long id = bucket.getKeyAsNumber().longValue();
            long docCount = bucket.getDocCount();
            map.put(id,docCount);
        }
        return map;
    }

    @Override
    public InvokeTotalDTO getTimeRangeTotal(TimeRangeVO timeRangeVO) {

        InvokeTotalDTO invokeTotalDTO = new InvokeTotalDTO();

        if (timeRangeVO.getWhether()) {
            invokeTotalDTO = logStatisticsMapper.getTheseDaysCompanyTotal(timeRangeVO);
        } else {
            invokeTotalDTO = logStatisticsMapper.getTimeRangeBusinessTotal(timeRangeVO);
        }
        //判断是否查出来
        if (invokeTotalDTO != null) {
            invokeTotalDTO.setInvokeFailTotal(invokeTotalDTO.getInvokeTotal() - invokeTotalDTO.getInvokeSuccessTotal());
        } else {
            invokeTotalDTO = new InvokeTotalDTO();
        }
        return invokeTotalDTO;
    }

    @Override
    public TotalDistributeDTO getGraphicalTotal(TimeRangeVO timeRangeVO) {

        TotalDistributeDTO totalDistributeDTO = new TotalDistributeDTO();

        Map<String, Integer> timeMap = Maps.newLinkedHashMap();
        timeMap.put("hour", 1);
        timeMap.put("day", 2);
        timeMap.put("week", 3);
        timeMap.put("month", 4);

        if (timeMap.containsKey(timeRangeVO.getTimeType())) {
            switch (timeMap.get(timeRangeVO.getTimeType())) {
                case 1:
                    List<TotalDistributeVO> totalDistributeDTOS = getDayEveryHourTotal(timeRangeVO.getStartTime(),
                            timeRangeVO.getCompanyId(), timeRangeVO.getWhether());
                    totalDistributeDTO.setHour(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setDistributeTotal(totalDistributeDTOS);
                    break;
                case 2:
                    Map<String, TotalDistributeVO> dtoMap = getEveryTotal(timeRangeVO.getStartTime(), timeRangeVO
                                    .getEndTime(), timeRangeVO.getCompanyId(), timeRangeVO.getWhether(),
                            DateHistogramInterval.DAY);
                    List<String> everyday = DateUtils.collectRangeDates(timeRangeVO.getStartTime(), timeRangeVO
                            .getEndTime());
                    List<TotalDistributeVO> resultDay = getEveryTimeTotal(dtoMap, everyday);
                    totalDistributeDTO.setDay(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setDistributeTotal(resultDay);
                    break;
                case 3:
                    Map<String, TotalDistributeVO> dtoWeekMap = getEveryTotal(timeRangeVO.getStartTime(), timeRangeVO
                                    .getEndTime(), timeRangeVO.getCompanyId(), timeRangeVO.getWhether(),
                            DateHistogramInterval.WEEK);
                    List<String> everyWeek = DateUtils.getFirstMondayOfTimes(timeRangeVO.getStartTime(), timeRangeVO
                            .getEndTime());
                    List<TotalDistributeVO> resultWeek = getEveryTimeTotal(dtoWeekMap, everyWeek);
                    totalDistributeDTO.setDay(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setWeek(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setDistributeTotal(resultWeek);
                    break;
                case 4:
                    Map<String, TotalDistributeVO> dtoMonthMap = getEveryTotal(timeRangeVO.getStartTime(),
                            timeRangeVO.getEndTime(), timeRangeVO.getCompanyId(), timeRangeVO.getWhether(),
                            DateHistogramInterval.MONTH);
                    List<String> everyMonth = DateUtils.getFirstDayOfMonths(timeRangeVO.getStartTime(), timeRangeVO
                            .getEndTime());
                    List<TotalDistributeVO> resultMonth = getEveryTimeTotal(dtoMonthMap, everyMonth);
                    totalDistributeDTO.setWeek(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setMonth(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setDistributeTotal(resultMonth);
                    break;
                default:
                    break;
            }
        }
        return totalDistributeDTO;
    }

    /**
     * 获取时间段每天数据统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param sign      按什么时间段聚合 day Week 等等
     */
    private Map<String, TotalDistributeVO> getEveryTotal(String startTime, String endTime, Long companyId, boolean
            whether, DateHistogramInterval sign) {

        Map<String, TotalDistributeVO> dtoMap = Maps.newHashMap();
        //条件语句
        RangeQueryBuilder rangeBuilder = QueryBuilders
                .rangeQuery(Constants.TIME_STAMP)
                .from(startTime)
                .to(endTime);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (whether) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.COMPANYID, companyId);
            boolQuery.must(termQueryBuilder);
        } else {
            CompanyBean companyBean = companyMapper.getCompanyById(companyId);
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.COMPANYCODE, companyBean.getCompanyCode());
            boolQuery.must(termQueryBuilder);
        }

        boolQuery.must(rangeBuilder);

        boolQueryBuilder.filter(boolQuery);

        //子聚合
        TermsBuilder statusAggs = AggregationBuilders
                .terms(Constants.ES_STATUS)
                .field(Constants.ES_STATUS)
                .size(Constants.ES_ZERO);

        // 聚合语句
        DateHistogramBuilder termAggs = AggregationBuilders
                .dateHistogram(Constants.TIME_STAMP)
                .field(Constants.TIME_STAMP)
                .subAggregation(statusAggs)
                .format(Constants.DATE_FORMAT)
                .interval(sign);

        // 创建查询语句
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .withFields(Constants.TIME_STAMP)
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(0, 1))
                .addAggregation(termAggs)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        InternalHistogram stringTerms = (InternalHistogram) aggregations.asMap().get(Constants.TIME_STAMP);
        List<InternalHistogram.Bucket> buckets = stringTerms.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            TotalDistributeVO totalDistributeDTO = new TotalDistributeVO();
            InternalHistogram.Bucket bucket = buckets.get(i);
            String time = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            totalDistributeDTO.setTime(time);
            totalDistributeDTO.setTotal(docCount);
            //获取访问成功失败统计
            LongTerms statusTerms = (LongTerms) bucket.getAggregations().get(Constants.ES_STATUS);
            List<Terms.Bucket> statusBuckets = statusTerms.getBuckets();
            for (Terms.Bucket statusBucket : statusBuckets) {
                long status = statusBucket.getKeyAsNumber().intValue();
                long statusDocCount = statusBucket.getDocCount();
                //判断是否成功
                if (status == Constants.ENABLE_STATUS) {
                    totalDistributeDTO.setSuccessTotal(statusDocCount);
                    //失败
                } else {
                    totalDistributeDTO.setFailTotal(statusDocCount);
                }
            }
            dtoMap.put(time, totalDistributeDTO);
        }
        return dtoMap;
    }

    /**
     * 获取一天各个时间段数据统计
     *
     * @param time 时间
     */
    private List<TotalDistributeVO> getDayEveryHourTotal(String time, Long companyId, boolean whether) {
        //返回结果集合
        List<TotalDistributeVO> totalDistributeDTOS = Lists.newArrayList();
        //条件语句
        RangeQueryBuilder rangeBuilder = QueryBuilders
                .rangeQuery(Constants.ADD_TIME)
                .from(time + Constants.TIME_START)
                .to(time + Constants.TIME_END);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (whether) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.COMPANYID, companyId);
            boolQuery.must(termQueryBuilder);
        } else {
            CompanyBean companyBean = companyMapper.getCompanyById(companyId);
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.COMPANYCODE, companyBean.getCompanyCode());
            boolQuery.must(termQueryBuilder);
        }

        boolQuery.must(rangeBuilder);

        boolQueryBuilder.filter(boolQuery);

        //子聚合
        TermsBuilder statusAggs = AggregationBuilders
                .terms(Constants.ES_STATUS)
                .field(Constants.ES_STATUS)
                .size(Constants.ES_ZERO);

        // 聚合语句
        TermsBuilder termAggs = AggregationBuilders
                .terms(Constants.ADD_HOUR)
                .field(Constants.ADD_HOUR)
                .subAggregation(statusAggs)
                .size(Constants.ES_ZERO);

        // 创建查询语句
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .withFields(Constants.ADD_HOUR)
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(0, 1))
                .addAggregation(termAggs)
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        HashMap<String, TotalDistributeVO> hashMap = Maps.newHashMap();
        StringTerms longTerms = (StringTerms) aggregations.asMap().get(Constants.ADD_HOUR);
        List<Terms.Bucket> buckets = longTerms.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            TotalDistributeVO totalDistributeDTO = new TotalDistributeVO();
            Terms.Bucket bucket = buckets.get(i);
            String hour = bucket.getKeyAsNumber().toString();
            long docCount = bucket.getDocCount();
            totalDistributeDTO.setTime(hour);
            totalDistributeDTO.setTotal(docCount);
            //获取访问成功失败统计
            LongTerms statusTerms = (LongTerms) bucket.getAggregations().get(Constants.ES_STATUS);
            List<Terms.Bucket> statusBuckets = statusTerms.getBuckets();
            for (Terms.Bucket statusBucket : statusBuckets) {
                long status = statusBucket.getKeyAsNumber().intValue();
                long statusDocCount = statusBucket.getDocCount();
                //判断是否成功
                if (status == Constants.ENABLE_STATUS) {
                    totalDistributeDTO.setSuccessTotal(statusDocCount);
                    //失败
                } else {
                    totalDistributeDTO.setFailTotal(statusDocCount);
                }
            }
            hashMap.put(hour, totalDistributeDTO);
        }
        //获取数字与时间范围的映射
        Map<String, String> timeMap = DateUtils.getTimeMap();
        int index = 1;
        for (String key : timeMap.keySet()) {
            if (hashMap.containsKey(key)) {
                TotalDistributeVO totalDistributeDTO = hashMap.get(key);
                totalDistributeDTO.setTime(timeMap.get(key));
                totalDistributeDTO.setNum(index);
                index++;
                totalDistributeDTOS.add(hashMap.get(key));
            } else {
                TotalDistributeVO totalDistributeDTO = new TotalDistributeVO();
                totalDistributeDTO.setTime(timeMap.get(key));
                totalDistributeDTO.setNum(index);
                index++;
                totalDistributeDTOS.add(totalDistributeDTO);
            }
        }
        return totalDistributeDTOS;
    }

    /**
     * 传入时间范围,ES的查询结果Map,时间段每天日期
     *
     * @param dtoMap   ES的查询结果Map
     * @param everyday 时间段每天日期(每天,每周第一天,每月第一天等)
     * @return
     */
    private List<TotalDistributeVO> getEveryTimeTotal(Map<String, TotalDistributeVO> dtoMap, List<String> everyday) {

        //创建返回对象
        List<TotalDistributeVO> result = Lists.newArrayList();
        int index = 1;
        for (String time : everyday) {
            //判断查询的集合结果是否存在,存在添加,不存在New个
            if (dtoMap.containsKey(time)) {
                TotalDistributeVO totalDistributeDTO = dtoMap.get(time);
                totalDistributeDTO.setNum(index);
                index++;
                result.add(totalDistributeDTO);
            } else {
                TotalDistributeVO totalDistributeDTO = new TotalDistributeVO();
                totalDistributeDTO.setTime(time);
                totalDistributeDTO.setNum(index);
                index++;
                result.add(totalDistributeDTO);
            }
        }
        return result;
    }

    /**
     * 获取终端数
     * @param businessId 客户单位Id
     * @return
     */
    private int getTerminalAccount(long businessId){

        int result = 0;
        // 创建查询语句
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .withQuery(boolQuery()
                        .must(termQuery(Constants.BUSINESSID,businessId)))
                .build();
        List<JSONObject> jsonObjects = elasticsearchTemplate.queryForList(searchQuery, JSONObject.class);

        if(jsonObjects != null && jsonObjects.size() > Constants.ZERO){
            result = jsonObjects.size();
        }
        return result;
    }
}
