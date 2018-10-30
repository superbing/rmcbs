package com.bfd.service;

import com.bfd.bean.BookBean;
import com.bfd.bean.PrivateColumnBean;
import com.bfd.bean.PublicColumnBean;
import com.bfd.common.vo.PageVO;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/8/8 17:48
 * @Description:
 */
public interface ColumnService {

    /**
     * 查询公共栏目
     * @param businessId
     * @return
     */
    List<PublicColumnBean> queryPublicColumn(Long businessId);

    /**
     * 查询公共栏目
     * @param businessId
     * @return
     */
    List<PrivateColumnBean> queryPrivateColumn(Long businessId);

    /**
     * 分页查询栏目下PDF书籍
     * @param pageNum
     * @param pageSize
     * @param columnId
     * @param type
     * @param parentId
     * @param order
     * @return
     */
    PageVO<BookBean> bookPcPage(int pageNum, int pageSize, Long columnId, int type, Long parentId, String order);

    /**
     * 分页查询栏目下Epub书籍
     * @param pageNum
     * @param pageSize
     * @param columnId
     * @param type
     * @param parentId
     * @param order
     * @return
     */
    PageVO<BookBean> bookAppPage(int pageNum, int pageSize, Long columnId, int type, Long parentId, String order);

    /**
     * 获取PDF模板ID
     * @param businessId
     * @return
     */
    String getTemplate(Long businessId);
}
