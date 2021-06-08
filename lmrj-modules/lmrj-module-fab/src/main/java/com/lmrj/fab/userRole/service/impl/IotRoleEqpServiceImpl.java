package com.lmrj.fab.userRole.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.userRole.entity.IotRoleEqp;
import com.lmrj.fab.userRole.mapper.IotRoleEqpMapper;
import com.lmrj.fab.userRole.service.IIotRoleEqpService;
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
public class IotRoleEqpServiceImpl extends CommonServiceImpl<IotRoleEqpMapper, IotRoleEqp> implements IIotRoleEqpService {
    @Override
    public List<String> getEqpByRoleList(List<String> roleList) {
        List<String> retList = new ArrayList<>();
      List<IotRoleEqp> iotRoleEqps = baseMapper.getEqpByRoleList(roleList);
        for (IotRoleEqp iotRoleEqp:iotRoleEqps) {
            retList.add(iotRoleEqp.getEqpId());
        }
        return retList;
    }

    @Override
    public List<String> listOrgIds(String orgid) {

        return baseMapper.listOrgIds(orgid);
    }

    @Override
    public int getnum(String eqpId,String roleId){
        return baseMapper.selectCount(new EntityWrapper<IotRoleEqp>().eq("eqp_id",eqpId).andNew().eq("role_id",roleId));
    }
    @Override
    public void deleteByroleAndEqp(String eqpId,String roleId){
        baseMapper.delete(new EntityWrapper<IotRoleEqp>().eq("eqp_id",eqpId).andNew().eq("role_id",roleId));
    }
    @Override
    public void insertByroleAndEqp(String eqpId,String roleId){
        IotRoleEqp iotRoleEqp = new IotRoleEqp();
        iotRoleEqp.setEqpId(eqpId);
        iotRoleEqp.setRoleId(roleId);
        baseMapper.insert(iotRoleEqp);
    }
}
