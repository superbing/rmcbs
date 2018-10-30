package com.bfd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bfd.dao.mapper.BookMapper;
import com.bfd.service.CheckDrmBookService;
import com.bfd.utils.DeEnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @date:
 * @Description:
 */
@Service
@Slf4j
public class CheckDrmBookServiceImpl implements CheckDrmBookService {
    
    @Autowired
    private BookMapper bookMapper;
    
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    
    @Value("${es.rms_device_info.indexName}")
    private String indexName;
    
    @Value("${es.rms_device_info.indexType}")
    private String indexType;
    
    @Value("${epub.key.dir}")
    private String keyDir;
    
    @Override
    public Map<String, Object> checkDrmBook(String bookId, Long businessId, String token) {
        Map<String, Object> map = new HashMap<>(16);
        // 验证成功，组织返回结果
        JSONObject object = new JSONObject();
        // 读取key文件
        File parent = new File(keyDir, "CP" + bookId);
        File keyFile = new File(parent, "key");
        
        BufferedReader reader = null;
        String temp;
        StringBuffer buff = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(keyFile));
            while ((temp = reader.readLine()) != null) {
                buff.append(temp).append("@");
            }
        } catch (Exception e) {
            throw new RuntimeException("读取文件失败");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String key = buff.toString().substring(0, buff.toString().length() - 1);
        Map<String, Object> businessTermOfValidity = bookMapper.getBusinessTermOfValidity(businessId);
        long endTime = ((Date)businessTermOfValidity.get("end_time")).getTime()/1000;
        object.put("key", key);
        object.put("message", "验证通过!");
        object.put("validate", true);
        object.put("timestamp", endTime);
        log.info("加密的json串： " + object.toJSONString());
        
        String enc = DeEnCode.encode(object.toString(), token);
        map.put("code", HttpStatus.OK.value());
        map.put("message", "接口验证成功！");
        map.put("encryptResult", enc);
        log.info("返回结果： " + map);
        return map;
        
    }
    
}
