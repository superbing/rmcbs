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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.bean.BookBean;
import com.bfd.dao.mapper.BookMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/drmbook")
@Api(value = "/drmbook", description = "DRM书籍下载")
@Slf4j
public class DownloadDrmBook {
    
    @Value("${epub.drm.dir}")
    private String drmBaseDir;
    
    @Autowired
    BookMapper bookMapper;
    
    public static final String LIMIT_EPUB_DRM_FILE = "limit.epub.drm";
    
    public static final String EPUB_DRM_FILE_SUFFIX = ".epub.drm";
    
    public static final String EPUB_DRM_FILE_DIR_PREFIX = "CP";
    
    /**
     * 下载DRM图书
     * 
     * @param response
     * @param businessId
     * @param bookId
     * @return
     * @see [类、类#方法、类#成员]
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载drm图书")
    public Object downloadDrmBook(HttpServletResponse response, @RequestParam Long businessId, @RequestParam String bookId) {
        BookBean book = bookMapper.getBookById(bookId);
        if (book == null) {
            String msg = String.format("%s书籍不存在.", bookId);
            log.error(msg);
            throw new RuntimeException(msg);
        }
        
        File drmParentDir = new File(drmBaseDir, EPUB_DRM_FILE_DIR_PREFIX + bookId);
        if (!drmParentDir.exists()) {
            log.error(String.format("目录%s不存在.", drmParentDir));
            throw new RuntimeException(String.format("%s书籍不存在", bookId));
        }
        
        File drmFile = new File(drmParentDir, LIMIT_EPUB_DRM_FILE);
        if (!drmFile.exists()) {
            log.error(String.format("加密文件%s不存在.", drmFile));
            throw new RuntimeException(String.format("%s书籍不存在", bookId));
        }
        
        InputStream is = null;
        OutputStream os = null;
        try {
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(book.getBookName().getBytes("UTF-8"), "iso-8859-1") + EPUB_DRM_FILE_SUFFIX);
            response.addHeader("Content-Length", "" + drmFile.length());
            response.setContentType("application/octet-stream;charset=UTF-8");
            
            is = new FileInputStream(drmFile);
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
        
        return null;
    }
    
}
