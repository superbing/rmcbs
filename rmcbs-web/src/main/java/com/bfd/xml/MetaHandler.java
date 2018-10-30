package com.bfd.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.bfd.bean.MetadataBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.dao.mapper.MetadataMapper;

@Slf4j
public class MetaHandler extends DefaultHandler {
    
    // 元数据
    private MetadataBean metadata;
    
    // 当前xml元素
    private String currentTag;
    
    // 元素的文本值
    private String currentValue;
    
    private String nodeName;
    
    private int count;
    
    private MetadataMapper metadataMapper;
    
    public MetaHandler(String nodeName, MetadataMapper metadataMapper) {
        this.nodeName = nodeName;
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
            metadata = new MetadataBean();
            // metadata.setBookEpub(EpubStatusEnum.NOT_UPLOAD.getKey());
            // metadata.setBookPdf(PdfStatusEnum.NOT_UPLOAD.getKey());
            // metadata.setBookXml("0");
            // metadata.setBookXmlChapter("0");
            
            // 以下均为不能为null的字段，由于元数据XML不一定缺少那个字段，所以设置为空字符串，防止入库时为null
            metadata.setUniqueId("");
            metadata.setBookName("");
            metadata.setAuthor("");
            metadata.setResponsible("");
            metadata.setPress("");
            metadata.setPublishPlace("");
            // metadata.setPublishPlace("");
            metadata.setLanguage("");
            metadata.setBookIsbn("");
            metadata.setKeywords("");
            metadata.setContentSummary("");
            metadata.setBookMaker("");
            
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
        
        if (currentTag != null && metadata != null) {
            currentValue = new String(ch, start, length);
            if (currentTag.equals("RESOURCEUNIQUEID")) {
                metadata.setUniqueId(currentValue);
            } else if (currentTag.equals("TITLE")) {
                metadata.setBookName(currentValue);
            } else if (currentTag.equals("CREATOR")) {
                metadata.setAuthor(currentValue);
            } else if (currentTag.equals("PUBLISHER")) {
                String press = metadata.getPress();
                if (!StringUtils.isEmpty(press)) {
                    press = press + "," + currentValue;
                } else {
                    press = currentValue;
                }
                metadata.setPress(press);
            } else if (currentTag.equals("OTHERPUBLISHER")) {
                String press = metadata.getPress();
                if (!StringUtils.isEmpty(press)) {
                    press = press + "," + currentValue;
                } else {
                    press = currentValue;
                }
                metadata.setPress(press);
            } else if (currentTag.equals("LANGUAGE")) {
                String language = "";
                if ("0".equals(currentValue)) {
                    language = "简体中文";
                } else if ("1".equals(currentValue)) {
                    language = "繁体中文";
                } else if ("2".equals(currentValue)) {
                    language = "简繁混排";
                } else if ("3".equals(currentValue)) {
                    language = "英文";
                }
                metadata.setLanguage(language);
                
            } else if (currentTag.equals("PUBDATE")) {
                log.info("=============PUBDATE========:" + currentValue);
                if (currentValue.length() >= 10) {
                    currentValue = currentValue.substring(0, 10);
                }
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdf.parse(currentValue);
                } catch (ParseException e) {
                    log.info("PUBDATE不是日期类型, 修改为1900-01-01" + currentValue);
                    currentValue = "1900-01-01";
                }
                metadata.setPublishDate(currentValue);
            } else if (currentTag.equals("ISBN")) {
                metadata.setBookIsbn(currentValue);
            } else if (currentTag.equals("KEYWORDS")) {
                metadata.setKeywords(currentValue);
            } else if (currentTag.equals("DESCRIPTION")) {
                metadata.setContentSummary(currentValue);
            }
            // 根据梓壮的文档
            else if (currentTag.equals("PRODUCTIONUNIT")) {
                metadata.setBookMaker(currentValue);
            }
            // 根据梓壮的文档
            else if (currentTag.equals("PRODUCTIONTIME")) {
                metadata.setMakeTime(currentValue);
            } else if (currentTag.equals("PUBCOUNT")) {
                metadata.setEditOrder(currentValue);
            }
            // 根据梓壮的文档
            else if (currentTag.equals("PUBCOUNTYEAR")) {
                metadata.setEditOrderYear(currentValue);
            }
            // 根据梓壮的文档
            else if (currentTag.equals("PUBCOUNTMONTH")) {
                metadata.setEditOrderMonth(currentValue);
            } else if (currentTag.equals("PRINTCOUNT")) {
                metadata.setPrintOrder(currentValue);
            } else if (currentTag.equals("PRINTCOUNTYEAR")) {
                metadata.setPrintOrderYear(currentValue);
            } else if (currentTag.equals("PRINTCOUNTMONTH")) {
                metadata.setPrintOrderMonth(currentValue);
            } else if (currentTag.equals("FORMAT")) {
                metadata.setBookSize(currentValue);
            } else if (currentTag.equals("FORMATSIZE1")) {
                metadata.setBookSizeOne(currentValue);
            } else if (currentTag.equals("FORMATSIZE2")) {
                metadata.setBookSizeTwo(currentValue);
            } else if (currentTag.equals("WORDCOUNT")) {
                metadata.setWordCount(currentValue);
            } else if (currentTag.equals("PRINTFLOAT")) {
                metadata.setPrintSheet(currentValue);
            } else if (currentTag.equals("FRAMETYPE")) {
                metadata.setBindingType(currentValue);
            } else if (currentTag.equals("PAGECOUNT")) {
                metadata.setPagesNumber(currentValue);
            }
            // 根据梓壮的文档
            else if (currentTag.equals("DUTYEDITOR")) {
                metadata.setEditor(currentValue);
            } else if (currentTag.equals("PRICE")) {
                metadata.setPrice(currentValue);
            } else if (currentTag.equals("TYPE")) {
                metadata.setBookCategory(currentValue);
            } else if (currentTag.equals("AUTHORINTRODUCEEN")) {
                metadata.setAuthorBrief(currentValue);
            } else if (currentTag.equals("WORDS")) {
                metadata.setCitation(currentValue);
            } else if (currentTag.equals("ISOVERSEAS")) {
                metadata.setImportVersion(currentValue);
            } else if (currentTag.equals("SOURCELANGUAGE")) {
                metadata.setOriginLanguage(currentValue);
            } else if (currentTag.equals("ORIGINALTITLE")) {
                metadata.setOriginBookName(currentValue);
            } else if (currentTag.equals("TRANSLATOR")) {
                metadata.setTranslator(currentValue);
            } else if (currentTag.equals("COVER")) {
                metadata.setCover(currentValue);
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
            log.info("metadata:" + JSONObject.toJSONString(metadata));
            
            // 没有封面字段不导入
            if (!StringUtils.isEmpty(metadata.getCover())) {
                MetadataBean metadataBean = metadataMapper.getMetaByUniqueId(metadata.getUniqueId());
                if (metadataBean != null) {
                    log.info(String.format("书籍：%s已存在！共导入%s本书", metadata.getUniqueId(), count));
                    throw new RmcbsException("书籍：" + metadata.getUniqueId() + "已存在！");
                }
                metadataMapper.addMetadata(metadata);
                count++;
            }
        }
    }
    
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        log.info("元数据XML内容结束, 上传元数据XML结束!");
    }
    
}
