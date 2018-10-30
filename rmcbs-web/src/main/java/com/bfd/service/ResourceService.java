package com.bfd.service;

import com.bfd.param.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description: 资源授权管理 Service
 * @Date: Created in 11:37 2018/8/3
 * @Modified by:
 */
public interface ResourceService {


    /**
     * 查看客户单位是否选择
     * @param id
     * @return
     */
    List<ResourceRecordVO> getCustomerRecord(Long id);

    /**
     * 获取接口列表
     * @param resourceApiQO
     * @return
     */
    List<ApiVo> getApiInfoList(ResourceApiQO resourceApiQO);

    /**
     * 根据ID获取平台商选择的所有接口
     * @param id
     * @return
     */
    List<ApiVo> getApiById(Long id);

    /**
     * 批量添加接口
     * @param ids
     * @return
     */
    int AddApi(long businessId, List<Long> ids);


    /**
     * 批量添加栏目
     * @param ids
     * @return
     */
    int AddColumn(long businessId, List<Long> ids);

    /**
     * 批量添加资源
     * @param resourceVO
     * @return
     */
    void addResource(ResourceVO resourceVO);

    /**
     * 获取用户单位的栏目树
     * @param businessId
     * @return
     */
    List<ColumnVO> getColumnList(Long businessId);

    /**
     * 展示客户单位栏目分级树结构
     * @param businessId
     * @return
     */
    List<ColumnVO> showColumnList(Long businessId);

    /**
     * 批量添加数据包
     * @param ids
     * @return
     */
    int AddPackage(long businessId, List<Long> ids);

    /**
     * 展示客户单位数据包分级树结构
     * @param businessId
     * @return
     */
    List<DataPackageVO> showPackageList(Long businessId);

    /**
     * 获取用户单位的数据包树
     * @param businessId
     * @return
     */
    List<DataPackageVO> getPackageList(Long businessId);


    /**
     * 根据平台商ID和接口ID删除对应关系
     * @param id
     * @param apiId
     * @return
     */
    boolean deleteOne(Long id,Long apiId);

    /**
     * 更新权限缓存
     * @param businessId
     */
    void cacheAuth(Long businessId);
}
