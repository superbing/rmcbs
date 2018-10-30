package com.bfd.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: bing.shen
 * @date: 2018/8/24 14:49
 * @Description:
 */
@Data
public class ApiBean implements Serializable {

    private static final long serialVersionUID = 8779100860711443301L;

    private Long apiId;

    private String url;

    private Long apiType;

    private Long businessId;

    private Long companyId;
    
}
