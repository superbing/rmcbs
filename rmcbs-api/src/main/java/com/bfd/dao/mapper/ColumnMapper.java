package com.bfd.dao.mapper;

import com.bfd.bean.BookBean;
import com.bfd.bean.ColumnBean;
import com.bfd.bean.PublicColumnBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  栏目 mapper
 *
 * @author bing.shen
 * @date 2018-07-27
 */
@Repository
public interface ColumnMapper {

    /**
     * 查询公共栏目
     * @param businessId
     * @return
     */
    List<PublicColumnBean> queryColumnList(Long businessId);

    /**
     * 查询一级栏目下PDF书籍
     * @param columnId
     * @param order
     * @return
     */
    List<BookBean> queryPcParentMetadataList(@Param("columnId") Long columnId, @Param("order") String order);

    /**
     * 查询二级栏目下PDF书籍
     * @param columnId
     * @param order
     * @return
     */
    List<BookBean> queryPcMetadataList(@Param("columnId") Long columnId, @Param("order") String order);

    /**
     * 查询一级栏目下EPUB书籍
     * @param columnId
     * @param order
     * @return
     */
    List<BookBean> queryAppParentMetadataList(@Param("columnId") Long columnId, @Param("order") String order);

    /**
     * 查询二级栏目下EPUB书籍
     * @param columnId
     * @param order
     * @return
     */
    List<BookBean> queryAppMetadataList(@Param("columnId") Long columnId, @Param("order") String order);
}


