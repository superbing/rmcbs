package com.bfd.enums;

/**
 * @author: bing.shen
 * @date: 2018/8/10 15:14
 * @Description:PDF上传状态
 */
public enum PdfStatusEnum {

    NOT_UPLOAD("0", "未上传"),
    UPLOAD("1", "已上传"),
    UPLOADING("2", "上传中");

    private String key;
    private String desc;

    PdfStatusEnum(String key, String desc) {
        this.key=key;
        this.desc=desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
