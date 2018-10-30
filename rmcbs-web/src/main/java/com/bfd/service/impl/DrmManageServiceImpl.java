package com.bfd.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.bfd.bean.BusinessUnitsBean;
import com.bfd.bean.CompanyBean;
import com.bfd.bean.MetadataBean;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.DrmConstants;
import com.bfd.common.vo.MetadataConstants;
import com.bfd.dao.mapper.BusinessUnitsMapper;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.dao.mapper.DrmManageMapper;
import com.bfd.dao.mapper.MetadataMapper;
import com.bfd.enums.EpubStatusEnum;
import com.bfd.service.DrmManageService;
import com.bfd.utils.HttpTool;

@Service
public class DrmManageServiceImpl implements DrmManageService {
    
    private static final long FIRST_LEVEL_COMPANY = 0L;
    
    @Autowired
    CompanyMapper companyMapper;
    
    @Autowired
    DrmManageMapper drmManageMapper;
    
    @Autowired
    MetadataMapper metadataMapper;
    
    @Autowired
    BusinessUnitsMapper businessUnitsMapper;
    
    @Value("${offline.epub.url}")
    private String offLineDrmUrl;
    
    @Override
    public void offLineEncrypt(Long companyId, String startDate, String endDate) {
        
        // 校验时间格式并判断时间段有效性
        this.compareDate(startDate, endDate);
        
        // 根据companyId获取客户单位
        CompanyBean companyBean = companyMapper.getCompanyById(companyId);
        
        // 判断客户单位是否为空
        if (companyBean == null) {
            throw new RuntimeException(String.format("id为%s的客户单位不存在！", companyId));
        }
        
        // 不能是一级平台商
        if (FIRST_LEVEL_COMPANY == companyBean.getParentCompanyId()) {
            throw new RuntimeException("不能对一级部门进行离线数据加密！");
        }
        
        // accessKey不能为空
        String accessKey = companyBean.getAccessKey();
        if (StringUtils.isEmpty(accessKey)) {
            throw new RuntimeException(String.format("id为%s客户单位的秘钥为空，请联系管理员！", companyId));
        }
        
        // 获取该单位的所有元数据的唯一标识列表
        List<String> uniqueIdList = drmManageMapper.getUniqueIdListByCompanyId(companyId);
        if (CollectionUtils.isEmpty(uniqueIdList)) {
            throw new RuntimeException(String.format("id为%s客户单位下没有书籍，请先为改客户添加书籍！", companyId));
        }
        
        // 根据唯一标识列表获取元数据列表，用于判断是否有未上传EPUB的元数据
        List<MetadataBean> metadataList = metadataMapper.getMetadataListByUniqueIdList(uniqueIdList);
        StringBuffer notUploadBuf = new StringBuffer();
        for (MetadataBean meta : metadataList) {
            
            // 记录未上传的EPUB
            if (EpubStatusEnum.NOT_UPLOAD.getKey().equals(meta.getBookEpub())) {
                notUploadBuf.append(meta.getUniqueId()).append(",");
            }
        }
        String notUploadString = notUploadBuf.toString();
        if (!StringUtils.isEmpty(notUploadString)) {
            String uniqueIds = notUploadString.substring(0, notUploadString.lastIndexOf(","));
            throw new RuntimeException(String.format("%s未上传EPUB书籍", uniqueIds));
        }
        
        // 调用DRM离线加密
        HttpTool httpTool = new HttpTool();
        Map<String, String> param = new HashMap<String, String>();
        param.put(DrmConstants.DOC_IDS, JSONArray.toJSONString(uniqueIdList));
        param.put(DrmConstants.ACCESS_KEY, accessKey);
        param.put(Constants.START_TIME, startDate);
        param.put(Constants.END_TIME, endDate);
        httpTool.post(this.offLineDrmUrl, param);
        
        // 修改company表的drm_encrypt字段为离线加密中
        CompanyBean paramBean = new CompanyBean();
        paramBean.setId(companyId);
        paramBean.setDrmEncrypt(MetadataConstants.OFFLINE_EPUB_ENCRYPTING);
        companyMapper.update(paramBean);
        
        // 单位的更新有效时间
        BusinessUnitsBean business = new BusinessUnitsBean();
        business.setBusinessId(companyId);
        business.setStartTime(startDate);
        business.setStartTime(endDate);
        businessUnitsMapper.update(business);
    }
    
    /**
     * 比较时间：结束时间要大于开始时间
     * 
     * @param start
     * @param end
     * @see [类、类#方法、类#成员]
     */
    private void compareDate(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            Date currentDate = new Date();
            if (endDate.getTime() <= startDate.getTime() || startDate.getTime() < currentDate.getTime()) {
                throw new RuntimeException("有效结束时间必须大于有效开始时间，并且开始时间必须大于等于今天！");
            }
        } catch (ParseException e) {
            throw new RuntimeException("时间格式必须为yyyy-MM-dd！");
        }
    }
    
}
