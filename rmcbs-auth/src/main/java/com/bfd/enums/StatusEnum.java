package com.bfd.enums;

/**
 * @author: bing.shen
 * @date: 2018/8/24 11:24
 * @Description:
 */
public enum StatusEnum {

    /**
     * 开启
     */
    OPEN(1, "开启"),

    /**
     * 关闭
     */
    CLOSE(0, "关闭");

    private Integer key;

    private String desc;

    StatusEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public Integer getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
