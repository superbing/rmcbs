package com.bfd.enums;

/**
 * EpubStatusEnum
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年8月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum EpubStatusEnum {
    
    // 0:未上传；1:已上传；2:drm加密中；3:加密完成
    NOT_UPLOAD("0", "未上传"), 
    UPLOADED("1", "已上传"), 
    ENCRYPTING("2", "加密中"),
    ENCRYPTED("3", "加密完成");
    
    private String key;
    
    private String desc;
    
    EpubStatusEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getDesc() {
        return desc;
    }
}
