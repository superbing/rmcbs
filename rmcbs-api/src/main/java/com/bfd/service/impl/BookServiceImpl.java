package com.bfd.service.impl;

import com.bfd.bean.BookBean;
import com.bfd.bean.XmlChapter;
import com.bfd.bean.XmlContent;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.BookMapper;
import com.bfd.service.BookService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/8/22 17:00
 * @Description:
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private JestClient jestClient;

    @Value("${xml.chapter.indexName}")
    private String chapterIndexName;

    @Value("${xml.chapter.indexType}")
    private String chapterIndexType;

    @Value("${xml.content.indexName}")
    private String contentIndexName;

    @Value("${xml.content.indexType}")
    private String contentIndexType;

    @Override
    public BookBean getBookById(String bookId, Long businessId) {
        if(authentication(bookId, businessId)){
            return bookMapper.getBookById(bookId);
        }else {
            throw new RuntimeException("无权访问此图书");
        }
    }

    @Override
    public Boolean authentication(String bookId, Long businessId) {
        //如果查出有值即有权限
        Boolean flag = false;
        Long num = bookMapper.getPrivateCount(bookId, businessId);
        if(num!=null && num>0L){
            flag = true;
        }else{
            num = bookMapper.getPublicCount(bookId, businessId);
            if(num!=null && num>0L){
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public List<XmlChapter> queryChapter(String bookId, Long businessId) throws Exception{
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("bookId",bookId));
        searchSourceBuilder.size(10000);
        searchSourceBuilder.sort("pageNum", SortOrder.ASC);
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(chapterIndexName).addType(chapterIndexType).build();
        JestResult result = jestClient.execute(search);
        return result.getSourceAsObjectList(XmlChapter.class);
    }

    @Override
    public PageVO<XmlContent> queryContent(int pageNum, int pageSize, String searchContent, String bookId, Long businessId) throws Exception{
        String highLightField = "pageContent";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort("pageNum", SortOrder.ASC);
        searchSourceBuilder.from((pageNum-1)*pageSize);
        searchSourceBuilder.size(pageSize);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder
                .must(QueryBuilders.termQuery("bookId", bookId))
                .must(QueryBuilders.matchQuery(highLightField, searchContent));
        searchSourceBuilder.query(queryBuilder);
        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //设置前缀
        highlightBuilder.preTags("<em>");
        //设置后缀
        highlightBuilder.postTags("</em>");
        //设置高亮字段
        highlightBuilder.field("pageContent");
        //设置高亮信息
        searchSourceBuilder.highlight(highlightBuilder);

        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(contentIndexName).addType(contentIndexType).build();
        JestResult result = jestClient.execute(search);
        List<SearchResult.Hit<XmlContent,Void>> hits = ((SearchResult) result).getHits(XmlContent.class);
        List<XmlContent> list = result.getSourceAsObjectList(XmlContent.class);
        ((SearchResult) result).getTotal();
        for(int i=0;i<list.size();i++){
            list.get(i).setPageContent(StringUtils.join(hits.get(i).highlight.get(highLightField),""));
        }
        return new PageVO<>(pageNum, pageSize, ((SearchResult) result).getTotal(), list);
    }


}
