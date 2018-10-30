package com.bfd.controller;

import com.bfd.aop.LimitDevice;
import com.bfd.common.vo.Result;
import com.bfd.service.BookService;
import com.bfd.aop.LimitIp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author: bing.shen
 * @date: 2018/8/2 15:56
 * @Description:数据包接口
 */
@RestController
@RequestMapping(BookController.REQ_URL)
@Api(value = BookController.REQ_URL, description = "图书接口")
public class BookController {

    public static final String REQ_URL = "/book";

    @Autowired
    private BookService bookService;

    @Value("${pdf.dir}")
    private String pdfDir;

    @GetMapping("/getImg")
    @ApiOperation(value = "获取PDF图片")
    @LimitIp
    public String getImg(HttpServletResponse response,@RequestParam Long businessId,
                           @RequestParam String bookId, @RequestParam int pageNum){
        //鉴权
        if(!bookService.authentication(bookId, businessId)){
            throw new RuntimeException("无权访问此图书");
        }
        String fileName = "(" + pageNum + ").jpg";
        return downloadFile(fileName, bookId, response);
    }

    @GetMapping("/getJs")
    @ApiOperation(value = "获取PDF图片对应的JS")
    @LimitIp
    public String getJs(HttpServletResponse response,@RequestParam Long businessId,
                         @RequestParam String bookId, @RequestParam int pageNum){
        //鉴权
        if(!bookService.authentication(bookId, businessId)){
            throw new RuntimeException("无权访问此图书");
        }
        String fileName = "(" + pageNum + ").js";
        return downloadFile(fileName, bookId, response);
    }

    private String downloadFile(String fileName, String bookId, HttpServletResponse response){
        if (fileName != null) {
            File file = new File(pdfDir + bookId, fileName);
            BufferedInputStream in = null;
            ServletOutputStream out = null;
            if (file.exists()) {
                try {
                    in = new BufferedInputStream(new FileInputStream(file));
                    out = response.getOutputStream();
                    IOUtils.copy(in,out);
                    out.flush();
                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                return "文件不存在";
            }
        }
        return "下载失败";
    }

    @GetMapping(value = "/appDetail")
    @ApiOperation(value = "获取图书元数据")
    @LimitDevice
    public Result<Object> appDetail(@RequestParam String bookId, @RequestParam Long businessId){
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),
                bookService.getBookById(bookId, businessId));
    }

    @GetMapping(value = "/authentication")
    @ApiOperation(value = "验证是否有看此图书的权限")
    @LimitDevice
    public Result<Object> authentication(@RequestParam String bookId, @RequestParam Long businessId){
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),
                bookService.authentication(bookId, businessId));
    }

    @GetMapping(value = "/queryChapter")
    @ApiOperation(value = "查询目录")
    @LimitIp
    public Result<Object> queryChapter(@RequestParam String bookId, @RequestParam Long businessId) throws Exception{
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),
                bookService.queryChapter(bookId, businessId));
    }

    @GetMapping(value = "/queryContent")
    @ApiOperation(value = "查询内容")
    @LimitIp
    public Result<Object> queryContent(@RequestParam(defaultValue = "1") int pageNum,
           @RequestParam(defaultValue = "10") int pageSize, @RequestParam String searchContent,
           @RequestParam String bookId, @RequestParam Long businessId) throws Exception{
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),
                bookService.queryContent(pageNum, pageSize, searchContent, bookId, businessId));
    }
}
