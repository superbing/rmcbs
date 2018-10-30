package com.bfd.service;

import com.bfd.bean.ResourceBean;
import com.bfd.bean.RoleBean;
import com.bfd.bean.UserBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.ChangePasswordQO;
import com.bfd.param.vo.UserBeanVO;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/7/30 14:04
 * @Description :
 */
public interface SystemService {

    /**
     * 新增用户
     * @param userBean
     * @return
     */
    boolean addUser(UserBean userBean);

    /**
     * 获取单个用户
     * @return
     */
    UserBean getUser(Long id);

    /**
     * 更新用户
     * @param userBean
     * @return
     */
    boolean updateUser(UserBean userBean);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean deleteUser(Long id);

    /**
     * 新增角色
     * @param roleBean
     * @return
     */
    boolean addRole(RoleBean roleBean);

    /**
     * 获取角色
     * @param id
     * @return
     */
    RoleBean getRole(Long id);

    /**
     * 更新角色
     * @param roleBean
     * @return
     */
    boolean updateRole(RoleBean roleBean);

    /**
     * 删除角色
     * @param id
     * @return
     */
    boolean deleteRole(Long id);

    /**
     * 查询所有资源
     * @return
     */
    List<ResourceBean> queryAllResource();

    /**
     * 查询角色下的资源
     * @return
     */
    List<ResourceBean> queryResourceByRoleId(Long roleId);

    /**
     * 为角色授权
     * @return
     */
    boolean authorize(Long roleId, Long[] resourceIds);

    /**
     * 角色分页
     * @return
     */
    PageVO<RoleBean> rolePage(int pageNum, int pageSize, String name);

    /**
     * 用户分页
     * @return
     */
    PageVO<UserBeanVO> userPage(int pageNum, int pageSize, String account);

    /**
     * 查询所有角色
     * @return
     */
    List<RoleBean> queryAllRole();

    /**
     * 重置密码
     * @param id
     */
    void resetPassword(long id);

    /**
     * 修改密码
     * @param changePasswordQO
     */
    void changePassword(ChangePasswordQO changePasswordQO);
}
