package com.bfd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.bean.CompanyBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.Result;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.service.DrmManageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * DRM管理
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年8月17日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping(value = "/drmManage", produces = {"application/json;charset=utf-8"})
@Api(value = "DRM管理", description = "DRM管理")
@Slf4j
public class DrmManageController {
    
    @Autowired
    DrmManageService drmManageService;
    
    @Autowired
    CompanyMapper companyMapper;
    
    @Value("${android.sdkPath}")
    private String androidSdkPath;
    
    @Value("${ios.sdkPath}")
    private String iosSdkPath;
    
    /**
     * 离线加密
     * 
     * @param companyId
     * @return
     * @see [类、类#方法、类#成员]
     */
    // @PostMapping(value = "/offline/encrypt")
    // @ApiOperation(value = "离线加密")
    public Object offLineEncrypt(@RequestParam Long companyId, @RequestParam String startDate, @RequestParam String endDate) {
        log.info(String.format("离线加密param-companyId:%s,startDate:%s,endDate:%s", companyId, startDate, endDate));
        drmManageService.offLineEncrypt(companyId, startDate, endDate);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
    }
    
    @GetMapping(value = "/downloadSDK")
    @ApiOperation(value = "下载SDK")
    public void downloadSDK(@RequestParam String accessKey, @RequestParam String platformType, HttpServletResponse response) {
        CompanyBean companyBean = companyMapper.getByAccessKey(accessKey);
        if (companyBean == null) {
            log.error(String.format("未查询到accessKey:%s对应的客户单位", accessKey));
            throw new RmcbsException("SDK下载失败，客户单位不存在！");
        }
        
        if (companyBean.getStatus() == 0) {
            log.error(String.format("accessKey:%s对应的客户单位已失效", accessKey));
            throw new RmcbsException("SDK下载失败，客户单位已失效！");
        }
        
        // String sdkFileName = accessKey + ".zip";
        // this.fileToZip(accessKey, sdkFileName);
        
        this.downLoadFile(platformType, accessKey, response);
    }
    
    /**
     * 将SDK目录下的文件压缩到SDK父级目录下，文件路径：../父目录/accessKey.zip
     * 
     * @param accessKey
     * @param sdkFileName
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unused")
    private void fileToZip(String accessKey, String sdkFileName) {
        File sdkDir = new File(androidSdkPath);
        if (!sdkDir.exists()) {
            log.error(String.format("SDK目录:%s不存在", androidSdkPath));
            throw new RmcbsException("SDK下载失败！");
        }
        
        String sdkParentPath = sdkDir.getParent();
        File zipFile = new File(sdkParentPath, sdkFileName);
        
        ZipOutputStream os = null;
        try {
            zipFile.createNewFile();
            os = new ZipOutputStream(new FileOutputStream(zipFile));
            File[] jsFiles = sdkDir.listFiles();
            byte[] buffer = new byte[1024 * 10];
            for (int i = 0; i < jsFiles.length; i++) {
                FileInputStream is = null;
                try {
                    log.info("zip-->file-->" + jsFiles[i].getName());
                    is = new FileInputStream(jsFiles[i]);
                    os.putNextEntry(new ZipEntry(jsFiles[i].getName()));
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        os.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    log.error("[压缩文件失败]", e);
                } finally {
                    try {
                        os.closeEntry();
                    } catch (IOException e) {
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            log.info(zipFile + " 生成成功");
        } catch (Exception e) {
            log.error("生成ZIP文件失败", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    /**
     * SDK下载
     * 
     * @param downLoadFileName
     * @param response
     * @see [类、类#方法、类#成员]
     */
    private void downLoadFile(String platformType, String accessKey, HttpServletResponse response) {
        
        File sdkDir = null;
        if ("ios".equals(platformType)) {
            sdkDir = new File(this.iosSdkPath);
        } else if ("android".equals(platformType)) {
            sdkDir = new File(this.androidSdkPath);
        } else {
            throw new RmcbsException("下载失败，只能传入ios或者android！");
        }
        
        if (!sdkDir.exists()) {
            throw new RmcbsException("下载失败，下载目录不存在！");
        }
        
        // String parent = sdkDir.getParent();
        
        InputStream is = null;
        OutputStream os = null;
        try {
            File file = new File(sdkDir, "sdk.zip");
            
            if (!file.exists()) {
                throw new RmcbsException("下载失败，sdk文件不存在！");
            }
            
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(accessKey.getBytes("UTF-8"), "iso-8859-1") + ".zip");
            response.addHeader("Content-Length", "" + file.length());
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
    
    public static void main(String[] args) {
        File file = new File("E://sdk");
        System.out.println(file.getParent());
    }
    
}
