package com.bfd.bean;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Document(indexName = "pdf_content_index", type = "content_type")
public class XmlContent implements Serializable {

    private static final long serialVersionUID = 5465115169856523557L;

    private String bookId;
    
    private int pageNum;
    
    private String pageContent;
}
