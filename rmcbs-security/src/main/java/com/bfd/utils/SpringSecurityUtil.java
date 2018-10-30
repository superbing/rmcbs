package com.bfd.utils;

import com.bfd.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:工具类
 */
public class SpringSecurityUtil {

    /**
     * 获取当前用户信息
     * @return
     */
    public static User getCurrentUser() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    /**
     * 获取当前用户信息
     * @return
     */
    public static Long getCurrentUserId() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
