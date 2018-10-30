package com.bfd.repository;

import com.bfd.domain.Resource;
import com.bfd.domain.ResourceRowMapper;
import com.bfd.domain.User;
import com.bfd.properties.SecurityConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.Authentication;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:用户信息查询接口实现
 */
public class UserRepositoryImpl extends JdbcDaoSupport implements UserRepository{

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public User loadUserByUsername(String account) {
        User user = null;
        String sql = "select * from t_user where account = ?";
        try {
            user = this.getJdbcTemplate().queryForObject(sql, new Object[]{account}, new BeanPropertyRowMapper<>(User.class));
        }catch (EmptyResultDataAccessException e){
            return user;
        }
        return user;
    }

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;
        if (principal instanceof User) {
            //如果用户名是admin，就永远返回true
            if (StringUtils.equals(((User) principal).getUsername(), SecurityConstants.ADMIN_ACCOUNT)) {
                hasPermission = true;
            } else {
                // 读取用户所拥有权限的所有URL
                List<Resource> list = ((User) principal).getAuthenticateList();
                for (Resource resource : list) {
                    if (antPathMatcher.match(resource.getUrl(), request.getServletPath())) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        }
        return hasPermission;
    }

    @Override
    public List<Resource> loadUserPermissionByUsername(String account) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from (");
        sql.append("select t_resource.* from t_user,t_user_role,t_role,t_role_resource,t_resource ");
        sql.append("where t_user.id=t_user_role.user_id ");
        sql.append("and t_user_role.role_id=t_role.id ");
        sql.append("and t_role.id=t_role_resource.role_id ");
        sql.append("and t_role_resource.resource_id=t_resource.id ");
        sql.append("and t_user.account = ? ");
        sql.append("union ");
        sql.append("select t_resource.* from t_resource where id in ( ");
        sql.append("select t_resource.parent_id from t_user,t_user_role,t_role,t_role_resource,t_resource ");
        sql.append("where t_user.id=t_user_role.user_id ");
        sql.append("and t_user_role.role_id=t_role.id ");
        sql.append("and t_role.id=t_role_resource.role_id  ");
        sql.append("and t_role_resource.resource_id=t_resource.id ");
        sql.append("and t_user.account = ? )");
        sql.append(") t order by t.id");
        return this.getJdbcTemplate().query(sql.toString(), new ResourceRowMapper(), account, account);
    }

    @Override
    public int updateUserIp(String lastIp, String account) {
        String sql = "update t_user set last_ip=?,last_time=now() where account=?";
        return this.getJdbcTemplate().update(sql,lastIp, account);
    }
}
