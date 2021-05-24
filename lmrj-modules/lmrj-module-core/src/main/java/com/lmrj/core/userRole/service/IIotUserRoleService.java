package com.lmrj.core.userRole.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.userRole.entity.IotRoleUser;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:07
 */
public interface IIotUserRoleService extends ICommonService<IotRoleUser> {
    List<String> getRoleByUserId(String userId);
}
