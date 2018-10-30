package com.bfd.service;

/**
 * @author: bing.shen
 * @date: 2018/9/19 15:40
 * @Description:
 */
public interface ExtractService {

    int done(String bookId, String pdfFilename, String jpgFilename, String jsFileName, String txtFileName, int dpi)
            throws Exception;
}
