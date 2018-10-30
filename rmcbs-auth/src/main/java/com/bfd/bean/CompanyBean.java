package com.bfd.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: bing.shen
 * @date: 2018/8/24 11:02
 * @Description:
 */
@Data
public class CompanyBean implements Serializable {

    private Long businessId;

    private String companyCode;

    private Long companyId;

    private String accessKey;

    private Integer status;

    private Date startTime;

    private Date endTime;
}
