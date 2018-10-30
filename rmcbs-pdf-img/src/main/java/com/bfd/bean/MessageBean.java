package com.bfd.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: bing.shen
 * @date: 2018/9/4 15:58
 * @Description:
 */
@Data
public class MessageBean implements Serializable {

    private static final long serialVersionUID = -3306929704573296065L;

    private String fileDir;

    private String fileName;

    private String bookId;

}
