package com.bfd.enums;

/**
 * @author: bing.shen
 * @date: 2018/8/9 16:58
 * @Description:
 */
public enum BookTypeEnum {

    /**
     * COLUMN
     */
    COLUMN(1, "公共栏目"),
    /**
     * COLUMN_PRIVATE
     */
    PRIVATE_COLUMN(2, "个性化栏目"),
    /**
     * PACKAGE
     */
    PACKAGE(3, "数据包"),
    /**
     * PACKAGE_PRIVATE
     */
    PRIVATE_PACKAGE(4, "个性化数据包");


    private Integer key;
    private String desc;

    BookTypeEnum(Integer key, String desc) {
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
