package com.bfd.service;

import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.statisticsvo.*;

import java.util.List;
import java.util.Map;

/**
 * 统计查询--资源使用查询service服务层
 *
 * @author
 * @date 2018-09-18
 */
public interface StatisticsResourceService {

    /**
     * 获取数据包列表
     *
     * @return
     */
    PageVO<ResourceCategoryVO> getDataPackageList(ResourceParamVO resourceParamVO);

    /**
     * 获取栏目列表
     *
     * @return
     */
    PageVO<ResourceCategoryVO> getColumnList(ResourceParamVO resourceParamVO);

    /**
     * 获取个性化数据包列表
     *
     * @return
     */
    PageVO<ResourceCategoryVO> getPrivateDataPackageList(ResourceParamVO resourceParamVO);

    /**
     * 获取个性化栏目列表
     *
     * @return
     */
    PageVO<ResourceCategoryVO> getPrivateColumnList(ResourceParamVO resourceParamVO);


    /**
     * 根据数据包获取客户单位列表
     *
     * @return
     */
    PageVO<ResourceCompanyInfoVO> getCompanyListByPackage(ResourceCompanyParamVO resourceCompanyParamVO);

    /**
     * 根据栏目获取客户单位列表
     *
     * @return
     */
    PageVO<ResourceCompanyInfoVO> getCompanyListByColumn(ResourceCompanyParamVO resourceCompanyParamVO);


    /**
     * 根据个性化数据包获取客户单位
     *
     * @return
     */
    ResourceCompanyInfoVO getCompanyListByPrivatePackage(ResourceCompanyParamVO resourceCompanyParamVO);

    /**
     * 根据个性化栏目获取客户单位
     *
     * @return
     */
    ResourceCompanyInfoVO getCompanyListByPrivateColumn(ResourceCompanyParamVO resourceCompanyParamVO);

    /**
     * 根据一级分类id获取二级分类列表
     *
     * @return
     */
    List<ResourceCategoryVO> getDropDownList(long id, String type);

    /**
     * 数据包--客户单位使用明细--新增数
     *
     * @return
     */
    Map<String, Object> getPackageUseDetailTotal(TimeRangeParamVO timeRangeParamVO);

    /**
     * 数据包--客户单位使用明细--趋势图
     *
     * @return
     */
    List<Map<String,Object>> getPackageUseDetailTrend(TimeRangeParamVO timeRangeParamVO);

    /**
     * 栏目--客户单位使用明细--新增数
     *
     * @return
     */
    Map<String, Object> getColumnUseDetailTotal(TimeRangeParamVO timeRangeParamVO);

    /**
     * 栏目--客户单位使用明细--趋势图
     *
     * @return
     */
    List<Map<String,Object>> getColumnUseDetailTrend(TimeRangeParamVO timeRangeParamVO);

    /**
     * 个性化数据包--客户单位使用明细--新增数
     *
     * @return
     */
    Map<String, Object> getPrivatePackageUseDetailTotal(TimeRangeParamVO timeRangeParamVO);

    /**
     * 个性化数据包--客户单位使用明细--趋势图
     *
     * @return
     */
    List<Map<String,Object>> getPrivatePackageUseDetailTrend(TimeRangeParamVO timeRangeParamVO);

    /**
     * 个性化栏目--客户单位使用明细--新增数
     *
     * @return
     */
    Map<String, Object> getPrivateColumnUseDetailTotal(TimeRangeParamVO timeRangeParamVO);

    /**
     * 个性化栏目--客户单位使用明细--趋势图
     *
     * @return
     */
    List<Map<String,Object>> getPrivateColumnUseDetailTrend(TimeRangeParamVO timeRangeParamVO);

}
