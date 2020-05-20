package com.lmrj.cim.modules.sys.service.impl;

import com.lmrj.cim.modules.sys.mapper.MenuMapper;
import com.lmrj.common.mybatis.mvc.service.impl.TreeCommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.sys.entity.Menu;
import com.lmrj.core.sys.service.IMenuService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("menuService")
public class MenuServiceImpl extends TreeCommonServiceImpl<MenuMapper, Menu> implements IMenuService {

	@Override
	public List<Menu> findMenuByUserId(String userId) {
		return baseMapper.findMenuByUserId(userId);
	}

	@Override
	public List<Menu> findMenuByRoleId(String roleId) {
		return baseMapper.findMenuByRoleId(roleId);
	}

	@Override
	public List<String> findPermissionByUserId(String userId) {
		return baseMapper.findPermissionByUserId(userId);
	}

	@Override
	public List<String> findPermissionByRoleId(String roleId) {
		return baseMapper.findPermissionByRoleId(roleId);
	}

	@Override
	public void changeSort(String menuId, Integer sort) {
		Menu menu = selectById(menuId);
		menu.setSort(sort);
		insertOrUpdate(menu);
	}

	@Override
	public void generateButton(String menuId,
							   String parentPermission,
							   String[] permissions,
							   String[] permissionTitles) {
		EntityWrapper<Menu> deleteEntityWrapper = new EntityWrapper();
		deleteEntityWrapper.eq("parent_id",menuId);
		deleteEntityWrapper.eq("type",3);
		delete(deleteEntityWrapper);
		List<Menu> menuList = new ArrayList<>();
		Menu parentmenu = selectById(menuId);

		for (int i = 0; i < permissions.length; i++) {
			String permission = permissions[i];
			Menu menu = new Menu();
			menu.setParentId(menuId);
			menu.setParentIds(parentmenu.getParentIds()+menuId+"/");
			menu.setPath(parentmenu.getPath()+StringUtil.cap(permission));
			if("add".equals(permission)||"update".equals(permission)){
				menu.setType("4");
				String component=  parentmenu.getComponent();
				if(StringUtil.isNotBlank(component)){ //父节点有组件名,路由才能有组件名
					String permissionNew = permission;
					if("update".equals(permission)){
						permissionNew = "edit";
					}
					menu.setComponent(component.substring(0, component.lastIndexOf("/")+1)+permissionNew);
				}
			}else{
				menu.setType("3");
			}
			menu.setName(permissionTitles[i]);
			menu.setPermission(parentPermission + ":" + permission);
			menu.setSort(i);
			menu.setEnabled((short) 1);
			menu.setProjectId(parentmenu.getProjectId());
			menuList.add(menu);
		}
		insertBatch(menuList);
	}

	@Override
	public List<Menu> findMenuByUserIdAndNodeId(String userId, String nodeId) {
		return baseMapper.findMenuByUserIdAndNodeId(userId,nodeId);
	}

}
