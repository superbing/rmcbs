package com.bfd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bfd.bean.ApiTypeBean;
import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.ApiInfoMapper;
import com.bfd.dao.mapper.ApiTypeMapper;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.dao.mapper.LogStatisticsMapper;
import com.bfd.param.vo.edgesighvo.*;
import com.bfd.param.vo.edgesighvo.es.DayCountLogVO;
import com.bfd.param.vo.serviceaudit.ServiceAuditQO;
import com.bfd.param.vo.serviceaudit.ServiceAuditVO;
import com.bfd.service.EdgeSightService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @Author: chong.chen
 * @Description: 服务监控
 * @Date: Created in 16:58 2018/8/27
 * @Modified by:
 */
@Service
@Slf4j
public class EdgeSightServiceImpl implements EdgeSightService {
    
    @Autowired
    LogStatisticsMapper logStatisticsMapper;
    
    @Autowired
    CompanyMapper companyMapper;
    
    @Autowired
    ApiInfoMapper apiInfoMapper;

    @Autowired
    ApiTypeMapper apiTypeMapper;
    
    @Value("${api.url}")
    private String address;
    
    @Value("${es.rms_log_index.indexName}")
    private String indexName;
    
    @Value("${es.rms_log_index.indexType}")
    private String indexType;
    
    @Value("${es.max.total}")
    private Long maxTotal;
    
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    
    @Override
    public PageVO<ApiSightVO> getApiSight(ApiSightQO apiSightQO) {
        // 添加当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(System.currentTimeMillis());
        apiSightQO.setToday(today);
        // 分页查询出单位
        PageHelper.startPage(apiSightQO.getCurrent(), apiSightQO.getPageSize());
        List<ApiSightVO> companyList = logStatisticsMapper.getApiSight(apiSightQO);
        // 通过PageHelper获取PageInfo返回对象
        PageInfo<ApiSightVO> pageInfo = new PageInfo<ApiSightVO>(companyList);
        return new PageVO<ApiSightVO>(apiSightQO.getCurrent(), apiSightQO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }
    
    @Override
    public CompanySightDTO getCompanySight(CompanySightQO companySightQO) {
        
        // 根据ID获取接口信息
        CompanySightDTO companySightDTO = apiInfoMapper.getApiTypeUrl(companySightQO.getApiId());
        
        // 如果不为空
        if (companySightDTO != null) {
            
            // 将根地址+类型地址+接口地址拼接
            String apiUrl = companySightDTO.getUrl();
            companySightDTO.setUrl(address + companySightDTO.getTypeUrl() + apiUrl);
            
            // 分页查询出单位
            PageHelper.startPage(companySightQO.getCurrent(), companySightQO.getPageSize());
            List<CompanySightVO> companySightVOS = logStatisticsMapper.getCompanySight(companySightQO);
            if (companySightVOS != null) {
                // 通过PageHelper获取PageInfo返回对象
                PageInfo<CompanySightVO> pageInfo = new PageInfo<CompanySightVO>(companySightVOS);
                PageVO<CompanySightVO> pageVO = new PageVO<CompanySightVO>(companySightQO.getCurrent(), companySightQO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
                companySightDTO.setCompanySightVOS(pageVO);
            } else {
                // 为空造个空对象给前端
                companySightVOS = Lists.newArrayList();
                PageVO<CompanySightVO> pageVO = new PageVO<CompanySightVO>(companySightQO.getCurrent(), companySightQO.getPageSize(), Constants.ZERO, companySightVOS);
                companySightDTO.setCompanySightVOS(pageVO);
            }
            
        } else {
            // 给前端造个空对象
            companySightDTO = new CompanySightDTO();
            List<CompanySightVO> list = Lists.newArrayList();
            PageVO<CompanySightVO> pageVO = new PageVO<CompanySightVO>(companySightQO.getCurrent(), companySightQO.getPageSize(), Constants.ZERO, list);
            companySightDTO.setCompanySightVOS(pageVO);
        }
        return companySightDTO;
    }
    
    @Override
    public BusinessSightDTO getBusinessSight(BusinessSightQO businessSightQO) {
        // 根据ID获取接口信息
        CompanySightDTO companySightDTO = apiInfoMapper.getApiTypeUrl(businessSightQO.getApiId());
        // 变成BusinessSightDTO对象
        BusinessSightDTO businessSightDTO = new BusinessSightDTO();
        // 如果不为空
        if (companySightDTO != null) {
            businessSightDTO.setId(companySightDTO.getId());
            businessSightDTO.setName(companySightDTO.getName());
            // 将根地址+类型地址+接口地址拼接
            String apiUrl = companySightDTO.getUrl();
            businessSightDTO.setUrl(address + companySightDTO.getTypeUrl() + apiUrl);
            // 分页查询出单位
            PageHelper.startPage(businessSightQO.getCurrent(), businessSightQO.getPageSize());
            List<BusinessSightVO> businessSightVOS = logStatisticsMapper.getBusinessSight(businessSightQO);
            if (businessSightVOS != null) {
                // 通过PageHelper获取PageInfo返回对象
                PageInfo<BusinessSightVO> pageInfo = new PageInfo<BusinessSightVO>(businessSightVOS);
                PageVO<BusinessSightVO> pageVO = new PageVO<BusinessSightVO>(businessSightQO.getCurrent(), businessSightQO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
                businessSightDTO.setBusinessSightVOS(pageVO);
            } else {
                // 为空造个空对象给前端
                businessSightVOS = Lists.newArrayList();
                PageVO<BusinessSightVO> pageVO = new PageVO<BusinessSightVO>(businessSightQO.getCurrent(), businessSightQO.getPageSize(), Constants.ZERO, businessSightVOS);
                businessSightDTO.setBusinessSightVOS(pageVO);
            }
        } else {
            // 给前端造个空对象
            List<BusinessSightVO> list = Lists.newArrayList();
            PageVO<BusinessSightVO> pageVO = new PageVO<BusinessSightVO>(businessSightQO.getCurrent(), businessSightQO.getPageSize(), Constants.ZERO, list);
            businessSightDTO.setBusinessSightVOS(pageVO);
        }
        
        return businessSightDTO;
    }
    
    @Override
    public DayCountSightDTO getDayApiCount(DayCountSightQO dayCountSightQO) {
        
        // 根据ID获取接口信息
        CompanySightDTO companySightDTO = apiInfoMapper.getApiTypeUrl(dayCountSightQO.getApiId());
        // 变成DayCountSightDTO对象
        DayCountSightDTO dayCountSightDTO = new DayCountSightDTO();
        
        // 如果不为空
        if (dayCountSightDTO != null) {
            
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(System.currentTimeMillis());
            
            dayCountSightDTO.setId(companySightDTO.getId());
            dayCountSightDTO.setName(companySightDTO.getName());
            // 将根地址+类型地址+接口地址拼接
            String apiUrl = companySightDTO.getUrl();
            dayCountSightDTO.setUrl(address + companySightDTO.getTypeUrl() + apiUrl);
            dayCountSightDTO.setAccessDay(today);
            
            // 分页从ES中获取
            Pageable pageable = PageRequest.of(dayCountSightQO.getCurrent() - 1, dayCountSightQO.getPageSize());
            // 排序
            SortBuilder sortBuilder = SortBuilders.fieldSort(Constants.ADD_TIME).order(SortOrder.DESC);
            // 开始拼接条件
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.APIID, dayCountSightQO.getApiId());
            boolQueryBuilder.must(termQueryBuilder);
            TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery(Constants.TIME_STAMP, today);
            boolQueryBuilder.must(termQueryBuilder1);
            // 添加当前时间
            SearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withIndices(indexName)
                    .withTypes(indexType)
                    .withQuery(boolQueryBuilder)
                    .withSort(sortBuilder)
                    .withPageable(pageable)
                    .build();
            
            Page<JSONObject> jsonObjects = elasticsearchTemplate.queryForPage(searchQuery, JSONObject.class);
            
            // 将客户单位的ID存起来
            List<String> companyCodes = Lists.newArrayList();
            // 结果List集合
            List<DayCountLogVO> dayCountLogVOList = Lists.newArrayList();
            for (JSONObject jsonObject : jsonObjects) {
                DayCountLogVO dayCountLogVO = JSONObject.toJavaObject(jsonObject, DayCountLogVO.class);
                companyCodes.add(dayCountLogVO.getCompanyCode());
                dayCountLogVOList.add(dayCountLogVO);
            }
            // 判断是否没查到
            if (dayCountLogVOList != null && dayCountLogVOList.size() > Constants.ZERO) {
                // 查询客户单位相关信息
                List<CompanyBean> companyBeans = companyMapper.getCompanyByCompanyCodes(companyCodes);
                // 制作Map映射
                Map<String, CompanyBean> companyCodeMap = Maps.newHashMap();
                companyBeans.forEach(companyBean -> companyCodeMap.put(companyBean.getCompanyCode(), companyBean));
                // 为返回集合添加数据
                for (DayCountLogVO dayCountLogVO : dayCountLogVOList) {
                    if (companyCodeMap.containsKey(dayCountLogVO.getCompanyCode())) {
                        CompanyBean companyBean = companyCodeMap.get(dayCountLogVO.getCompanyCode());
                        dayCountLogVO.setCompanyName(companyBean.getCompanyName());
                        // 将Key加密
                        String key = companyBean.getAccessKey().replace(companyBean.getAccessKey().substring(10, 21), "*********");
                        dayCountLogVO.setAccessKey(key);
                    }
                }
            }
            long total = jsonObjects.getTotalElements();
            if (jsonObjects.getTotalElements() > maxTotal) {
                total = maxTotal;
            }
            // 封装返回前端的对象
            PageVO<DayCountLogVO> pageVO = new PageVO<DayCountLogVO>(dayCountSightQO.getCurrent(), dayCountSightQO.getPageSize(), total, dayCountLogVOList);
            dayCountSightDTO.setDayCountLogVOS(pageVO);
        }
        
        return dayCountSightDTO;
    }
    
    @Override
    public PageVO<ServiceAuditVO> getServiceAudit(ServiceAuditQO serviceAuditQO) {
        
        // 分页从ES中获取
        Pageable pageable = PageRequest.of(serviceAuditQO.getCurrent() - 1, serviceAuditQO.getPageSize());
        
        // 开始拼接条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        
        if (serviceAuditQO.getBusinessId() != Constants.ZERO) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.BUSINESSID, serviceAuditQO.getBusinessId());
            boolQueryBuilder.must(termQueryBuilder);
        }
        if (serviceAuditQO.getApiType() != Constants.ZERO) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.APITYPE, serviceAuditQO.getApiType());
            boolQueryBuilder.must(termQueryBuilder);
        }
        if (serviceAuditQO.getApiId() != Constants.ZERO) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.APIID, serviceAuditQO.getApiId());
            boolQueryBuilder.must(termQueryBuilder);
        }
        if (serviceAuditQO.getStatus() != null && serviceAuditQO.getStatus() != "") {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(Constants.ES_STATUS, serviceAuditQO.getStatus());
            boolQueryBuilder.must(termQueryBuilder);
        }
        if (StringUtils.isNotEmpty(serviceAuditQO.getStartTime()) && StringUtils.isNotEmpty(serviceAuditQO.getEndTime())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(Constants.ADD_TIME).from(serviceAuditQO.getStartTime() + Constants.TIME_START).to(serviceAuditQO.getEndTime() + Constants.TIME_END);
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        
        log.info("getServiceAudit query:\n" + boolQueryBuilder);
        
        // 排序
        SortBuilder sortBuilder = SortBuilders.fieldSort(Constants.ADD_TIME).order(SortOrder.DESC);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withIndices(indexName)
            .withTypes(indexType)
            .withQuery(boolQueryBuilder)
            .withSort(sortBuilder)
            .withPageable(pageable)
            .build();
        
        Page<JSONObject> jsonObjects = elasticsearchTemplate.queryForPage(searchQuery, JSONObject.class);
        
        // 返回结果的List集合
        List<ServiceAuditVO> serviceAuditVOList = Lists.newArrayList();
        
        // 接口url集合
        List<String> apiUrls = Lists.newArrayList();
        
        // 客户单位ID集合
        List<String> companyCodes = Lists.newArrayList();
        for (JSONObject jsonObject : jsonObjects) {
            ServiceAuditVO serviceAuditVO = JSONObject.toJavaObject(jsonObject, ServiceAuditVO.class);
            apiUrls.add(serviceAuditVO.getUrl());
            companyCodes.add(serviceAuditVO.getCompanyCode());
            serviceAuditVOList.add(serviceAuditVO);
        }
        
        // 根据客户companyCode集合获取CompanyName
        Map<String, CompanyBean> companyBeanMap = Maps.newHashMap();
        if (companyCodes != null && companyCodes.size() > Constants.ZERO) {
            List<CompanyBean> companyBeanList = companyMapper.getCompanyByCompanyCodes(companyCodes);
            companyBeanList.forEach(companyBean -> companyBeanMap.put(companyBean.getCompanyCode(), companyBean));
        }
        
        // 根据apiId集合获取api信息
        Map<String, ApiSightVO> apiSightVOMap = Maps.newHashMap();
        if (apiUrls != null && apiUrls.size() > Constants.ZERO) {
            List<ApiSightVO> apiByIds = apiInfoMapper.getApiByUrls(apiUrls);
            apiByIds.forEach(apiSightVO -> apiSightVOMap.put(apiSightVO.getUrl(), apiSightVO));
        }
        //标记开关,用于找不到接口类型时,根据接口ID查询
        boolean flag = false;

        for (ServiceAuditVO serviceAuditVO : serviceAuditVOList) {
            if (companyBeanMap.containsKey(serviceAuditVO.getCompanyCode())) {
                serviceAuditVO.setCompanyName(companyBeanMap.get(serviceAuditVO.getCompanyCode()).getCompanyName());
            }
            if (apiSightVOMap.containsKey(serviceAuditVO.getUrl())) {
                serviceAuditVO.setName(apiSightVOMap.get(serviceAuditVO.getUrl()).getName());
                serviceAuditVO.setApiType(apiSightVOMap.get(serviceAuditVO.getUrl()).getTypeName());
            }else{
                flag = true;
            }
        }
        long total = jsonObjects.getTotalElements();
        if (jsonObjects.getTotalElements() > maxTotal) {
            total = maxTotal;
        }

        //如果有找不到类型名称的
        if(flag){
            //获取所有的api类型
            List<ApiTypeBean> apiTypes =apiTypeMapper.getAllApiType();
            //制作类型id与名称的map集合
            Map<String,String> typeMap = Maps.newHashMap();
            apiTypes.forEach(apiTypeBean -> typeMap.put(String.valueOf(apiTypeBean.getId()),apiTypeBean.getTypeName()));
            //遍历结果集合,将如果将将删除后,历史记录显示不出类型名称问题
            for(ServiceAuditVO serviceAuditVO : serviceAuditVOList){
                if(typeMap.containsKey(serviceAuditVO.getApiType())){
                    serviceAuditVO.setApiType(typeMap.get(serviceAuditVO.getApiType()));
                }
            }
        }
        // 封装返回前端的对象
        PageVO<ServiceAuditVO> pageVO = new PageVO<ServiceAuditVO>(serviceAuditQO.getCurrent(), serviceAuditQO.getPageSize(), total, serviceAuditVOList);
        return pageVO;
    }
    
    @Override
    public DayCountSightDTO getDayCount(DayCountSightQO dayCountSightQO) {
        
        // 根据ID获取接口信息
        CompanySightDTO companySightDTO = apiInfoMapper.getApiTypeUrl(dayCountSightQO.getApiId());
        // 变成DayCountSightDTO对象
        DayCountSightDTO dayCountSightDTO = new DayCountSightDTO();
        
        // 如果不为空
        if (dayCountSightDTO != null) {
            dayCountSightDTO.setId(companySightDTO.getId());
            dayCountSightDTO.setName(companySightDTO.getName());
            // 将根地址+类型地址+接口地址拼接
            String apiUrl = companySightDTO.getUrl();
            dayCountSightDTO.setUrl(address + companySightDTO.getTypeUrl() + apiUrl);
            dayCountSightDTO.setAccessDay(dayCountSightQO.getAccessDay());
            
            // 分页从ES中获取
            Pageable pageable = PageRequest.of(dayCountSightQO.getCurrent() - 1, dayCountSightQO.getPageSize());
            // 排序
            SortBuilder sortBuilder = SortBuilders.fieldSort(Constants.ADD_TIME).order(SortOrder.DESC);

            SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(indexName).withTypes(indexType).withQuery(boolQuery().must(termQuery(Constants.APIID, dayCountSightQO.getApiId())).must(termQuery(Constants.BUSINESSID, dayCountSightQO.getBusinessId())).must(matchQuery(Constants.TIME_STAMP, dayCountSightQO.getAccessDay()))).withSort(sortBuilder).withPageable(pageable).build();

            log.info(String.format("查询语句 term: \n {\"query\":%s}", searchQuery));

            Page<JSONObject> jsonObjects = elasticsearchTemplate.queryForPage(searchQuery, JSONObject.class);
            
            // 结果List集合
            List<DayCountLogVO> dayCountLogVOList = Lists.newArrayList();
            // 根据客户单位ID获单位信息
            CompanyBean companyBean = companyMapper.getCompanyById(dayCountSightQO.getBusinessId());
            // 将Key加密
            String key = companyBean.getAccessKey().replace(companyBean.getAccessKey().substring(10, 21), "*********");
            for (JSONObject jsonObject : jsonObjects) {
                DayCountLogVO dayCountLogVO = JSONObject.toJavaObject(jsonObject, DayCountLogVO.class);
                dayCountLogVO.setAccessKey(key);
                dayCountLogVO.setCompanyName(companyBean.getCompanyName());
                dayCountLogVOList.add(dayCountLogVO);
            }
            long total = jsonObjects.getTotalElements();
            if (jsonObjects.getTotalElements() > maxTotal) {
                total = maxTotal;
            }
            // 封装返回前端的对象
            PageVO<DayCountLogVO> pageVO = new PageVO<DayCountLogVO>(dayCountSightQO.getCurrent(), dayCountSightQO.getPageSize(), total, dayCountLogVOList);
            dayCountSightDTO.setDayCountLogVOS(pageVO);
        }
        
        return dayCountSightDTO;
    }
    
}
