package com.bfd.enums;


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

    EnabledEnum(Integer key, String desc){
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
