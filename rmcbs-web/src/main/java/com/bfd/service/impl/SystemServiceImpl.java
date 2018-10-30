package com.bfd.service.impl;

import com.bfd.bean.*;
import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.ResourceMapper;
import com.bfd.dao.mapper.RoleMapper;
import com.bfd.dao.mapper.UserMapper;
import com.bfd.enums.EnabledEnum;
import com.bfd.param.vo.ChangePasswordQO;
import com.bfd.param.vo.UserBeanVO;
import com.bfd.service.SystemService;
import com.bfd.utils.SpringSecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: bing.shen
 * @date: 2018/7/30 14:04
 * @Description :
 */
@Service
public class SystemServiceImpl implements SystemService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private ResourceMapper resourceMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(UserBean userBean) {
        
        List<UserBean> account = userMapper.findByAccount(userBean.getAccount());
        if (account != null && account.size() > Constants.ZERO) {
            throw new RmcbsException("用户名称已存在,请重新命名");
        }
        // 默认可用
        userBean.setEnabled(EnabledEnum.OPEN.getKey());
        // 密码加密
        userBean.setPassword(passwordEncoder.encode(userBean.getPassword()));
        userBean.setCreateUser(SpringSecurityUtil.getCurrentUserId());
        userMapper.insert(userBean);
        userMapper.addUserRole(userBean.getId(), userBean.getRoleId());
        return true;
    }
    
    @Override
    public UserBean getUser(Long id) {
        return userMapper.findById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserBean userBean) {
        Long userId = userBean.getId();
        if (userBean.getRoleId() != null) {
            userMapper.delUserRole(userId);
            userMapper.addUserRole(userId, userBean.getRoleId());
        }
        if (!StringUtils.isEmpty(userBean.getPassword())) {
            userBean.setPassword(passwordEncoder.encode(userBean.getPassword()));
        }
        userMapper.update(userBean);
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        userMapper.delete(id);
        userMapper.delUserRole(id);
        return true;
    }
    
    @Override
    public boolean addRole(RoleBean roleBean) {
        
        List<RoleBean> roleBeanList = roleMapper.findByName(roleBean.getName());
        if (roleBeanList != null && roleBeanList.size() > Constants.ZERO  && roleBeanList.size() > Constants.ZERO) {
            throw new RmcbsException("角色名称已存在,请重新命名");
        }
        roleBean.setCreateUser(SpringSecurityUtil.getCurrentUserId());
        roleMapper.insert(roleBean);
        return true;
    }
    
    @Override
    public RoleBean getRole(Long id) {
        return roleMapper.findById(id);
    }
    
    @Override
    public boolean updateRole(RoleBean roleBean) {
        List<RoleBean> roleBeanList = roleMapper.findByName(roleBean.getName());
        if (roleBeanList != null && roleBeanList.size() > Constants.ZERO  && !roleBeanList.get(Constants.ZERO).getId().equals(roleBean.getId())) {
            throw new RmcbsException("角色名称已存在,请重新命名");
        }
        roleMapper.update(roleBean);
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long id) {
        if(roleMapper.queryUserRole(id) > 0){
            throw new RmcbsException("此角色有用户正在使用，不能删除");
        }
        roleMapper.delete(id);
        roleMapper.deleteRoleResource(id);
        return true;
    }
    
    @Override
    public List<ResourceBean> queryAllResource() {
        List<ResourceBean> list = resourceMapper.findAll();
        Map<Long, List<ResourceBean>> map = list.stream().collect(Collectors.groupingBy(ResourceBean::getParentId));
        List<ResourceBean> parentList = map.get(0L);
        for (ResourceBean resource : parentList) {
            resource.setList(map.get(resource.getId()));
        }
        return parentList;
    }
    
    @Override
    public List<ResourceBean> queryResourceByRoleId(Long roleId) {
        List<ResourceBean> list = resourceMapper.findByRoleId(roleId);
        List<ResourceBean> parentList = null;
        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, List<ResourceBean>> map = list.stream().collect(Collectors.groupingBy(ResourceBean::getParentId));
            parentList = map.get(0L);
            for (ResourceBean resource : parentList) {
                resource.setList(map.get(resource.getId()));
            }
        }
        return parentList;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean authorize(Long roleId, Long[] resourceIds) {
        roleMapper.deleteRoleResource(roleId);
        for (Long resourceId : resourceIds) {
            roleMapper.addRoleResource(roleId, resourceId);
        }
        return true;
    }
    
    @Override
    public PageVO<RoleBean> rolePage(int pageNum, int pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<RoleBean> list = roleMapper.queryList(name);
        PageInfo<RoleBean> pageInfo = new PageInfo<>(list);
        return new PageVO<>(pageNum, pageSize, pageInfo.getTotal(), pageInfo.getList());
    }
    
    @Override
    public PageVO<UserBeanVO> userPage(int pageNum, int pageSize, String account) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserBeanVO> list = userMapper.queryList(account);
        PageInfo<UserBeanVO> pageInfo = new PageInfo<>(list);
        return new PageVO<>(pageNum, pageSize, pageInfo.getTotal(), pageInfo.getList());
    }
    
    @Override
    public List<RoleBean> queryAllRole() {
        return roleMapper.queryList(null);
    }
    
    @Override
    public void resetPassword(long id) {
        
        UserBean userBean = new UserBean();
        userBean.setId(id);
        userBean.setPassword(passwordEncoder.encode(Constants.DEFAULT_PASSWORD));
        userMapper.update(userBean);
    }
    
    @Override
    public void changePassword(ChangePasswordQO changePasswordQO) {
        
        // 查询客户信息
        UserBean userBean = userMapper.findById(changePasswordQO.getId());
        if (userBean != null) {
            // 判断密码是否正确
            if (passwordEncoder.matches(changePasswordQO.getOldPassword(), userBean.getPassword())) {
                UserBean userBean1 = new UserBean();
                userBean1.setId(changePasswordQO.getId());
                userBean1.setPassword(passwordEncoder.encode(changePasswordQO.getNewPassword()));
                userMapper.update(userBean1);
            } else {
                throw new RmcbsException("原密码错误");
            }
        }
    }
}
