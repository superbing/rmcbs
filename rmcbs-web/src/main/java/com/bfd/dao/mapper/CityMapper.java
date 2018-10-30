package com.bfd.dao.mapper;

import com.bfd.bean.ColumnBean;
import com.bfd.bean.PublicTemplateBean;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.CityVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *  栏目 mapper
 *
 * @author jiang.liu
 * @date 2018-07-27
 */
@Repository
public interface CityMapper {

    /**
     * 获取省市列表
     * @return
     */
    List<CityVO> getCityTree();



}


