package com.bfd.controller;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/estest", produces = {"application/json;charset=UTF-8"})
@Api(value = "测试ES", consumes = "application/x-www-form-urlencoded", description = "ES")
public class TestEsController {
    
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    
    @PostMapping(value = "/test")
    @ApiOperation(value = "添加元数据")
    public Object testEs() {
        TermQueryBuilder termQuery = QueryBuilders.termQuery("apiId", 0L);
        RangeQueryBuilder range = QueryBuilders.rangeQuery("addTime").gte("2018-08-31 00:00:00").lte("2018-08-31 23:59:59");
        BoolQueryBuilder query = QueryBuilders.boolQuery().filter(termQuery).filter(range);
        
        System.out.println(String.format("{query:%s}", query));
        
        SearchQuery searchQuery = new NativeSearchQueryBuilder()//
            .withIndices("rms-log-index")//
            .withTypes("log_type")//
            .withPageable(PageRequest.of(0, 10))//
            .withQuery(query)//
            .build();
        
        Page<JSONObject> result = elasticsearchTemplate.queryForPage(searchQuery, JSONObject.class);
        
        List<JSONObject> content = result.getContent();
        for (JSONObject json : content) {
            System.out.println(json);
        }
        
        return content;
        
    }
    
}
