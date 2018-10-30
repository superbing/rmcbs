package com.bfd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.bean.CompanyBean;
import com.bfd.dao.mapper.CompanyMapper;

/**
 * SDK下载接口
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年9月20日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping(value = "/sdk", produces = {"application/json;charset=utf-8"})
@Slf4j
public class SdkController {
    
    private static final String ANDROID = "android";
    
    private static final String IOS = "ios";
    
    @Value("${android.sdkPath}")
    private String androidSdkPath;
    
    @Value("${ios.sdkPath}")
    private String iosSdkPath;
    
    @Autowired
    CompanyMapper companyMapper;
    
    /**
     * SDK下载
     * 
     * @param businessId：gateway解出token后传入的客户单位id
     * @param platform：ios、android
     * @param response
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("/download")
    public void downloadSDK(@RequestParam Long businessId, @RequestParam String platform, HttpServletResponse response) {
        CompanyBean companyBean = companyMapper.getById(businessId);
        if (companyBean == null) {
            log.error(String.format("未查询到businessId:%s对应的客户单位", businessId));
            throw new RuntimeException("SDK下载失败，客户单位不存在！");
        }
        
        if (companyBean.getStatus() == 0) {
            log.error(String.format("businessId:%s对应的客户单位已失效", businessId));
            throw new RuntimeException("SDK下载失败，客户单位已失效！");
        }
        
        // 下载
        this.downLoadFile(platform, companyBean.getAccessKey(), response);
    }
    
    /**
     * SDK下载
     * 
     * @param platform：ios、android
     * @param accessKey：作为下载的文件名称
     * @param response
     * @see [类、类#方法、类#成员]
     */
    private void downLoadFile(String platform, String accessKey, HttpServletResponse response) {
        File sdkDir = null;
        if (IOS.equals(platform)) {
            sdkDir = new File(this.iosSdkPath);
        } else if (ANDROID.equals(platform)) {
            sdkDir = new File(this.androidSdkPath);
        } else {
            log.error("下载失败，只能传入ios或者android！");
            throw new RuntimeException("SDK下载失败，只能传入ios或者android！");
        }
        
        if (!sdkDir.exists()) {
            log.error(String.format("accessKey:%s下载目录%s不存在", accessKey, sdkDir.getAbsolutePath()));
            throw new RuntimeException("SDK下载失败，请联系开发者！");
        }
        
        File file = new File(sdkDir, "sdk.zip");
        if (!file.exists()) {
            log.error(String.format("accessKey:%s文件%s不存在", accessKey, file.getAbsolutePath()));
            throw new RuntimeException("SDK下载失败，请联系开发者！");
        }
        
        InputStream is = null;
        OutputStream os = null;
        try {
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(accessKey.getBytes("UTF-8"), "iso-8859-1") + ".zip");
            response.addHeader("Content-Length", String.valueOf(file.length()));
            response.setContentType("application/octet-stream;charset=UTF-8");
            
            is = new FileInputStream(file);
            os = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while ((length = is.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
            throw new RuntimeException("SDK下载失败，请联系开发者！");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
