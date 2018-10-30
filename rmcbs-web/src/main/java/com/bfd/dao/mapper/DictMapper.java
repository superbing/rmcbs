package com.bfd.dao.mapper;

import com.bfd.bean.DictBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 11:18 2018/8/7
 * @Modified by:
 */
@Repository
public interface DictMapper {


    /**
     * 插入字典类型
     * @param dictBeans
     * @return
     */
    int insert(@Param(value = "dicts") List<DictBean> dictBeans);

    /**
     * 获取字典
     * @param type
     * @return
     */
    List<DictBean> getDict(@Param(value = "type") String type);

    /**
     * 获取图书作者列表
     *
     * @return
     */
    List<String> getAuthorList();
}
