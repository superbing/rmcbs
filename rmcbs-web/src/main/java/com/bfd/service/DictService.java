package com.bfd.service;

import com.bfd.bean.DictBean;
import com.bfd.param.vo.CityVO;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description: 字典service
 * @Date: Created in 11:23 2018/8/7
 * @Modified by:
 */

public interface DictService {

    /**
     * 插入字典类型
     * @param dictBeans
     * @return
     */
    int insert(List<DictBean> dictBeans);

    /**
     * 获取字典
     * @param type
     * @return
     */
    List<DictBean> getDict(String type);

    /**
     * 获取图书作者列表
     *
     * @return
     */
    List<String> getAuthorList();

    /**
     * 获取省市列表
     * @return
     */
    List<CityVO> getCityTree();
}
