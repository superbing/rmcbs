package com.bfd.dao.mapper;

import com.bfd.bean.RoleBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/7/30 14:04
 * @Description :
 */
@Repository
public interface RoleMapper {
    /**
     * 新增
     * @param role
     * @return
     */
    int insert(RoleBean role);

    /**
     * 更新
     * @param role
     * @return
     */
    int update(RoleBean role);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 查询单条
     * @param id
     * @return
     */
    RoleBean findById(Long id);

    /**
     * 根据名称查询角色
     * @param name
     * @return
     */
    List<RoleBean> findByName(@Param("name")String name);

    /**
     * 查询列表
     * @param name
     * @return
     */
    List<RoleBean> queryList(@Param("name") String name);

    /**
     * 删除关系表
     * @param id
     * @return
     */
    int deleteRoleResource(Long id);

    /**
     * 角色授权
     * @param roleId
     * @param resourceId
     * @return
     */
    int addRoleResource(@Param("roleId") Long roleId, @Param("resourceId") Long resourceId);

    /**
     * 查询是否有用户正在使用
     * @param roleId
     * @return
     */
    int queryUserRole(@Param("roleId") Long roleId);
}
