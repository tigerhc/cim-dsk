package com.lmrj.cim.modules.sys.mapper;

import java.util.List;
import java.util.Map;

import com.lmrj.common.mybatis.mvc.mapper.BaseTreeMapper;
import com.lmrj.core.sys.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MenuMapper extends BaseTreeMapper<Menu> {

	/**
	 *
	 * @title: findMenuByUserId
	 * @description: 通过用户查找菜单
	 * @param userId
	 * @return
	 * @return: List<Menu>
	 */
	List<Menu> findMenuByUserId(String userId);

	/**
	 *
	 * @title: findMenuByRoleId
	 * @description: 通过角色查找菜单
	 * @param roleId
	 * @return
	 * @return: List<Menu>
	 */
	List<Menu> findMenuByRoleId(String roleId);


	/**
	 *
	 * @title: findMenuByUserId
	 * @description: 通过用户查找菜单
	 * @param userId
	 * @return
	 * @return: List<Menu>
	 */
	List<String> findPermissionByUserId(String userId);

	/**
	 *
	 * @title: findMenuByRoleId
	 * @description: 通过角色查找菜单
	 * @param roleId
	 * @return
	 * @return: List<Menu>
	 */
	List<String> findPermissionByRoleId(String roleId);

	/**
	 * 根据当前用户id
	 * @param userId
	 * @param nodeId
	 * @return
	 */
	List<Menu> findMenuByUserIdAndNodeId(@Param(value = "userId") String userId, @Param(value = "nodeId") String nodeId);

	List<Map> selectChart();
}
