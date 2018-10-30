package com.bfd.param.vo.edgesighvo.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 10:31 2018/8/31
 * @Modified by:
 */
@Document(indexName = "rms-log-index",type = "log_type")
@Data
public class DayCountLogVO {

    private String id;

    private long businessId;

    private String accessKey;

    private String companyCode;

    private String companyName;

    private String addTime;

    private String ip;

    private String message;

    private long status;

}
