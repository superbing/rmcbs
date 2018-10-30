package com.bfd.service;

import java.util.Map;

/**
 *
 * DRM图书验证
 * @author:
 * @date:
 * @Description:
 */
public interface CheckDrmBookService {

    /**
     * 验证图书接口
     * @param bookId 图书id
     * @param businessId 客户单位id
     * @param token  设备信息
     * @return
     */
    Map<String,Object> checkDrmBook(String bookId, Long businessId, String token);
}
