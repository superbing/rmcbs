package com.bfd.dao.mapper;

import com.bfd.bean.BookBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;


/**
 *  栏目 mapper
 *
 * @author bing.shen
 * @date 2018-07-27
 */
@Repository
public interface BookMapper {

    /**
     * 获取图书元数据
     * @param id
     * @return
     */
    BookBean getBookById(String id);

    /**
     * 获取按单位查询某图书所属个性化栏目数
     * @param bookId
     * @param businessId
     * @return
     */
    Long getPrivateCount(@Param("bookId") String bookId, @Param("businessId") Long businessId);

    /**
     * 获取按单位查询某图书所属公共栏目数
     * @param bookId
     * @param businessId
     * @return
     */
    Long getPublicCount(@Param("bookId") String bookId, @Param("businessId") Long businessId);

    /**
     * 获取客户单位有效期到期时间
     *
     * @param businessId
     * @return
     */
    Map<String, Object> getBusinessTermOfValidity(Long businessId);

}


