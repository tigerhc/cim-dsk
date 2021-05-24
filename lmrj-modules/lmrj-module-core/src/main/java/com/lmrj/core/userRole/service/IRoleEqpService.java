package com.lmrj.core.userRole.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.userRole.entity.IotRoleEqp;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:09
 */
public interface IRoleEqpService extends ICommonService<IotRoleEqp> {
    List<String> getEqpByRoleList(List<String> roleList);
}
