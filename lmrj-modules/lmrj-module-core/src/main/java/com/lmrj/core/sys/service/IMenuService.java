package com.lmrj.core.sys.service;

import com.lmrj.common.mybatis.mvc.service.ITreeCommonService;
import com.lmrj.core.sys.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @author jwcg
 * @date 2014-12-20 21:33:32
 * @version V1.0
 *
 */
public interface IMenuService extends ITreeCommonService<Menu> {

	/**
	 * 通过用户ID查找菜单
	 *
	 * @return
	 */
	List<Menu> findMenuByUserId(String userId);

	/**
	 * 通过角色查找菜单
	 *
	 * @return
	 */
	List<Menu> findMenuByRoleId(String roleId);

	/**
	 * 通过用户ID查找菜单
	 *
	 * @return
	 */
	List<String> findPermissionByUserId(String userId);

	/**
	 * 通过用户ID查找菜单
	 *
	 * @return
	 */
	List<String> findPermissionByRoleId(String roleId);

	void changeSort(String menuId, Integer sort);

	void generateButton(String menuId,
						String parentPermission,
						String[] permissions,
						String[] permissionTitles);

	/**
	 * 根据userId还有 父级id
	 * @param userId
	 * @param nodeId
	 * @return
	 */
	List<Menu> findMenuByUserIdAndNodeId(String userId, String nodeId);

    List<Map> selectChart();
}
