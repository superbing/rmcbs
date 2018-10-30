package com.bfd.enums;

import com.bfd.bean.DictBean;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author: bing.shen
 * @Date: 2018/7/30 17:46
 * @Description: 置灰枚举
 */
public enum SetAshEnum {

    //不可用
    CLOSE(0,"不置灰"),
    //可用
    OPEN(1,"置灰");

    private Integer key;
    private String desc;

    SetAshEnum(Integer key, String desc){
        this.key=key;
        this.desc=desc;
    }

    public Integer getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

}
