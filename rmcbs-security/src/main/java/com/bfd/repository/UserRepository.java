package com.bfd.repository;

import com.bfd.domain.Resource;
import com.bfd.domain.User;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:用户信息查询接口
 */
public interface UserRepository {

    User loadUserByUsername(String account);

    boolean hasPermission(HttpServletRequest request, Authentication authentication);

    List<Resource> loadUserPermissionByUsername(String account);

    int updateUserIp(String lastIp, String account);
}
