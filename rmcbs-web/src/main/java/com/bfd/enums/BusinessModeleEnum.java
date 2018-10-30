package com.bfd.enums;

import com.bfd.bean.DictBean;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author: chong.chen
 * @Description:   商业模式
 * @Date: Created in 10:19 2018/7/31
 * @Modified by:
 */
public enum BusinessModeleEnum {

    /**
     *商家/企业与商家/企业的网络交易
     */
    B2B(0,"B2B"),
    /**
     *商家企业与消费者交易
     */
    B2C(1,"B2C"),
    /**
     * 个体户与消费者的交易
     */
    C2C(2,"C2C");

    private Integer key;
    private String desc;

    BusinessModeleEnum(Integer key,String desc) {
        this.key=key;
        this.desc=desc;
    }

    public Integer getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<Integer,String> bsinessModeleMap() {
        Map<Integer,String> bsinessModeleMap = Maps.newHashMap();
        for (BusinessModeleEnum s : BusinessModeleEnum.values()){
            bsinessModeleMap.put(s.getKey(),s.getDesc());
        }
        return bsinessModeleMap;
    }

    public static List<DictBean> getList(){
        List<DictBean> list = Lists.newArrayList();
        for (BusinessModeleEnum s : BusinessModeleEnum.values()){
            DictBean dictBean = new DictBean();
            dictBean.setCode(s.getKey());
            dictBean.setName(s.getDesc());
            dictBean.setType("model");
            list.add(dictBean);
        }
        return list;
    }
}
