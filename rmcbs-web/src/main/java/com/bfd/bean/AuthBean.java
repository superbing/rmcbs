package com.bfd.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: bing.shen
 * @date: 2018/9/25 10:48
 * @Description:
 */
@Data
public class AuthBean implements Serializable {

    private static final long serialVersionUID = 3086176445581767864L;

    private Long apiId;

    private String url;

    private Long apiType;

    private Long businessId;
}
