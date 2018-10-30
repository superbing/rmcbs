package com.bfd.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: bing.shen
 * @date: 2018/8/24 15:22
 * @Description:
 */
@Data
public class AuthInfoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private CompanyBean companyBean;

    private JSONObject authenticatedObject;
}
