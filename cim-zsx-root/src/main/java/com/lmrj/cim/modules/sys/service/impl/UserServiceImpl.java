package com.lmrj.cim.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lmrj.cim.modules.sys.service.IUserOrganizationService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.core.sys.entity.User;
import com.lmrj.core.sys.entity.UserOrganization;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.cim.modules.sys.mapper.UserMapper;
import com.lmrj.core.sys.service.IUserRoleService;
import com.lmrj.core.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

@Transactional
@Service("userService")
public class UserServiceImpl extends CommonServiceImpl<UserMapper, User> implements IUserService {
	@Autowired
	PasswordService passwordService;
	@Autowired
	private IUserOrganizationService userOrganizationService;
	@Autowired
	private IUserRoleService userRoleService;

	@Override
	public void changePassword(String userid, String newPassword) {
		User user = selectById(userid);
		if (user != null) {
			user.setPassword(newPassword);
			passwordService.encryptPassword(user);
		}
		insertOrUpdate(user);
	}

	@Override
	public User findByUsername(String username) {
		if (StringUtil.isEmpty(username)) {
			return null;
		}
		return selectOne(new EntityWrapper<User>(User.class).eq("username", username));
	}

	@Override
	public User findByEmail(String email) {
		if (StringUtil.isEmpty(email)) {
			return null;
		}
		return selectOne(new EntityWrapper<User>(User.class).eq("email", email));
	}

	@Override
	public User findByPhone(String phone) {
		if (StringUtil.isEmpty(phone)) {
			return null;
		}
		return selectOne(new EntityWrapper<User>(User.class).eq("phone", phone));
	}

	@Override
	public boolean deleteById(Serializable id) {
		// 删除角色关联
		userRoleService.delete(new EntityWrapper<UserRole>(UserRole.class).eq("userId", id));
		// 删除部门关联
		userOrganizationService.delete(new EntityWrapper<UserOrganization>(UserOrganization.class).eq("userId", id));
		return super.deleteById(id);
	}

	@Override
	public boolean deleteBatchIds(Collection<? extends Serializable> idList) {
		for (Object id : idList) {
			this.deleteById((Serializable) id);
		}
		return true;
	}

	@Override
	public Page<User> selectPage(Page<User> page, Wrapper<User> wrapper) {
		wrapper.eq("1", "1");
		page.setRecords(baseMapper.selectUserList(page, wrapper));
		return page;
	}

	/**
	 * <p>
	 * 翻页查询
	 * </p>
	 *
	 * @param page    翻页对象
	 * @param wrapper 实体包装类 {@link Wrapper}
	 * @return
	 */
	public Page<User> selectNewUser(Page<User> page, Wrapper<User> wrapper){
		wrapper.eq("1", "1");
		// TODO: 2019/6/20
		page.setRecords(baseMapper.selectUserList(page, wrapper));
		return page;
	}

	@Override
	public Boolean checkPassword(String userId, String password) {
		User user=selectById(userId);
		if (user==null){
			return Boolean.FALSE;
		}
		String newPassword=passwordService.getPassword(password,user.getCredentialsSalt());
		if (newPassword.equals(user.getPassword())){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public boolean insert(User user) {
		//账号重复
		if (selectCount(new EntityWrapper<User>().eq("username",user.getUsername()))>0){
			throw new RuntimeException("账号重复");
		}
		//检查手机号码
		if (selectCount(new EntityWrapper<User>().eq("phone",user.getPhone()))>0){
			throw new RuntimeException("手机号码重复");
		}
		passwordService.encryptPassword(user);
		return super.insert(user);
	}

	@Override
	public boolean insertOrUpdate(User user) {
		//账号重复
		if (selectCount(new EntityWrapper<User>().ne("id",user.getId()).eq("username",user.getUsername()))>0){
			throw new RuntimeException("账号重复");
		}
		//检查手机号码
		if (selectCount(new EntityWrapper<User>().ne("id",user.getId()).eq("phone",user.getPhone()))>0){
			throw new RuntimeException("手机号码重复");
		}
		return super.insertOrUpdate(user);
	}


}
