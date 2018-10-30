package com.bfd.enums;

/**
 * @author: bing.shen
 * @date: 2018/8/7 15:31
 * @Description:上传文档类型
 */
public enum FileTypeEnum {
    
    PDF(".pdf", "pdf文档"), 
    EPUB(".epub", "epub文档"),
    XML(".xml", "xnml文档");
    
    private String key;
    
    private String desc;
    
    FileTypeEnum(String key, String desc) {
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
