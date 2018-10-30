package com.bfd.xml;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.alibaba.fastjson.JSONObject;
import com.bfd.bean.MetadataBean;
import com.bfd.bean.xml.XmlChapter;
import com.bfd.common.vo.MetadataConstants;
import com.bfd.dao.mapper.MetadataMapper;
import com.bfd.utils.EsUtils;

/**
 * XML处理目录
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年8月10日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class ChapterHandler extends DefaultHandler {
    
    private static final String PDF_PAGE_NUM_END = "PDFPageNumEnd";
    
    private static final String PDF_PAGE_NUM = "PDFPageNum";
    
    private static final String PAGE_NUM_END = "PageNumEnd";
    
    private static final String PAGE_NUM = "PageNum";
    
    private static final String LEVEL = "Level";
    
    private static final String TITLE = "Title";
    
    // 目录实例
    private XmlChapter chapter;
    
    // 当前xml元素
    private String currentTag;
    
    // 元素的文本值
    private String currentValue;
    
    private String nodeName;
    
    // 数的唯一id
    private String bookId;
    
    // 索引
    private String indexName;
    
    // 索引类型
    private String indexType;
    
    private BulkProcessor bulkProcessor = null;
    
    private Client client;
    
    private MetadataMapper metadataMapper;
    
    public ChapterHandler(String nodeName, String bookId, Client client, String indexName, String indexType, MetadataMapper metadataMapper) {
        this.nodeName = nodeName;
        this.bookId = bookId;
        this.client = client;
        this.indexName = indexName;
        this.indexType = indexType;
        this.metadataMapper = metadataMapper;
    }
    
    /**
     * 当读到一个开始标签的时候，会触发这个方法
     * 
     * @throws SAXException
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        bulkProcessor = EsUtils.bulkProcessor(this.client);
        log.info(String.format("开始解析%s的xml章节,并添加索引", bookId));
    }
    
    /**
     * 当遇到文档的开头的时候，调用这个方法
     * 
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        
        if (qName.equals(nodeName)) {
            chapter = new XmlChapter();
            chapter.setBookId(bookId);
        }
        currentTag = qName;
    }
    
    /**
     * 用来处理在XML文件中读到的内容
     * 
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        
        if (currentTag != null && chapter != null) {
            currentValue = new String(ch, start, length);
            if (currentValue != null && !currentValue.trim().equals("") && !currentValue.trim().equals("\n")) {
                if (currentTag.equals(TITLE)) {
                    chapter.setTitle(currentValue);
                } else if (currentTag.equals(LEVEL)) {
                    chapter.setLevel(Integer.parseInt(currentValue));
                } else if (currentTag.equals(PAGE_NUM)) {
                    chapter.setPageNum(Integer.parseInt(currentValue));
                } else if (currentTag.equals(PAGE_NUM_END)) {
                    chapter.setPageNumEnd(Integer.parseInt(currentValue));
                } else if (currentTag.equals(PDF_PAGE_NUM)) {
                    chapter.setPdfPageNum(Integer.parseInt(currentValue));
                } else if (currentTag.equals(PDF_PAGE_NUM_END)) {
                    chapter.setPdfPageNumEnd(Integer.parseInt(currentValue));
                }
            }
        }
        currentTag = null;
        currentValue = null;
    }
    
    /**
     * 在遇到结束标签的时候，调用这个方法
     * 
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals(nodeName)) {
            IndexRequest indexRequest = new IndexRequest(indexName, indexType);
            this.bulkProcessor.add(indexRequest.source(JSONObject.toJSONString(chapter)));
        }
    }
    
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        
        MetadataBean metadata = new MetadataBean();
        metadata.setUniqueId(this.bookId);
        metadata.setBookXmlChapter(MetadataConstants.XML_CHAPTER_UPLOADED);
        metadataMapper.updateMetadata(metadata);
        log.info("解析XML章节结束，上传XML章节结束！");
    }
}
