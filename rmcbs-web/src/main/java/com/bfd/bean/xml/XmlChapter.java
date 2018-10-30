package com.bfd.bean.xml;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "chapter_index", type = "chapter_type")
public class XmlChapter {
    
    private String bookId;
    
    private String title;
    
    private int level;
    
    private int pageNum;
    
    private int pageNumEnd;
    
    private int pdfPageNum;
    
    private int pdfPageNumEnd;
    
}
