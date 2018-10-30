package com.bfd.bean.xml;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "content_index", type = "content_type")
public class XmlContent {
    
    private String bookId;
    
    private int pageNum;
    
    private int pdfPageNum;
    
    private String pageContent;
}
