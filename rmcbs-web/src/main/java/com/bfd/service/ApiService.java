package com.bfd.service;

import java.util.List;

import com.bfd.bean.ApiInfoBean;
import com.bfd.bean.ApiTypeBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.ApiInfoVO;
import com.bfd.param.vo.ApiTreeVO;
import com.bfd.param.vo.ApiVo;

/**
 * @author: bing.shen
 * @date: 2018/8/2 15:34
 * @Description:
 */
public interface ApiService {
    
    /**
     * 新增API
     * 
     * @param apiInfoBean
     * @return
     */
    boolean addApi(ApiInfoBean apiInfoBean);

    /**
     * 查询所有接口树
     * @param id 客户单位ID
     * @return
     */
    List<ApiTreeVO> getApiTree(long id);
    
    /**
     * 获取API信息
     * 
     * @param id
     * @return
     */
    ApiInfoBean getApiInfo(Long id);

    /**
     * api文档的获取API信息(url为全地址)
     * @param id
     * @return
     */
    ApiInfoBean getApiDocument(Long id);
    
    /**
     * 修改
     * 
     * @param apiInfoBean
     * @return
     */
    boolean updateApi(ApiInfoBean apiInfoBean);

    /**
     * 修改api状态
     *
     * @param id
     * @param status
     * @return
     */
    boolean updateStatus(long id,int status);
    
    /**
     * 分页
     * 
     * @param apiInfoVO
     * @return
     */
    PageVO<ApiInfoBean> apiInfoPage(ApiInfoVO apiInfoVO);

    /**
     * 平台商新增编辑接口时展示
     * @param apiInfoVO
     * @return
     */
    PageVO<ApiVo> getApiList(ApiInfoVO apiInfoVO);
    
    /**
     * 删除API
     * 
     * @param id
     * @return
     */
    boolean deleteApi(Long id);
    
    /**
     * 查询状态可用的api类型
     * 
     * @param status
     * @return
     * @see [类、类#方法、类#成员]
     */
    List<ApiTypeBean> queryApiType(Integer status);

    /**
     * 根据状态获取Api
     * @return
     */
    List<ApiInfoBean> queryApi();

    /**
     * 根据接口类型获取接口名称
     * @return
     */
    List<ApiInfoBean> getApiByType(long type);
    
}
