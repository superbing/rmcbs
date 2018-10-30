package com.bfd.dao.mapper;


import com.bfd.bean.ApiInfoBean;
import com.bfd.param.vo.ApiInfoVO;
import com.bfd.param.vo.ApiTreeVO;
import com.bfd.param.vo.ApiVo;
import com.bfd.param.vo.ResourceApiQO;
import com.bfd.param.vo.edgesighvo.ApiSightVO;
import com.bfd.param.vo.edgesighvo.CompanySightDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/8/2 15:03
 * @Description:
 */
@Repository
public interface ApiInfoMapper {

    /**
     * 新增
     * @param apiInfoBean
     * @return
     */
    int insert(ApiInfoBean apiInfoBean);

    /**
     * 更新
     * @param apiInfoBean
     * @return
     */
    int update(ApiInfoBean apiInfoBean);

    /**
     * 修改api状态
     *
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(@Param(value = "id") long id,@Param(value = "status") int status);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 查询单条
     * @param id
     * @return
     */
    ApiInfoBean findById(Long id);

    /**
     * 判断接口地址是否重复
     * @param url
     * @return
     */
    ApiInfoBean findByUrl(@Param(value = "url") String url);

    /**
     * 判断接口名称是否重复
     * @param name
     * @return
     */
    ApiInfoBean findByName(@Param(value = "name") String name);

    /**
     * 查询列表
     * @param apiInfoVO
     * @return
     */
    List<ApiInfoBean> queryList(ApiInfoVO apiInfoVO);

    /**
     * 获取Api总数
     * @return
     */
    long getApiTotal();


    /**
     * 查询所有接口的名称
     * @param ids 客户单位所选择的接口
     * @return
     */
    List<ApiTreeVO> getApiTree(@Param(value = "ids") List<Long> ids);

    /**
     * 查询出所有api接口
     * @param resourceApiQO
     * @return
     */
    List<ApiVo> getApiList(ResourceApiQO resourceApiQO);


    /**
     * 根据ID获取平台商选择的所有接口
     * @param id
     * @return
     */
    List<ApiVo> getApiById(@Param(value = "id") long id);

    /**
     * 获取所有的可用的API接口
     * @param status
     * @return
     */
    List<ApiInfoBean> queryApi(@Param(value = "status")Integer status);

    /**
     * 查询全部的客户单位
     * @return
     */
    List<ApiInfoBean> queryAllApi();

    /**
     * 根据apiId的集合获取信息
     * @param urls
     * @return
     */
    List<ApiSightVO> getApiByUrls(@Param(value = "urls") List<String> urls);


    /**
     * 根据接口类型获取接口名称
     * @return
     */
    List<ApiInfoBean> getApiByType(long type);

    /**
     * 根据接口ID获取接口URl和类型URl
     * @param apiId
     * @return
     */
    CompanySightDTO getApiTypeUrl(@Param(value = "apiId")long apiId);
}
