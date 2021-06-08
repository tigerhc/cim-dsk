package com.lmrj.fab.userRole.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.userRole.entity.IotRoleEqp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:09
 */
public interface IIotRoleEqpService extends ICommonService<IotRoleEqp> {
    List<String> getEqpByRoleList(List<String> roleList);

    List<String> listOrgIds(String orgid);

    int getnum(String eqpId,String roleId);

    void deleteByroleAndEqp(String eqpId, String roleId);

    void insertByroleAndEqp(String eqpId, String roleId);
}
