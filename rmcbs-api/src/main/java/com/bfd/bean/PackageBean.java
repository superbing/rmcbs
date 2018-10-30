package com.bfd.bean;

import com.bfd.enums.BookTypeEnum;
import lombok.Data;

/**
 * 数据包实体类
 * @author jiang.liu
 * @date 2018-7-25
 */
@Data
public class PackageBean {

    private long id;

    private String name;

    private String aliasName;

    private long parentId;

    private int sort;

    private int type = BookTypeEnum.PACKAGE.getKey();

}


