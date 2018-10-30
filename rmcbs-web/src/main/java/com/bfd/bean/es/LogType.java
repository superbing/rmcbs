package com.bfd.bean.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 15:31 2018/8/30
 * @Modified by:
 */
@Document(indexName = "rms-log-index",type = "log_type")
@Data
public class LogType{

    @Id
    private Long id;

    private String companyCode;

    private String addTime;

    private String ip;

    private Long businessId;

    private String message;

    private String url;

    private Long companyId;

    private String accessKey;

    private Long useTime;

    private Long apiId;

    private Long apiType;

    private Long status;

    private String timeStamp;

    private String addYear;

    private String addMonth;

    private String addDay;

    private String addHour;

    private String addMinute;

    private String addSecond;
}
