package com.bfd.dao.mapper;

import com.bfd.bean.ColumnBean;
import com.bfd.param.vo.statisticsvo.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 统计查询--资源使用查询mapper
 *
 * @author
 * @date 2018-09-18
 */
@Repository
public interface StatisticsResourceMapper {

    // 查询数据包列表=====================================================================================================
    /**
     * 查询一级数据包列表
     *
     * @param resourceParamVO 查询条件
     * @return
     */
    List<ResourceCategoryVO> getPackageList(ResourceParamVO resourceParamVO);

    /**
     * 根据数据包id获取下一级数据包列表
     *
     * @param id 数据包id
     * @return
     */
    List<ResourceCategoryVO> getPackageListByParentId(long id);

    /**
     * 根据数据包id获取客户单位数
     *
     * @param id 数据包id
     * @return
     */
    int queryCompanyNumberByPackage(long id);

    /**
     * 根据数据包id获取图书种类数量
     *
     * @param list 数据包id集合
     * @return
     */
    int queryBookNumberByPackage(List<Long> list);

    /**
     * 通过数据包id查询PDF图书数量
     *
     * @param list 数据包id集合
     * @return
     */
    int queryPdfNumberByPackage(List<Long> list);

    /**
     * 通过数据包id查询EPUB图书数量
     *
     * @param list 数据包id集合
     * @return
     */
    int queryEpubNumberByPackage(List<Long> list);

    // 查询栏目列表======================================================================================================
    /**
     * 查询一级栏目列表
     *
     * @param resourceParamVO 查询条件
     * @return
     */
    List<ResourceCategoryVO> getColumnList(ResourceParamVO resourceParamVO);

    /**
     * 根据id获取下一级栏目列表
     *
     * @param id 栏目id
     * @return
     */
    List<ResourceCategoryVO> getColumnListByParentId(long id);

    /**
     * 根据栏目id获取客户单位数
     *
     * @param id 栏目id
     * @return
     */
    int queryCompanyNumberByColumn(long id);

    /**
     * 根据栏目id获取图书种类数量
     *
     * @param list 栏目id集合
     * @return
     */
    int queryBookNumberByColumn(List<Long> list);

    /**
     * 通过栏目id查询PDF图书数量
     *
     * @param list 栏目id集合
     * @return
     */
    int queryPdfNumberByColumn(List<Long> list);

    /**
     * 通过栏目id查询EPUB图书数量
     *
     * @param list 栏目id集合
     * @return
     */
    int queryEpubNumberByColumn(List<Long> list);

    // 查询个性化数据包列表===============================================================================================
    /**
     * 查询一级个性化数据包列表
     *
     * @param resourceParamVO 查询条件
     * @return
     */
    List<ResourceCategoryVO> getPrivatePackageList(ResourceParamVO resourceParamVO);

    /**
     * 根据数据包id获取下一级数据包列表
     *
     * @param id 数据包id
     * @return
     */
    List<ResourceCategoryVO> getPrivatePackageListByParentId(long id);

    /**
     * 根据数据包id获取图书种类数量
     *
     * @param list 数据包id集合
     * @return
     */
    int queryBookNumberByPrivatePackage(List<Long> list);

    /**
     * 通过数据包id查询PDF图书数量
     *
     * @param list 数据包id集合
     * @return
     */
    int queryPdfNumberByPrivatePackage(List<Long> list);

    /**
     * 通过数据包id查询EPUB图书数量
     *
     * @param list 数据包id集合
     * @return
     */
    int queryEpubNumberByPrivatePackage(List<Long> list);

    // 查询个性化栏目列表=================================================================================================
    /**
     * 查询一级个性化栏目列表
     *
     * @param resourceParamVO 查询条件
     * @return
     */
    List<ResourceCategoryVO> getPrivateColumnList(ResourceParamVO resourceParamVO);

    /**
     * 根据栏目id获取下一级栏目列表
     *
     * @param id 栏目id
     * @return
     */
    List<ResourceCategoryVO> getPrivateColumnListByParentId(long id);

    /**
     * 根据栏目id获取图书种类数量
     *
     * @param list 栏目id集合
     * @return
     */
    int queryBookNumberByPrivateColumn(List<Long> list);

    /**
     * 通过栏目id查询PDF图书数量
     *
     * @param list 数据包id集合
     * @return
     */
    int queryPdfNumberByPrivateColumn(List<Long> list);

    /**
     * 通过栏目id查询EPUB图书数量
     *
     * @param list 栏目id集合
     * @return
     */
    int queryEpubNumberByPrivateColumn(List<Long> list);

    // 通过数据包查询单位列表==============================================================================================
    /**
     * 通过数据包id查询客户单位列表
     *
     * @param resourceCompanyParamVO 查询条件
     * @return
     */
    List<ResourceCompanyInfoVO>  getCompanyListByPackage(ResourceCompanyParamVO resourceCompanyParamVO);

    /**
     * 通过客户单位id和数据包idList查询该单位在该数据包下拥有的二级数据包数量
     *
     * @param param 查询条件
     * @return
     */
    int getCompanyPackageNumber(Map<String, Object> param);

    /**
     * 通过客户单位id和数据包idList查询该单位在该数据包下拥有的二级数据包拥有的图书种类
     *
     * @param param 查询条件
     * @return
     */
    int queryCompanyBookNumberByPackage(Map<String, Object> param);

    /**
     * 通过客户单位id和数据包idList查询该单位在该数据包下拥有的二级数据包拥有的PDF图书种类
     *
     * @param param 查询条件
     * @return
     */
    int queryCompanyPdfNumberByPackage(Map<String, Object> param);

    /**
     * 通过客户单位id和数据包idList查询该单位在该数据包下拥有的二级数据包拥有的PDF图书种类
     *
     * @param param 查询条件
     * @return
     */
    int queryCompanyEpubNumberByPackage(Map<String, Object> param);

    // 通过栏目查询单位列表==============================================================================================
    /**
     * 通过栏目id查询客户单位列表
     *
     * @param resourceCompanyParamVO 查询条件
     * @return
     */
    List<ResourceCompanyInfoVO>  getCompanyListByColumn(ResourceCompanyParamVO resourceCompanyParamVO);

    /**
     * 通过客户单位id和栏目idList查询该单位在该栏目下拥有的二级栏目数量
     *
     * @param param 查询条件
     * @return
     */
    int getCompanyColumnNumber(Map<String, Object> param);

    /**
     * 通过客户单位id和栏目idList查询该单位在该栏目下拥有的二级栏目拥有的图书种类
     *
     * @param param 查询条件
     * @return
     */
    int queryCompanyBookNumberByColumn(Map<String, Object> param);

    /**
     * 通过客户单位id和栏目idList查询该单位在该栏目下拥有的二级栏目拥有的PDF图书种类
     *
     * @param param 查询条件
     * @return
     */
    int queryCompanyPdfNumberByColumn(Map<String, Object> param);

    /**
     * 通过客户单位id和栏目idList查询该单位在该栏目下拥有的二级栏目拥有的PDF图书种类
     *
     * @param param 查询条件
     * @return
     */
    int queryCompanyEpubNumberByColumn(Map<String, Object> param);

    // 通过个性化数据包查询单位列表========================================================================================
    /**
     * 通过个性化数据包id查询客户单位
     *
     * @param resourceCompanyParamVO 查询条件
     * @return
     */
    ResourceCompanyInfoVO getCompanyByPrivatePackage(ResourceCompanyParamVO resourceCompanyParamVO);

    // 通过个性化栏目查询单位列表==========================================================================================
    /**
     * 通过个性化栏目id查询客户单位
     *
     * @param resourceCompanyParamVO 查询条件
     * @return
     */
    ResourceCompanyInfoVO getCompanyByPrivateColumn(ResourceCompanyParamVO resourceCompanyParamVO);

    // 数据包--客户单位使用明细==========================================================================================
    /**
     * 数据包--客户单位使用明细--新增客户单位数
     *
     * @param param 查询条件
     * @return
     */
    int getPackageAddCompanyTotalByPackage(Map<String,Object> param);

    /**
     * 数据包--客户单位使用明细--新增图书数
     *
     * @param param 查询条件
     * @return
     */
    int getPackageAddBookTotalByPackage(Map<String,Object> param);

    /**
     * 数据包--客户单位使用明细--新增图书趋势图
     *
     * @param param 查询条件
     * @return
     */
    List<Map<String,Object>> getPackageUseDetailBookTrend(Map<String,Object> param);

    /**
     * 数据包--客户单位使用明细--新增单位趋势图
     *
     * @param param 查询条件
     * @return
     */
    List<Map<String,Object>> getPackageUseDetailCompanyTrend(Map<String,Object> param);

    // 栏目--客户单位使用明细==========================================================================================
    /**
     * 栏目--客户单位使用明细--新增客户单位数
     *
     * @param param 查询条件
     * @return
     */
    int getColumnAddCompanyTotalByColumn(Map<String,Object> param);

    /**
     * 栏目--客户单位使用明细--新增图书数
     *
     * @param param 查询条件
     * @return
     */
    int getColumnAddBookTotalByColumn(Map<String,Object> param);

    /**
     * 栏目--客户单位使用明细--新增图书趋势图
     *
     * @param param 查询条件
     * @return
     */
    List<Map<String,Object>> getColumnUseDetailBookTrend(Map<String,Object> param);

    /**
     * 栏目--客户单位使用明细--新增单位趋势图
     *
     * @param param 查询条件
     * @return
     */
    List<Map<String,Object>> getColumnUseDetailCompanyTrend(Map<String,Object> param);

    // 个性化数据包--客户单位使用明细======================================================================================
    /**
     * 个性化数据包--客户单位使用明细--新增图书数
     *
     * @param param 查询条件
     * @return
     */
    int getPrivatePackageAddBookTotalByPackage(Map<String,Object> param);

    /**
     * 个性化数据包--客户单位使用明细--新增图书趋势图
     *
     * @param param 查询条件
     * @return
     */
    List<Map<String,Object>> getPrivatePackageUseDetailBookTrend(Map<String,Object> param);

    // 个性化栏目--客户单位使用明细======================================================================================
    /**
     * 个性化栏目--客户单位使用明细--新增图书数
     *
     * @param param 查询条件
     * @return
     */
    int getPrivateColumnAddBookTotalByColumn(Map<String,Object> param);

    /**
     * 个性化栏目--客户单位使用明细--新增图书趋势图
     *
     * @param param 查询条件
     * @return
     */
    List<Map<String,Object>> getPrivateColumnUseDetailBookTrend(Map<String,Object> param);
}
