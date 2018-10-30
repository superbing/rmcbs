package com.bfd.enums;

/**
 * @author: bing.shen
 * @date: 2018/8/22 15:21
 * @Description:
 */
public enum ClientTypeEnum {
    /**
     * PC端
     */
    PC("PC", "PC端"),
    /**
     * APP端
     */
    APP("APP", "APP端");

    ClientTypeEnum(String key, String desc) {
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
