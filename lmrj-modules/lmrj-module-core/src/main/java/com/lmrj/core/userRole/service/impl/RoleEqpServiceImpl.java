package com.lmrj.core.userRole.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.userRole.entity.IotRoleEqp;
import com.lmrj.core.userRole.mapper.RoleEqpMapper;
import com.lmrj.core.userRole.service.IRoleEqpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:12
 */
@Transactional
@Service("roleEqpService")
public class RoleEqpServiceImpl  extends CommonServiceImpl<RoleEqpMapper, IotRoleEqp> implements IRoleEqpService {
    @Override
    public List<String> getEqpByRoleList(List<String> roleList) {
        List<String> retList = new ArrayList<>();
      List<IotRoleEqp> iotRoleEqps = baseMapper.getEqpByRoleList(roleList);
        for (IotRoleEqp iotRoleEqp:iotRoleEqps) {
            retList.add(iotRoleEqp.getEqpId());
        }
        return retList;
    }
}
