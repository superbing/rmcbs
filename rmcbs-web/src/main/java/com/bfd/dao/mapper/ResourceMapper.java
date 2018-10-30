package com.bfd.dao.mapper;

import com.bfd.bean.ResourceBean;
import com.bfd.param.vo.ResourceRecordVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/7/30 14:04
 * @Description :
 */
@Repository
public interface ResourceMapper {

    List<ResourceBean> findAll();

    List<ResourceBean> findByRoleId(Long roleId);

    /**
     * 查看客户单位是否选择
     * @param id
     * @return
     */
    List<ResourceRecordVO> getCustomerRecord(@Param(value = "id") Long id);

}
