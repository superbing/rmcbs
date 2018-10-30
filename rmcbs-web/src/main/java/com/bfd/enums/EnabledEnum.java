package com.bfd.enums;

import com.bfd.bean.DictBean;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author: bing.shen
 * @Date: 2018/7/30 17:46
 * @Description:
 */
public enum EnabledEnum {

    //不可用
    CLOSE(0,"不可用"),
    //可用
    OPEN(1,"可用");

    private Integer key;
    private String desc;

    EnabledEnum(Integer key,String desc){
        this.key=key;
        this.desc=desc;
    }

    public Integer getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static List<DictBean> getList(){
        List<DictBean> list = Lists.newArrayList();
        for (EnabledEnum s : EnabledEnum.values()){
            DictBean dictBean = new DictBean();
            dictBean.setCode(s.getKey());
            dictBean.setName(s.getDesc());
            dictBean.setType("enable");
            list.add(dictBean);
        }
        return list;
    }
}
