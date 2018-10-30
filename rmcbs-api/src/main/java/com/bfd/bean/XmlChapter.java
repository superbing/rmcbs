package com.bfd.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class XmlChapter implements Serializable {

    private static final long serialVersionUID = -4254967565082605507L;

    private String bookId;
    
    private String title;
    
    private int level;
    
    private int pageNum;
    
}
