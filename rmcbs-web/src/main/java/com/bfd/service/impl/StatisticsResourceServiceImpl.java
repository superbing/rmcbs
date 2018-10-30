package com.bfd.service.impl;

import com.bfd.bean.BusinessUnitsBean;
import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.BusinessUnitsMapper;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.dao.mapper.DataPackageMapper;
import com.bfd.dao.mapper.StatisticsResourceMapper;
import com.bfd.param.vo.statisticsvo.*;
import com.bfd.service.StatisticsResourceService;
import com.bfd.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计查询--资源使用查询serviceImpl
 *
 * @author
 * @date 2018-09-18
 */
@Service
public class StatisticsResourceServiceImpl implements StatisticsResourceService {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String FORMAT_DAY = "%Y-%m-%d";

    public static final String FORMAT_HOUR = "%H";

    public static final String GROUP_HOUR = "%Y-%m-%d %H";

    public static final String GROUP_WEEK = "%Y%u";

    public static final String GROUP_MONTH = "%Y-%m";

    public static final String TIME_MIN_SECOND_START = " 00:00:00";

    public static final String TIME_MIN_SECOND_END = " 23:59:59";

    public static final String timeStr = "time";

    public static final String totalStr = "total";

    public static final String bookStr = "book";

    public static final String formatStr = "format";

    public static final String groupStr = "group";

    @Autowired
    StatisticsResourceMapper statisticsResourceMapper;

    @Autowired
    BusinessUnitsMapper businessUnitsMapper;

    @Autowired
    DataPackageMapper dataPackageMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Override
    public PageVO<ResourceCategoryVO> getDataPackageList(ResourceParamVO resourceParamVO) {
        List<ResourceCategoryVO> result = new ArrayList<>();
        PageHelper.startPage(resourceParamVO.getCurrent(), resourceParamVO.getPageSize());
        List<ResourceCategoryVO> dataPackageList = statisticsResourceMapper.getPackageList(resourceParamVO);
        PageInfo<ResourceCategoryVO> pageInfo = new PageInfo<>(dataPackageList);
        for (ResourceCategoryVO resourceCategoryVO : pageInfo.getList()) {

            resourceCategoryVO.setCreateTime(df.format(resourceCategoryVO.getCreateDate()));

            // 通过数据包id(一级)，查询该一级下所有二级被使用的客户单位数
            resourceCategoryVO.setCompanyNumber(statisticsResourceMapper.queryCompanyNumberByPackage(resourceCategoryVO.getId()));

            // 获取二级数据包列表
            List<ResourceCategoryVO> childrenList = statisticsResourceMapper.getPackageListByParentId(resourceCategoryVO
                    .getId());

            // 通过数据包id获取子节点个数
            resourceCategoryVO.setChildrenNumber(childrenList.size());

            // 通过数据包id查询下面的所有二级数据包的图书种类数量（元数据去重）
            List<Long> ids = new ArrayList<>();
            ids.add(resourceCategoryVO.getId());
            for (ResourceCategoryVO categoryVO : childrenList) {
                ids.add(categoryVO.getId());
            }
            resourceCategoryVO.setBookNumber(ids.size() == 0 ? 0 : statisticsResourceMapper.queryBookNumberByPackage(ids));

            // 通过数据包id查询下面的所有二级数据包的PDF图书数量
            resourceCategoryVO.setPdfNumber(ids.size() == 0 ? 0 : statisticsResourceMapper.queryPdfNumberByPackage(ids));

            // 通过数据包id查询下面的所有二级数据包的EPUB图书数量
            resourceCategoryVO.setEpubNumber(ids.size() == 0 ? 0 : statisticsResourceMapper.queryEpubNumberByPackage(ids));
            result.add(resourceCategoryVO);
        }
        return new PageVO<>(resourceParamVO.getCurrent(), resourceParamVO.getPageSize(), pageInfo.getTotal(), result);
    }

    @Override
    public PageVO<ResourceCategoryVO> getColumnList(ResourceParamVO resourceParamVO) {
        List<ResourceCategoryVO> result = new ArrayList<>();
        PageHelper.startPage(resourceParamVO.getCurrent(), resourceParamVO.getPageSize());
        List<ResourceCategoryVO> columnList = statisticsResourceMapper.getColumnList(resourceParamVO);
        PageInfo<ResourceCategoryVO> pageInfo = new PageInfo<>(columnList);
        for (ResourceCategoryVO resourceCategoryVO : pageInfo.getList()) {

            resourceCategoryVO.setCreateTime(df.format(resourceCategoryVO.getCreateDate()));

            // 通过栏目id(一级)，查询该一级下所有二级被使用的客户单位数
            resourceCategoryVO.setCompanyNumber(statisticsResourceMapper.queryCompanyNumberByColumn(resourceCategoryVO
                    .getId()));

            // 获取二级栏目列表
            List<ResourceCategoryVO> childrenList = statisticsResourceMapper.getColumnListByParentId(resourceCategoryVO
                    .getId());

            // 通过栏目id获取子节点个数
            resourceCategoryVO.setChildrenNumber(childrenList.size());

            // 通过栏目id查询下面的所有二级栏目的图书种类数量（元数据去重）
            List<Long> ids = new ArrayList<>();
            ids.add(resourceCategoryVO.getId());
            for (ResourceCategoryVO categoryVO : childrenList) {
                ids.add(categoryVO.getId());
            }
            resourceCategoryVO.setBookNumber(ids.size() == 0 ? 0 : statisticsResourceMapper.queryBookNumberByColumn(ids));

            // 通过栏目id查询下面的所有二级栏目的PDF图书数量
            resourceCategoryVO.setPdfNumber(ids.size() == 0 ? 0 : statisticsResourceMapper.queryPdfNumberByColumn(ids));

            // 通过栏目id查询下面的所有二级栏目的EPUB图书数量
            resourceCategoryVO.setEpubNumber(ids.size() == 0 ? 0 : statisticsResourceMapper.queryEpubNumberByColumn(ids));
            result.add(resourceCategoryVO);
        }
        return new PageVO<>(resourceParamVO.getCurrent(), resourceParamVO.getPageSize(), pageInfo.getTotal(), result);
    }

    @Override
    public PageVO<ResourceCategoryVO> getPrivateDataPackageList(ResourceParamVO resourceParamVO) {
        List<ResourceCategoryVO> result = new ArrayList<>();
        PageHelper.startPage(resourceParamVO.getCurrent(), resourceParamVO.getPageSize());
        List<ResourceCategoryVO> privatePackageList = statisticsResourceMapper.getPrivatePackageList(resourceParamVO);
        PageInfo<ResourceCategoryVO> pageInfo = new PageInfo<>(privatePackageList);
        for (ResourceCategoryVO resourceCategoryVO : pageInfo.getList()) {

            resourceCategoryVO.setCreateTime(df.format(resourceCategoryVO.getCreateDate()));

            // 通过数据包id查询被使用的客户单位数
            resourceCategoryVO.setCompanyNumber(1);

            // 获取二级数据包列表
            List<ResourceCategoryVO> childrenList = statisticsResourceMapper.getPrivatePackageListByParentId(
                    resourceCategoryVO.getId());

            // 通过数据包id获取子节点个数
            resourceCategoryVO.setChildrenNumber(childrenList.size());

            // 通过数据包id查询下面的所有二级数据包的图书种类数量（元数据去重）
            List<Long> ids = new ArrayList<>();
            ids.add(resourceCategoryVO.getId());
            for (ResourceCategoryVO categoryVO : childrenList) {
                ids.add(categoryVO.getId());
            }
            resourceCategoryVO.setBookNumber(ids.size() == 0 ? 0 : statisticsResourceMapper
                    .queryBookNumberByPrivatePackage(ids));

            // 通过数据包id查询下面的所有二级数据包的PDF图书数量
            resourceCategoryVO.setPdfNumber(ids.size() == 0 ? 0 : statisticsResourceMapper.queryPdfNumberByPrivatePackage
                    (ids));

            // 通过数据包id查询下面的所有二级数据包的EPUB图书数量
            resourceCategoryVO.setEpubNumber(ids.size() == 0 ? 0 : statisticsResourceMapper
                    .queryEpubNumberByPrivatePackage(ids));
            result.add(resourceCategoryVO);
        }
        return new PageVO<>(resourceParamVO.getCurrent(), resourceParamVO.getPageSize(), pageInfo.getTotal(), result);
    }

    @Override
    public PageVO<ResourceCategoryVO> getPrivateColumnList(ResourceParamVO resourceParamVO) {
        List<ResourceCategoryVO> result = new ArrayList<>();
        PageHelper.startPage(resourceParamVO.getCurrent(), resourceParamVO.getPageSize());
        List<ResourceCategoryVO> privateColumnList = statisticsResourceMapper.getPrivateColumnList(resourceParamVO);
        PageInfo<ResourceCategoryVO> pageInfo = new PageInfo<>(privateColumnList);
        for (ResourceCategoryVO resourceCategoryVO : pageInfo.getList()) {

            resourceCategoryVO.setCreateTime(df.format(resourceCategoryVO.getCreateDate()));

            // 通过栏目id查询被使用的客户单位数
            resourceCategoryVO.setCompanyNumber(1);

            // 获取二级栏目列表
            List<ResourceCategoryVO> childrenList = statisticsResourceMapper.getPrivateColumnListByParentId(
                    resourceCategoryVO.getId());

            // 通过栏目id获取子节点个数
            resourceCategoryVO.setChildrenNumber(childrenList.size());

            // 通过栏目id查询下面的所有二级栏目的图书种类数量（元数据去重）
            List<Long> ids = new ArrayList<>();
            ids.add(resourceCategoryVO.getId());
            for (ResourceCategoryVO categoryVO : childrenList) {
                ids.add(categoryVO.getId());
            }
            resourceCategoryVO.setBookNumber(ids.size() == 0 ? 0 : statisticsResourceMapper
                    .queryBookNumberByPrivateColumn(ids));

            // 通过栏目id查询下面的所有二级栏目的PDF图书数量
            resourceCategoryVO.setPdfNumber(ids.size() == 0 ? 0 : statisticsResourceMapper.queryPdfNumberByPrivateColumn
                    (ids));

            // 通过栏目id查询下面的所有二级栏目的EPUB图书数量
            resourceCategoryVO.setEpubNumber(ids.size() == 0 ? 0 : statisticsResourceMapper
                    .queryEpubNumberByPrivateColumn(ids));
            result.add(resourceCategoryVO);
        }
        return new PageVO<>(resourceParamVO.getCurrent(), resourceParamVO.getPageSize(), pageInfo.getTotal(), result);
    }

    @Override
    public PageVO<ResourceCompanyInfoVO> getCompanyListByPackage(ResourceCompanyParamVO resourceCompanyParamVO) {

        // 返回方法体结果集
        List<ResourceCompanyInfoVO> result = new ArrayList<>();

        // 获取一级分类id，二级分类id
        long cid = resourceCompanyParamVO.getCid();
        long sid = resourceCompanyParamVO.getSid();

        // 获取一级分类下二级分类id集合
        List<Long> cidList = new ArrayList<>();
        List<ResourceCategoryVO> childrenList = statisticsResourceMapper.getPackageListByParentId(cid);
        for (int i = 0; i < childrenList.size(); i++) {
            cidList.add(childrenList.get(i).getId());
        }

        // 判断页面传入查询条件是否是按照一级分类查询，如果按照二级分类查询，则需要将二级分类id传入参数对象
        if (sid != 0) {
            List<Long> temp = new ArrayList<>();
            temp.add(sid);
            resourceCompanyParamVO.setList(temp);
        } else {
            if(cidList.size() == 0){
                cidList.add(cid);
                resourceCompanyParamVO.setList(cidList);
            }else{
                resourceCompanyParamVO.setList(cidList);
            }
        }
        PageHelper.startPage(resourceCompanyParamVO.getCurrent(), resourceCompanyParamVO.getPageSize());

        // 查询使用该分类的所有客户单位列表
        List<ResourceCompanyInfoVO> companyList = statisticsResourceMapper.getCompanyListByPackage(resourceCompanyParamVO);
        PageInfo<ResourceCompanyInfoVO> pageInfo = new PageInfo<>(companyList);
        for (ResourceCompanyInfoVO resourceCompanyInfoVO : pageInfo.getList()) {

            // 获取平台商名称
            CompanyBean companyBean = companyMapper.getCompanyById(resourceCompanyInfoVO.getParentCompanyId());
            resourceCompanyInfoVO.setParentCompanyName(companyBean.getCompanyName());

            // 获取客户单位到期时间
            BusinessUnitsBean business = businessUnitsMapper.getBusinessById(resourceCompanyInfoVO.getId());
            if(business != null){
                resourceCompanyInfoVO.setEndTime(business.getEndTime());
            }

            // 获取客户单位拥有的分类数量
            Map<String, Object> param = new HashMap<>(16);
            param.put("companyId", resourceCompanyInfoVO.getId());
            param.put("cidList", cidList);
            resourceCompanyInfoVO.setChildrenNumber(statisticsResourceMapper.getCompanyPackageNumber(param));

            // 获取该客户单位拥有该分类下二级数据包的图书种类数量（元数据去重）
            resourceCompanyInfoVO.setBookNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper
                    .queryCompanyBookNumberByPackage(param));

            // 获取该客户单位拥有该分类下二级数据包的PDF图书数量
            resourceCompanyInfoVO.setPdfNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryCompanyPdfNumberByPackage(param));

            // 获取该客户单位拥有该分类下二级数据包的EPUB图书数量
            resourceCompanyInfoVO.setEpubNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryCompanyEpubNumberByPackage(param));
            result.add(resourceCompanyInfoVO);
        }
        return new PageVO<>(resourceCompanyParamVO.getCurrent(), resourceCompanyParamVO.getPageSize(), pageInfo.getTotal(), result);
    }

    @Override
    public PageVO<ResourceCompanyInfoVO> getCompanyListByColumn(ResourceCompanyParamVO resourceCompanyParamVO) {

        // 返回方法体结果集
        List<ResourceCompanyInfoVO> result = new ArrayList<>();

        // 获取一级分类id，二级分类id
        long cid = resourceCompanyParamVO.getCid();
        long sid = resourceCompanyParamVO.getSid();

        // 获取一级分类下二级分类id集合
        List<Long> cidList = new ArrayList<>();
        List<ResourceCategoryVO> childrenList = statisticsResourceMapper.getColumnListByParentId(cid);
        for (int i = 0; i < childrenList.size(); i++) {
            cidList.add(childrenList.get(i).getId());
        }

        // 判断页面传入查询条件是否是按照一级分类查询，如果按照二级分类查询，则需要将二级分类id传入参数对象
        if (sid != 0) {
            List<Long> temp = new ArrayList<>();
            temp.add(sid);
            resourceCompanyParamVO.setList(temp);
        } else {
            if(cidList.size() == 0){
                cidList.add(cid);
                resourceCompanyParamVO.setList(cidList);
            }else{
                resourceCompanyParamVO.setList(cidList);
            }
        }
        PageHelper.startPage(resourceCompanyParamVO.getCurrent(), resourceCompanyParamVO.getPageSize());

        // 查询使用该分类的所有客户单位列表
        List<ResourceCompanyInfoVO> companyList = statisticsResourceMapper.getCompanyListByColumn
                (resourceCompanyParamVO);
        PageInfo<ResourceCompanyInfoVO> pageInfo = new PageInfo<>(companyList);
        for (ResourceCompanyInfoVO resourceCompanyInfoVO : pageInfo.getList()) {

            // 获取平台商名称
            CompanyBean companyBean = companyMapper.getCompanyById(resourceCompanyInfoVO.getParentCompanyId());
            resourceCompanyInfoVO.setParentCompanyName(companyBean.getCompanyName());

            // 获取客户单位到期时间
            BusinessUnitsBean business = businessUnitsMapper.getBusinessById(resourceCompanyInfoVO.getId());
            if(business != null){
                resourceCompanyInfoVO.setEndTime(business.getEndTime());
            }

            // 获取客户单位拥有的分类数量
            Map<String, Object> param = new HashMap<>(16);
            param.put("companyId", resourceCompanyInfoVO.getId());
            param.put("cidList", cidList);
            resourceCompanyInfoVO.setChildrenNumber(statisticsResourceMapper.getCompanyColumnNumber(param));

            // 获取该客户单位拥有该分类下二级栏目的图书种类数量（元数据去重）
            resourceCompanyInfoVO.setBookNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper
                    .queryCompanyBookNumberByColumn(param));

            // 获取该客户单位拥有该分类下二级栏目的PDF图书数量
            resourceCompanyInfoVO.setPdfNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryCompanyPdfNumberByColumn(param));

            // 获取该客户单位拥有该分类下二级栏目的EPUB图书数量
            resourceCompanyInfoVO.setEpubNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryCompanyEpubNumberByColumn(param));
            result.add(resourceCompanyInfoVO);
        }
        return new PageVO<>(resourceCompanyParamVO.getCurrent(), resourceCompanyParamVO.getPageSize(), pageInfo.getTotal(), result);
    }

    @Override
    public ResourceCompanyInfoVO getCompanyListByPrivatePackage(ResourceCompanyParamVO resourceCompanyParamVO) {

        // 获取一级分类id，二级分类id
        long cid = resourceCompanyParamVO.getCid();
        long sid = resourceCompanyParamVO.getSid();

        // 获取一级分类下二级分类id集合
        List<Long> cidList = new ArrayList<>();
        List<ResourceCategoryVO> childrenList = statisticsResourceMapper.getPrivatePackageListByParentId(cid);
        for (int i = 0; i < childrenList.size(); i++) {
            cidList.add(childrenList.get(i).getId());
        }

        // 判断页面传入查询条件是否是按照一级分类查询，如果按照二级分类查询，则需要将二级分类id传入参数对象
        if(cidList.size() == 0){
            cidList.add(cid);
            resourceCompanyParamVO.setList(cidList);
        }else{
            resourceCompanyParamVO.setList(cidList);
        }

        // 查询使用该分类客户单位（此处只有一个客户单位，个性化数据包只属于某一个客户单位 ）
        ResourceCompanyInfoVO companyInfo = statisticsResourceMapper.getCompanyByPrivatePackage(resourceCompanyParamVO);

        // 获取平台商名称
        CompanyBean companyBean = companyMapper.getCompanyById(companyInfo.getParentCompanyId());
        companyInfo.setParentCompanyName(companyBean.getCompanyName());

        // 获取客户单位到期时间
        BusinessUnitsBean business = businessUnitsMapper.getBusinessById(companyInfo.getId());
        if(business != null){
            companyInfo.setEndTime(business.getEndTime());
        }

        // 获取客户单位拥有的个性化数据包数量
        if(childrenList.size() == 0){
            companyInfo.setChildrenNumber(1);
        }else{
            companyInfo.setChildrenNumber(childrenList.size());
        }

        // 通过个性化数据包id查询下面的所有二级数据包的图书种类数量（元数据去重）
        companyInfo.setBookNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryBookNumberByPrivatePackage
                (cidList));

        // 获取该客户单位拥有该分类下二级数据包的PDF图书数量
        companyInfo.setPdfNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryPdfNumberByPrivatePackage(cidList));

        // 获取该客户单位拥有该分类下二级数据包的EPUB图书数量
        companyInfo.setEpubNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryEpubNumberByPrivatePackage(cidList));
        return companyInfo;
    }

    @Override
    public ResourceCompanyInfoVO getCompanyListByPrivateColumn(ResourceCompanyParamVO resourceCompanyParamVO) {

        // 获取一级分类id，二级分类id
        long cid = resourceCompanyParamVO.getCid();

        // 获取一级分类下二级分类id集合
        List<Long> cidList = new ArrayList<>();
        List<ResourceCategoryVO> childrenList = statisticsResourceMapper.getPrivateColumnListByParentId(cid);
        for (int i = 0; i < childrenList.size(); i++) {
            cidList.add(childrenList.get(i).getId());
        }

        // 判断页面传入查询条件是否是按照一级分类查询，如果按照二级分类查询，则需要将二级分类id传入参数对象
        if(cidList.size() == 0){
            cidList.add(cid);
            resourceCompanyParamVO.setList(cidList);
        }else{
            resourceCompanyParamVO.setList(cidList);
        }

        // 查询使用该分类的客户单位（此处只有一个客户单位，个性化数据包只属于某一个客户单位 ）
        ResourceCompanyInfoVO companyInfo = statisticsResourceMapper.getCompanyByPrivateColumn(resourceCompanyParamVO);

        // 获取平台商名称
        CompanyBean companyBean = companyMapper.getCompanyById(companyInfo.getParentCompanyId());
        companyInfo.setParentCompanyName(companyBean.getCompanyName());

        // 获取客户单位到期时间
        BusinessUnitsBean business = businessUnitsMapper.getBusinessById(companyInfo.getId());
        if(business != null){
            companyInfo.setEndTime(business.getEndTime());
        }

        // 获取客户单位拥有的个性化栏目数量
        if(childrenList.size() == 0){
            companyInfo.setChildrenNumber(1);
        }else{
            companyInfo.setChildrenNumber(childrenList.size());
        }

        // 通过个性化栏目id查询下面的所有二级栏目的图书种类数量（元数据去重）
        companyInfo.setBookNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryBookNumberByPrivateColumn
                (cidList));

        // 获取该客户单位拥有该分类下二级栏目的PDF图书数量
        companyInfo.setPdfNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryPdfNumberByPrivateColumn
                (cidList));

        // 获取该客户单位拥有该分类下二级栏目的EPUB图书数量
        companyInfo.setEpubNumber(cidList.size() == 0 ? 0 : statisticsResourceMapper.queryEpubNumberByPrivateColumn
                (cidList));
        return companyInfo;
    }

    @Override
    public List<ResourceCategoryVO> getDropDownList(long id, String type) {

        List<ResourceCategoryVO> list;
        switch (type) {
            case "sjb":
                list = statisticsResourceMapper.getPackageListByParentId(id);
                break;
            case "lm":
                list = statisticsResourceMapper.getColumnListByParentId(id);
                break;
            case "gxhsjb":
                list = statisticsResourceMapper.getPrivatePackageListByParentId(id);
                break;
            default:
                list = statisticsResourceMapper.getPrivateColumnListByParentId(id);
        }
        return list;
    }

    @Override
    public Map<String, Object> getPackageUseDetailTotal(TimeRangeParamVO timeRangeParamVO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("id", timeRangeParamVO.getId());
        param.put("startTime", timeRangeParamVO.getStartTime() + TIME_MIN_SECOND_START);
        param.put("endTime", timeRangeParamVO.getEndTime() + TIME_MIN_SECOND_END);

        // 查询新增客户单位数 ，新增图书数，返回结果
        Map<String, Object> result = new HashMap<>(16);
        result.put("addCompanyTotal", statisticsResourceMapper.getPackageAddCompanyTotalByPackage(param));
        result.put("addBookTotal", statisticsResourceMapper.getPackageAddBookTotalByPackage(param));
        return result;
    }

    @Override
    public List<Map<String, Object>> getPackageUseDetailTrend(TimeRangeParamVO timeRangeParamVO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("id", timeRangeParamVO.getId());
        param.put("startTime", timeRangeParamVO.getStartTime() + TIME_MIN_SECOND_START);
        param.put("endTime", timeRangeParamVO.getEndTime() + TIME_MIN_SECOND_END);
        param.put(formatStr, FORMAT_DAY);
        param.put(groupStr, FORMAT_DAY);
        List<Map<String, Object>> list;
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> trendMap = new HashMap<>(16);
        List<String> collect = new ArrayList<>();
        String queryType = timeRangeParamVO.getQueryType();
        switch (timeRangeParamVO.getTimeType()) {
            case "hour":
                param.put(formatStr, FORMAT_HOUR);
                param.put(groupStr, GROUP_HOUR);
                list = queryType.equals(bookStr) ? statisticsResourceMapper.getPackageUseDetailBookTrend(param) : statisticsResourceMapper.getPackageUseDetailCompanyTrend(param);
                Map<String, String> timeMaps = DateUtils.getTimeMaps();
                for (Map.Entry<String, String> entry : timeMaps.entrySet()) {
                    trendMap.put(timeStr, entry.getValue());
                    for (int i = 0; i < list.size(); i++) {
                        if (entry.getKey().equals(list.get(i).get(timeStr))) {
                            trendMap.put(totalStr, list.get(i).get(totalStr));
                        }
                    }
                    if (!trendMap.containsKey(totalStr)) {
                        trendMap.put(totalStr, 0);
                    }
                    result.add(trendMap);
                    trendMap = new HashMap<>(16);
                }
                break;
            case "day":
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, FORMAT_DAY);
                list = queryType.equals(bookStr) ? statisticsResourceMapper.getPackageUseDetailBookTrend(param) : statisticsResourceMapper.getPackageUseDetailCompanyTrend(param);
                collect = DateUtils.collectRangeDates(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
                break;
            case "week":
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, GROUP_WEEK);
                list = queryType.equals(bookStr) ? statisticsResourceMapper.getPackageUseDetailBookTrend(param) : statisticsResourceMapper.getPackageUseDetailCompanyTrend(param);
                for (int i = 0; i < list.size(); i++) {
                    String monday = DateUtils.getMonday(list.get(i).get(timeStr).toString());
                    list.get(i).put(timeStr, monday);
                }
                collect = DateUtils.getFirstMondayOfTimes(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
                break;
            default:
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, GROUP_MONTH);
                list = queryType.equals(bookStr) ? statisticsResourceMapper.getPackageUseDetailBookTrend(param) : statisticsResourceMapper.getPackageUseDetailCompanyTrend(param);
                for (int i = 0; i < list.size(); i++) {
                    String monday = DateUtils.getFirstDayOfMonth(list.get(i).get(timeStr).toString());
                    list.get(i).put(timeStr, monday);
                }
                collect = DateUtils.getFirstDayOfMonths(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
        }
        for (int i = 0; i < collect.size(); i++) {
            trendMap.put(timeStr, collect.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (collect.get(i).equals(list.get(j).get(timeStr))) {
                    trendMap.put(totalStr, list.get(j).get(totalStr));
                }
            }
            if (!trendMap.containsKey(totalStr)) {
                trendMap.put(totalStr, 0);
            }
            result.add(trendMap);
            trendMap = new HashMap<>(16);
        }
        return result;
    }

    @Override
    public Map<String, Object> getColumnUseDetailTotal(TimeRangeParamVO timeRangeParamVO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("id", timeRangeParamVO.getId());
        param.put("startTime", timeRangeParamVO.getStartTime() + TIME_MIN_SECOND_START);
        param.put("endTime", timeRangeParamVO.getEndTime() + TIME_MIN_SECOND_END);

        // 查询新增客户单位数 ，新增图书数，返回结果
        Map<String, Object> result = new HashMap<>(16);
        result.put("addCompanyTotal", statisticsResourceMapper.getColumnAddCompanyTotalByColumn(param));
        result.put("addBookTotal", statisticsResourceMapper.getColumnAddBookTotalByColumn(param));
        return result;
    }

    @Override
    public List<Map<String, Object>> getColumnUseDetailTrend(TimeRangeParamVO timeRangeParamVO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("id", timeRangeParamVO.getId());
        param.put("startTime", timeRangeParamVO.getStartTime() + TIME_MIN_SECOND_START);
        param.put("endTime", timeRangeParamVO.getEndTime() + TIME_MIN_SECOND_END);
        param.put(formatStr, FORMAT_DAY);
        param.put(groupStr, FORMAT_DAY);
        List<Map<String, Object>> list;
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> trendMap = new HashMap<>(16);
        List<String> collect = new ArrayList<>();
        String queryType = timeRangeParamVO.getQueryType();
        switch (timeRangeParamVO.getTimeType()) {
            case "hour":
                param.put(formatStr, FORMAT_HOUR);
                param.put(groupStr, GROUP_HOUR);
                list = queryType.equals(bookStr) ? statisticsResourceMapper.getColumnUseDetailBookTrend(param) : statisticsResourceMapper.getColumnUseDetailCompanyTrend(param);
                Map<String, String> timeMaps = DateUtils.getTimeMaps();
                for (Map.Entry<String, String> entry : timeMaps.entrySet()) {
                    trendMap.put(timeStr, entry.getValue());
                    for (int i = 0; i < list.size(); i++) {
                        if (entry.getKey().equals(list.get(i).get(timeStr))) {
                            trendMap.put(totalStr, list.get(i).get(totalStr));
                        }
                    }
                    if (!trendMap.containsKey(totalStr)) {
                        trendMap.put(totalStr, 0);
                    }
                    result.add(trendMap);
                    trendMap = new HashMap<>(16);
                }
                break;
            case "day":
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, FORMAT_DAY);
                list = queryType.equals(bookStr) ? statisticsResourceMapper.getColumnUseDetailBookTrend(param) : statisticsResourceMapper.getColumnUseDetailCompanyTrend(param);
                collect = DateUtils.collectRangeDates(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
                break;
            case "week":
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, GROUP_WEEK);
                list = queryType.equals(bookStr) ? statisticsResourceMapper.getColumnUseDetailBookTrend(param) : statisticsResourceMapper.getColumnUseDetailCompanyTrend(param);
                for (int i = 0; i < list.size(); i++) {
                    String monday = DateUtils.getMonday(list.get(i).get(timeStr).toString());
                    list.get(i).put(timeStr, monday);
                }
                collect = DateUtils.getFirstMondayOfTimes(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
                break;
            default:
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, GROUP_MONTH);
                list = queryType.equals(bookStr) ? statisticsResourceMapper.getColumnUseDetailBookTrend(param) : statisticsResourceMapper.getColumnUseDetailCompanyTrend(param);
                for (int i = 0; i < list.size(); i++) {
                    String monday = DateUtils.getFirstDayOfMonth(list.get(i).get(timeStr).toString());
                    list.get(i).put(timeStr, monday);
                }
                collect = DateUtils.getFirstDayOfMonths(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
        }
        for (int i = 0; i < collect.size(); i++) {
            trendMap.put(timeStr, collect.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (collect.get(i).equals(list.get(j).get(timeStr))) {
                    trendMap.put(totalStr, list.get(j).get(totalStr));
                }
            }
            if (!trendMap.containsKey(totalStr)) {
                trendMap.put(totalStr, 0);
            }
            result.add(trendMap);
            trendMap = new HashMap<>(16);
        }
        return result;
    }

    @Override
    public Map<String, Object> getPrivatePackageUseDetailTotal(TimeRangeParamVO timeRangeParamVO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("id", timeRangeParamVO.getId());
        param.put("startTime", timeRangeParamVO.getStartTime() + TIME_MIN_SECOND_START);
        param.put("endTime", timeRangeParamVO.getEndTime() + TIME_MIN_SECOND_END);

        // 查询新增图书数，返回结果
        Map<String, Object> result = new HashMap<>(16);
        result.put("addBookTotal", statisticsResourceMapper.getPrivatePackageAddBookTotalByPackage(param));
        return result;
    }

    @Override
    public List<Map<String, Object>> getPrivatePackageUseDetailTrend(TimeRangeParamVO timeRangeParamVO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("id", timeRangeParamVO.getId());
        param.put("startTime", timeRangeParamVO.getStartTime() + TIME_MIN_SECOND_START);
        param.put("endTime", timeRangeParamVO.getEndTime() + TIME_MIN_SECOND_END);
        param.put(formatStr, FORMAT_DAY);
        param.put(groupStr, FORMAT_DAY);
        List<Map<String, Object>> list;
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> trendMap = new HashMap<>(16);
        List<String> collect = new ArrayList<>();
        switch (timeRangeParamVO.getTimeType()) {
            case "hour":
                param.put(formatStr, FORMAT_HOUR);
                param.put(groupStr, GROUP_HOUR);
                list = statisticsResourceMapper.getPrivatePackageUseDetailBookTrend(param);
                Map<String, String> timeMaps = DateUtils.getTimeMaps();
                for (Map.Entry<String, String> entry : timeMaps.entrySet()) {
                    trendMap.put(timeStr, entry.getValue());
                    for (int i = 0; i < list.size(); i++) {
                        if (entry.getKey().equals(list.get(i).get(timeStr))) {
                            trendMap.put(totalStr, list.get(i).get(totalStr));
                        }
                    }
                    if (!trendMap.containsKey(totalStr)) {
                        trendMap.put(totalStr, 0);
                    }
                    result.add(trendMap);
                    trendMap = new HashMap<>(16);
                }
                break;
            case "day":
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, FORMAT_DAY);
                list = statisticsResourceMapper.getPrivatePackageUseDetailBookTrend(param);
                collect = DateUtils.collectRangeDates(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
                break;
            case "week":
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, GROUP_WEEK);
                list = statisticsResourceMapper.getPrivatePackageUseDetailBookTrend(param);
                for (int i = 0; i < list.size(); i++) {
                    String monday = DateUtils.getMonday(list.get(i).get(timeStr).toString());
                    list.get(i).put(timeStr, monday);
                }
                collect = DateUtils.getFirstMondayOfTimes(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
                break;
            default:
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, GROUP_MONTH);
                list = statisticsResourceMapper.getPrivatePackageUseDetailBookTrend(param);
                for (int i = 0; i < list.size(); i++) {
                    String monday = DateUtils.getFirstDayOfMonth(list.get(i).get(timeStr).toString());
                    list.get(i).put(timeStr, monday);
                }
                collect = DateUtils.getFirstDayOfMonths(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
        }
        for (int i = 0; i < collect.size(); i++) {
            trendMap.put(timeStr, collect.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (collect.get(i).equals(list.get(j).get(timeStr))) {
                    trendMap.put(totalStr, list.get(j).get(totalStr));
                }
            }
            if (!trendMap.containsKey(totalStr)) {
                trendMap.put(totalStr, 0);
            }
            result.add(trendMap);
            trendMap = new HashMap<>(16);
        }
        return result;
    }

    @Override
    public Map<String, Object> getPrivateColumnUseDetailTotal(TimeRangeParamVO timeRangeParamVO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("id", timeRangeParamVO.getId());
        param.put("startTime", timeRangeParamVO.getStartTime() + TIME_MIN_SECOND_START);
        param.put("endTime", timeRangeParamVO.getEndTime() + TIME_MIN_SECOND_END);

        // 查询新增图书数，返回结果
        Map<String, Object> result = new HashMap<>(16);
        result.put("addBookTotal", statisticsResourceMapper.getPrivateColumnAddBookTotalByColumn(param));
        return result;
    }

    @Override
    public List<Map<String, Object>> getPrivateColumnUseDetailTrend(TimeRangeParamVO timeRangeParamVO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("id", timeRangeParamVO.getId());
        param.put("startTime", timeRangeParamVO.getStartTime() + TIME_MIN_SECOND_START);
        param.put("endTime", timeRangeParamVO.getEndTime() + TIME_MIN_SECOND_END);
        param.put(formatStr, FORMAT_DAY);
        param.put(groupStr, FORMAT_DAY);
        List<Map<String, Object>> list;
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> trendMap = new HashMap<>(16);
        List<String> collect = new ArrayList<>();
        switch (timeRangeParamVO.getTimeType()) {
            case "hour":
                param.put(formatStr, FORMAT_HOUR);
                param.put(groupStr, GROUP_HOUR);
                list = statisticsResourceMapper.getPrivateColumnUseDetailBookTrend(param);
                Map<String, String> timeMaps = DateUtils.getTimeMaps();
                for (Map.Entry<String, String> entry : timeMaps.entrySet()) {
                    trendMap.put(timeStr, entry.getValue());
                    for (int i = 0; i < list.size(); i++) {
                        if (entry.getKey().equals(list.get(i).get(timeStr))) {
                            trendMap.put(totalStr, list.get(i).get(totalStr));
                        }
                    }
                    if (!trendMap.containsKey(totalStr)) {
                        trendMap.put(totalStr, 0);
                    }
                    result.add(trendMap);
                    trendMap = new HashMap<>(16);
                }
                break;
            case "day":
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, FORMAT_DAY);
                list = statisticsResourceMapper.getPrivateColumnUseDetailBookTrend(param);
                collect = DateUtils.collectRangeDates(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
                break;
            case "week":
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, GROUP_WEEK);
                list = statisticsResourceMapper.getPrivateColumnUseDetailBookTrend(param);
                for (int i = 0; i < list.size(); i++) {
                    String monday = DateUtils.getMonday(list.get(i).get(timeStr).toString());
                    list.get(i).put(timeStr, monday);
                }
                collect = DateUtils.getFirstMondayOfTimes(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
                break;
            default:
                param.put(formatStr, FORMAT_DAY);
                param.put(groupStr, GROUP_MONTH);
                list = statisticsResourceMapper.getPrivateColumnUseDetailBookTrend(param);
                for (int i = 0; i < list.size(); i++) {
                    String monday = DateUtils.getFirstDayOfMonth(list.get(i).get(timeStr).toString());
                    list.get(i).put(timeStr, monday);
                }
                collect = DateUtils.getFirstDayOfMonths(timeRangeParamVO.getStartTime(), timeRangeParamVO.getEndTime());
        }
        for (int i = 0; i < collect.size(); i++) {
            trendMap.put(timeStr, collect.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (collect.get(i).equals(list.get(j).get(timeStr))) {
                    trendMap.put(totalStr, list.get(j).get(totalStr));
                }
            }
            if (!trendMap.containsKey(totalStr)) {
                trendMap.put(totalStr, 0);
            }
            result.add(trendMap);
            trendMap = new HashMap<>(16);
        }
        return result;
    }
}
