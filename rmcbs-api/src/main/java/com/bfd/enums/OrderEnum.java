package com.bfd.enums;

/**
 * @author: bing.shen
 * @date: 2018/9/13 10:09
 * @Description:
 */
public enum OrderEnum {

    ASC("asc", "升序"),

    DESC("desc", "降序");

    OrderEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    private String key;

    private String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
