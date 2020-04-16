package com.lmrj.core.sys.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.sys.entity.User;

/**
 *
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @title: IUserService.java
 * @package com.lmrj.web.modules.sys.service
 * @description: 用户
 * @author: 张飞
 * @date: 2017年7月11日 下午9:21:07
 * @version V1.0
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 *
 */
public interface IUserService extends ICommonService<User> {
	/**
	 * 修改密码
	 *
	 * @param userId
	 * @param newPassword
	 */
	void changePassword(String userId, String newPassword);

	/**
	 * 根据用户名查找用户
	 *
	 * @param username
	 * @return
	 */
	public User findByUsername(String username);

	/**
	 * 根据Email查找用户
	 *
	 * @param email
	 * @return
	 */
	User findByEmail(String email);

	/**
	 * 根据Email查找用户
	 *
	 * @param phone
	 * @return
	 */
	User findByPhone(String phone);

	/**
	 * 检查密码
	 * @param userId
	 * @param password
	 * @return
	 */
	Boolean checkPassword(String userId, String password);

	/**
	 * <p>
	 * 翻页查询
	 * </p>
	 *
	 * @param page    翻页对象
	 * @param wrapper 实体包装类 {@link Wrapper}
	 * @return
	 */
	Page<User> selectNewUser(Page<User> page, Wrapper<User> wrapper);

}
