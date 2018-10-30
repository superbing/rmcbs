package com.bfd.bean;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Document(indexName = "pdf_chapter_index", type = "chapter_type")
public class XmlChapter implements Serializable {

    private static final long serialVersionUID = -4254967565082605507L;

    private String bookId;
    
    private String title;
    
    private int level;
    
    private int pageNum;
    
}
