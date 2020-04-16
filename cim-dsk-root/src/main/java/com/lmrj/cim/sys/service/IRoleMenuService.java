package com.lmrj.cim.sys.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.sys.entity.RoleMenu;

/**
 * @Title:
 * @Description:
 * @author lmrj
 * @date 2017-02-21 12:54:43
 * @version V1.0
 *
 */
public interface IRoleMenuService extends ICommonService<RoleMenu>  {

    void insert(String roleId, String menuId);

    void setMenu(String roleId, String menuIds);
}

