package com.lmrj.cim.modules.sys.mapper;


import java.util.List;

import com.lmrj.core.sys.entity.Role;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
	/**
	 *
	 * @title: findRoleByUserId
	 * @description: 通过用户查找角色
	 * @param userId
	 * @return
	 * @return: List<Role>
	 */
	List<Role> findRoleByUserId(String userId);
}
