package com.bfd.authentication;

import com.bfd.domain.Resource;
import com.bfd.domain.User;
import com.bfd.exception.RmcbsException;
import com.bfd.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:自定义用户认证
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("表单登录用户名:" + username);
        User user = userRepository.loadUserByUsername(username);
        if(user==null){
            throw new RmcbsException("用户不存在");
        }
        if(!user.isEnabled()){
            throw new RmcbsException("用户已被禁用");
        }
        List<Resource> list = userRepository.loadUserPermissionByUsername(username);
        if(!CollectionUtils.isEmpty(list)){
            user.setAuthenticateList(list);
            Map<Long, List<Resource>> map = list.stream().collect(Collectors.groupingBy(Resource::getParentId));
            List<Resource> parentList = map.get(0L);
            for(Resource resource : parentList){
                resource.setChildren(map.get(resource.getId()));
            }
            user.setMenuList(parentList);
        }
        return user;
    }

}
