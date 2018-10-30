package com.bfd.enums;

import com.bfd.bean.DictBean;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author: chong.chen
 * @Description: 客户单位类型
 * @Date: Created in 11:44 2018/7/31
 * @Modified by:
 */
public enum CompanyTypeEnum {

    /**
     * 国有企业
     */
    PUBLICZNO(0,"国有企业"),
    /**
     * 国有控股企业
     */
    STATE_HOLDING(1,"国有控股企业"),

    /**
     * 外资企业
     */
    FOREIGN(2,"外资企业"),
    /**
     * 合资企业
     */
    JOINT(3,"合资企业"),
    /**
     * 私营企业
     */
    PRIVATE(4,"私营企业"),
    /**
     *事业单位
     */
    INSTITUTIONS(5,"事业单位"),
    /**
     *国家行政机关
     */
    ADMINISTRATION(6,"国家行政机关"),
    /**
     *政府
     */
    GOVERNMENT(7,"政府");


    private Integer key;
    private String desc;

    CompanyTypeEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public Integer getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<Integer,String> companyTypeMap() {
        Map<Integer,String> companyTypeMap = Maps.newHashMap();
        for (CompanyTypeEnum s : CompanyTypeEnum.values()){
            companyTypeMap.put(s.getKey(),s.getDesc());
        }
        return companyTypeMap;
    }

    public static List<DictBean> getList(){
        List<DictBean> list = Lists.newArrayList();
        for (CompanyTypeEnum s : CompanyTypeEnum.values()){
            DictBean dictBean = new DictBean();
            dictBean.setCode(s.getKey());
            dictBean.setName(s.getDesc());
            dictBean.setType("company_type");
            list.add(dictBean);
        }
        return list;
    }
}
