package com.bfd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfd.bean.DictBean;
import com.bfd.dao.mapper.CityMapper;
import com.bfd.dao.mapper.DictMapper;
import com.bfd.param.vo.CityVO;
import com.bfd.service.DictService;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 11:24 2018/8/7
 * @Modified by:
 */
@Service
public class DictServiceImpl implements DictService {

   @Autowired
   private DictMapper dictMapper;

   @Autowired
   private CityMapper cityMapper;


    @Override
    public int insert(List<DictBean> dictBeans) {
        return dictMapper.insert(dictBeans);
    }

    @Override
    public List<DictBean> getDict(String type) {
        return dictMapper.getDict(type);
    }

    @Override
    public List<String> getAuthorList() {
        return dictMapper.getAuthorList();
    }

    @Override
    public List<CityVO> getCityTree() {
        return cityMapper.getCityTree();
    }

}
