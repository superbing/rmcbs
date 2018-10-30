package com.bfd.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bfd.bean.CompanyBean;
import com.bfd.bean.MetadataBean;
import com.bfd.common.vo.DrmConstants;
import com.bfd.common.vo.MetadataConstants;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.dao.mapper.MetadataMapper;
import com.bfd.service.DrmEncryptService;

/**
 * DRM加解密
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年8月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
@Slf4j
public class DrmEncryptServiceImpl implements DrmEncryptService {
    
    @Autowired
    MetadataMapper metadataMapper;
    
    @Autowired
    CompanyMapper companyMapper;
    
    @Override
    @Transactional
    public void onlineResult(String resultJson) {
        JSONObject resultObj = JSONObject.parseObject(resultJson);
        String encryptResultStr = resultObj.getString(DrmConstants.ENCRYPT_RESULT);
        JSONArray docIdArray = resultObj.getJSONArray(DrmConstants.DOC_IDS);
        
        if (HttpStatus.OK.getReasonPhrase().equals(encryptResultStr)) {
            if (docIdArray.size() >= 1) {
                for (int i = 0; i < docIdArray.size(); i++) {
                    MetadataBean metadata = new MetadataBean();
                    metadata.setUniqueId(docIdArray.getString(i));
                    metadata.setBookEpub(MetadataConstants.ONLINE_EPUB_ENCRYPT_OVER);
                    metadataMapper.updateMetadata(metadata);
                }
            } else {
                throw new RuntimeException("docIds不能为空！");
            }
        } else {
            log.info("在线加密结果不确定");
            // throw new RuntimeException("encryptResult不为OK!");
        }
    }
    
    @Override
    public void offlineResult(String resultJson) {
        JSONObject resultObj = JSONObject.parseObject(resultJson);
        String encryptResultStr = resultObj.getString(DrmConstants.ENCRYPT_RESULT);
        String accessKeyStr = resultObj.getString(DrmConstants.ACCESS_KEY);
        
        if (HttpStatus.OK.getReasonPhrase().equals(encryptResultStr)) {
            if (!StringUtils.isEmpty(accessKeyStr)) {
                CompanyBean company = new CompanyBean();
                company.setAccessKey(accessKeyStr);
                company.setDrmEncrypt(MetadataConstants.OFFLINE_EPUB_ENCRYPT_OVER);
                companyMapper.updateCompanyByAccessKey(company);
            } else {
                throw new RuntimeException("accessKey不能为空！");
            }
        } else {
            log.info("离线加密结果不确定");
            // throw new RuntimeException("encryptResult不为OK!");
        }
    }
    
}
