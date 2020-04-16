package com.lmrj.core.sys.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.sys.entity.UserRole;

/**
 *
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @title: IUserRoleService.java
 * @package com.lmrj.web.modules.sys.service
 * @description: 角色
 * @author: 张飞
 * @date: 2017年7月11日 下午9:21:20
 * @version V1.0
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 *
 */
public interface IUserRoleService extends ICommonService<UserRole> {

    void insert(String uid, String roleCode);

    void insertByRoleId(String uid, String roleId);

    void deleteUserRole(String uid, String roleId);
}
