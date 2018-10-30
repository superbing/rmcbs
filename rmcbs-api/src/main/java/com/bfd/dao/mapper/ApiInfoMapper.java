package com.bfd.dao.mapper;


import com.bfd.bean.ApiInfoBean;
import com.bfd.param.vo.ApiTreeVO;
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
     * 查询单条
     * @param id
     * @return
     */
    ApiInfoBean findById(Long id);

    /**
     * 查询所有接口的名称
     * @param ids 客户单位所选择的接口
     * @return
     */
    List<ApiTreeVO> getApiTree(@Param(value = "ids") List<Long> ids);


}
