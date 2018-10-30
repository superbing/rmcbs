package com.bfd.service;

import com.bfd.bean.BookBean;
import com.bfd.bean.XmlChapter;
import com.bfd.bean.XmlContent;
import com.bfd.common.vo.PageVO;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/8/22 16:56
 * @Description:
 */
public interface BookService {

    /**
     * 获取图书详情
     * @param bookId
     * @param businessId
     * @return
     */
    BookBean getBookById(String bookId, Long businessId);

    /**
     * 图书权限验证
     * @param bookId
     * @param businessId
     * @return
     */
    Boolean authentication(String bookId, Long businessId);

    /**
     * 查询目录
     * @param bookId
     * @param businessId
     * @return
     * @throws Exception
     */
    List<XmlChapter> queryChapter(String bookId, Long businessId) throws Exception;

    /**
     * 查询内容
     * @param pageNum
     * @param pageSize
     * @param searchContent
     * @param bookId
     * @param businessId
     * @return
     * @throws Exception
     */
    PageVO<XmlContent> queryContent(int pageNum, int pageSize, String searchContent, String bookId, Long businessId) throws Exception;
}
