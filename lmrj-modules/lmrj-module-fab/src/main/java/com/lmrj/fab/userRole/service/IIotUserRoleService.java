package com.lmrj.fab.userRole.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.fab.userRole.entity.IotUserRole;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:07
 */
public interface IIotUserRoleService extends ICommonService<IotUserRole> {
    List<String> getRoleByUserId(String userId);


    public void insertByRoleId(String uid, String roleId) ;
}
