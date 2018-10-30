package com.bfd.service;



import com.bfd.bean.ApiInfoBean;
import com.bfd.param.vo.ApiTreeVO;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/8/2 15:34
 * @Description:
 */
public interface ApiService {
    


    /**
     * 查询所有接口树
     * @param id 客户单位ID
     * @return
     */
    List<ApiTreeVO> getApiTree(long id);


    /**
     * api文档的获取API信息(url为全地址)
     * @param id
     * @return
     */
    ApiInfoBean getApiDocument(Long id);

    
}
