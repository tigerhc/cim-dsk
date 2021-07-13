package com.lmrj.fab.userRole.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.userRole.entity.FabUserRole;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:07
 */
public interface IFabUserRoleService extends ICommonService<FabUserRole> {
    List<String> getRoleByUserId(String userId);


    public void insertByRoleId(String uid, String roleId) ;
}
