package com.bfd.dao.mapper;

import java.util.List;

import com.bfd.bean.UserBean;
import com.bfd.param.vo.UserBeanVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author: bing.shen
 * @date: 2018/7/30 14:04
 * @Description :
 */
@Repository
public interface UserMapper {

	/**
	 * 新增
	 * @param user
	 * @return
	 */
	int insert(UserBean user);

	/**
	 * 修改
	 * @param user
	 * @return
	 */
	int update(UserBean user);

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
	UserBean findById(Long id);

	/**
	 * 根据名称获取
	 * @param account
	 * @return
	 */
	List<UserBean> findByAccount(@Param(value = "account") String account);

	/**
	 * 查询列表
	 * @param account
	 * @return
	 */
	List<UserBeanVO> queryList(@Param("account") String account);

	/**
	 * 用户添加角色
	 * @param roleId
	 * @param userId
	 * @return
	 */
	int addUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

	/**
	 * 删除用户角色
	 * @param userId
	 * @return
	 */
	int delUserRole(@Param("userId") Long userId);
}
