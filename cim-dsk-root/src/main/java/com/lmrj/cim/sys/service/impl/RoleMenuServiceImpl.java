package com.lmrj.cim.sys.service.impl;

import com.lmrj.cim.sys.service.IRoleMenuService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.core.sys.entity.RoleMenu;
import com.lmrj.cim.modules.sys.mapper.RoleMenuMapper;
import com.lmrj.core.sys.service.IMenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("roleMenuService")
public class RoleMenuServiceImpl extends CommonServiceImpl<RoleMenuMapper,RoleMenu> implements IRoleMenuService {

    @Autowired
    private IMenuService menuService;

    @Override
    public void setMenu(String roleId, String menuIds) {
        if (!StringUtil.isEmpty(menuIds)) {
            // 删除菜单关联
            delete(new EntityWrapper<RoleMenu>(RoleMenu.class).eq("roleId", roleId));
            String[] selectMenus = menuIds.split(",");
            List<RoleMenu> roleMenuList = new ArrayList<RoleMenu>();
            for (String menuId : selectMenus) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuList.add(roleMenu);
            }
            insertOrUpdateBatch(roleMenuList);
        }
    }

    @Override
    public void insert(String roleId, String menuId) {
        EntityWrapper<RoleMenu> entityWrapper = new EntityWrapper<>(RoleMenu.class);
        entityWrapper.eq("menuId",menuId);
        entityWrapper.eq("roleId",roleId);
        int count = selectCount(entityWrapper);
        if(count == 0){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            insert(roleMenu);
        }
    }
}
