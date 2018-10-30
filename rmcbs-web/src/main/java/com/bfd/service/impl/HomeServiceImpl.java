package com.bfd.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
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

import com.bfd.common.vo.Constants;
import com.bfd.dao.mapper.ApiInfoMapper;
import com.bfd.dao.mapper.ColumnMapper;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.dao.mapper.DataPackageMapper;
import com.bfd.dao.mapper.LogStatisticsMapper;
import com.bfd.dao.mapper.MetadataMapper;
import com.bfd.enums.EnabledEnum;
import com.bfd.param.vo.TimeRangeVO;
import com.bfd.param.vo.edgesighvo.home.InformationCountDTO;
import com.bfd.param.vo.edgesighvo.home.InvokeTotalDTO;
import com.bfd.param.vo.edgesighvo.home.TotalDistributeDTO;
import com.bfd.param.vo.edgesighvo.home.TotalDistributeVO;
import com.bfd.service.HomeService;
import com.bfd.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 11:38 2018/9/7
 * @Modified by:
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private MetadataMapper metadataMapper;

    @Autowired
    private ColumnMapper columnMapper;

    @Autowired
    private DataPackageMapper dataPackageMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Autowired
    private LogStatisticsMapper logStatisticsMapper;

    @Value("${es.rms_log_index.indexName}")
    private String indexName;

    @Value("${es.rms_log_index.indexType}")
    private String indexType;

    @Override
    public InformationCountDTO getAllInformation() {

        //获取图书,栏目,数据包,客户单位,接口的总数
        long metadataTotal = metadataMapper.getMetadataTotal();

        long columnTotal = columnMapper.getColumnTotal();

        long packageTotal = dataPackageMapper.getPackageTotal();

        long customerTotal = companyMapper.getCustomerTotal();

        long apiTotal = apiInfoMapper.getApiTotal();
        //创建返回对象添加值
        InformationCountDTO informationCountDTO = new InformationCountDTO();
        informationCountDTO.setBookTotal(metadataTotal);
        informationCountDTO.setPackageTotal(packageTotal);
        informationCountDTO.setColumnTotal(columnTotal);
        informationCountDTO.setBusinessTotal(customerTotal);
        informationCountDTO.setApiTotal(apiTotal);

        return informationCountDTO;
    }

    @Override
    public InvokeTotalDTO getTimeRangeTotal(TimeRangeVO timeRangeVO) {

        InvokeTotalDTO invokeTotalDTO = logStatisticsMapper.getTheOtherDayTotal(timeRangeVO);

        //判断是否查出来
        if (invokeTotalDTO != null){
            invokeTotalDTO.setInvokeFailTotal(invokeTotalDTO.getInvokeTotal() - invokeTotalDTO.getInvokeSuccessTotal());
        }else{
            invokeTotalDTO = new InvokeTotalDTO();
        }
        return invokeTotalDTO;
    }

    @Override
    public TotalDistributeDTO getGraphicalTotal(TimeRangeVO timeRangeVO) {

        TotalDistributeDTO totalDistributeDTO = new TotalDistributeDTO();

        Map<String,Integer> timeMap = Maps.newLinkedHashMap();
        timeMap.put("hour", 1);
        timeMap.put("day",2);
        timeMap.put("week",3);
        timeMap.put("month",4);

        if(timeMap.containsKey(timeRangeVO.getTimeType())){
            switch (timeMap.get(timeRangeVO.getTimeType())){
                case 1:
                    List<TotalDistributeVO> totalDistributeDTOS = getDayEveryHourTotal(timeRangeVO.getStartTime());
                    totalDistributeDTO.setHour(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setDistributeTotal(totalDistributeDTOS);
                    break;
                case 2:
                    Map<String,TotalDistributeVO> dtoMap = getEveryTotal(timeRangeVO.getStartTime(),timeRangeVO.getEndTime(),DateHistogramInterval.DAY);
                    List<String> everyday = DateUtils.collectRangeDates(timeRangeVO.getStartTime(),timeRangeVO.getEndTime());
                    Collections.reverse(everyday);
                    List<TotalDistributeVO> resultDay = getEveryTimeTotal(dtoMap,everyday);
                    totalDistributeDTO.setDay(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setDistributeTotal(resultDay);
                    break;
                case 3:
                    Map<String,TotalDistributeVO> dtoWeekMap = getEveryTotal(timeRangeVO.getStartTime(),timeRangeVO.getEndTime(),DateHistogramInterval.WEEK);
                    List<String> everyWeek = DateUtils.getFirstMondayOfTimes(timeRangeVO.getStartTime(),timeRangeVO.getEndTime());
                    List<TotalDistributeVO> resultWeek = getEveryTimeTotal(dtoWeekMap,everyWeek);
                    Collections.reverse(resultWeek);
                    totalDistributeDTO.setDay(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setWeek(EnabledEnum.OPEN.getKey());
                    totalDistributeDTO.setDistributeTotal(resultWeek);
                    break;
                case 4:
                    Map<String,TotalDistributeVO> dtoMonthMap = getEveryTotal(timeRangeVO.getStartTime(),timeRangeVO.getEndTime(),DateHistogramInterval.MONTH);
                    List<String> everyMonth = DateUtils.getFirstDayOfMonths(timeRangeVO.getStartTime(),timeRangeVO.getEndTime());
                    List<TotalDistributeVO> resultMonth = getEveryTimeTotal(dtoMonthMap,everyMonth);
                    Collections.reverse(resultMonth);
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
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param sign 按什么时间段聚合 day Week 等等
     */
    private Map<String,TotalDistributeVO> getEveryTotal(String startTime, String endTime, DateHistogramInterval sign){

        Map<String,TotalDistributeVO> dtoMap = Maps.newHashMap();
        //条件语句
        RangeQueryBuilder rangeBuilder = QueryBuilders
                .rangeQuery(Constants.TIME_STAMP)
                .from(startTime)
                .to(endTime);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(rangeBuilder);

        //子聚合
        TermsBuilder statusAggs =  AggregationBuilders
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
        InternalHistogram stringTerms = (InternalHistogram)aggregations.asMap().get(Constants.TIME_STAMP);
        List<InternalHistogram.Bucket> buckets = stringTerms.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            TotalDistributeVO totalDistributeDTO = new TotalDistributeVO();
            InternalHistogram.Bucket bucket = buckets.get(i);
            String time = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            totalDistributeDTO.setTime(time);
            totalDistributeDTO.setTotal(docCount);
            //获取访问成功失败统计
            LongTerms statusTerms = (LongTerms)bucket.getAggregations().get(Constants.ES_STATUS);
            List<Terms.Bucket> statusBuckets = statusTerms.getBuckets();
            for(Terms.Bucket statusBucket : statusBuckets){
                long status = statusBucket.getKeyAsNumber().intValue();
                long statusDocCount = statusBucket.getDocCount();
                //判断是否成功
                if(status == Constants.ENABLE_STATUS){
                    totalDistributeDTO.setSuccessTotal(statusDocCount);
                    //失败
                }else {
                    totalDistributeDTO.setFailTotal(statusDocCount);
                }
            }
            dtoMap.put(time,totalDistributeDTO);
        }

        return dtoMap;
    }


    /**
     * 获取一天各个时间段数据统计
     * @param time 时间
     */
    private List<TotalDistributeVO> getDayEveryHourTotal(String time){
        //返回结果集合
        List<TotalDistributeVO> totalDistributeDTOS = Lists.newArrayList();
        //条件语句
        RangeQueryBuilder rangeBuilder = QueryBuilders
                .rangeQuery(Constants.ADD_TIME)
                .from(time+Constants.TIME_START)
                .to(time+Constants.TIME_END);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(rangeBuilder);

        //子聚合
        TermsBuilder statusAggs =  AggregationBuilders
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
        HashMap<String,TotalDistributeVO> hashMap = Maps.newHashMap();
        StringTerms longTerms = (StringTerms)aggregations.asMap().get(Constants.ADD_HOUR);
        List<Terms.Bucket> buckets = longTerms.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            TotalDistributeVO totalDistributeDTO = new TotalDistributeVO();
            Terms.Bucket bucket = buckets.get(i);
            String hour = bucket.getKeyAsNumber().toString();
            long docCount = bucket.getDocCount();
            totalDistributeDTO.setTime(hour);
            totalDistributeDTO.setTotal(docCount);
            //获取访问成功失败统计
            LongTerms statusTerms = (LongTerms)bucket.getAggregations().get(Constants.ES_STATUS);
            List<Terms.Bucket> statusBuckets = statusTerms.getBuckets();
            for(Terms.Bucket statusBucket : statusBuckets){
                long status = statusBucket.getKeyAsNumber().intValue();
                long statusDocCount = statusBucket.getDocCount();
                //判断是否成功
                if(status == Constants.ENABLE_STATUS){
                    totalDistributeDTO.setSuccessTotal(statusDocCount);
                    //失败
                }else {
                    totalDistributeDTO.setFailTotal(statusDocCount);
                }
            }
            hashMap.put(hour, totalDistributeDTO);
        }
        //获取数字与时间范围的映射
        Map<String,String> timeMap = DateUtils.getTimeMap();
        int index = 1;
        for(String key:timeMap.keySet())
        {
            if(hashMap.containsKey(key)){
                TotalDistributeVO totalDistributeDTO = hashMap.get(key);
                totalDistributeDTO.setTime(timeMap.get(key));
                totalDistributeDTO.setNum(index);
                index++;
                totalDistributeDTOS.add(hashMap.get(key));
            }else {
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
     * @param dtoMap ES的查询结果Map
     * @param everyday 时间段每天日期(每天,每周第一天,每月第一天等)
     * @return
     */
    private List<TotalDistributeVO> getEveryTimeTotal(Map<String,TotalDistributeVO> dtoMap, List<String> everyday){

        //创建返回对象
        List<TotalDistributeVO> result = Lists.newArrayList();
        int index = 1;
        for (String time : everyday){
            //判断查询的集合结果是否存在,存在添加,不存在New个
            if(dtoMap.containsKey(time)){
                TotalDistributeVO totalDistributeDTO = dtoMap.get(time);
                totalDistributeDTO.setNum(index);
                index++;
                result.add(totalDistributeDTO);
            }else {
                TotalDistributeVO totalDistributeDTO = new TotalDistributeVO();
                totalDistributeDTO.setTime(time);
                totalDistributeDTO.setNum(index);
                index++;
                result.add(totalDistributeDTO);
            }
        }

        return result;
    }

}
