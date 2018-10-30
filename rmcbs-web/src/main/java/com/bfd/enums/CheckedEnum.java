package com.bfd.enums;

/**
 * @author: bing.shen
 * @Date: 2018/7/30 17:46
 * @Description: 置灰枚举
 */
public enum CheckedEnum {

    //不可用
    CLOSE(0,"不选择"),
    //可用
    OPEN(1,"选中");

    private Integer key;
    private String desc;

    CheckedEnum(Integer key, String desc){
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
