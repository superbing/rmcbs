package com.bfd.dao.mapper;

import com.bfd.bean.ApiBean;
import com.bfd.bean.CompanyBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/8/13 15:27
 * @Description:
 */
public interface AuthMapper {

    /**
     * 查询客户端是否存在
     * @param clientId
     * @param secret
     * @return
     */
    CompanyBean getClient(@Param("clientId") String clientId, @Param("secret") String secret);

    /**
     * 查询该商户授权的API列表
     * @param businessId
     * @return
     */
    List<ApiBean> queryAuth(@Param("businessId") Long businessId);

    /**
     * 通过url获取API信息
     * @param url
     * @param companyCode
     * @return
     */
    ApiBean getApiInfoByUrl(@Param("url") String url, @Param("companyCode") String companyCode);
}
