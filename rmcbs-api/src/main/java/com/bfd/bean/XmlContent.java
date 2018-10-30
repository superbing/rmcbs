package com.bfd.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class XmlContent implements Serializable {

    private static final long serialVersionUID = 5465115169856523557L;

    private String bookId;
    
    private int pageNum;
    
    private String pageContent;
}
