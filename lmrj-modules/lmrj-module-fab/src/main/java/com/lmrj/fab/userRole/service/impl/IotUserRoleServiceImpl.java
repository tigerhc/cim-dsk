package com.lmrj.fab.userRole.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.fab.userRole.entity.IotUserRole;
import com.lmrj.fab.userRole.mapper.IotUserRoleMapper;
import com.lmrj.fab.userRole.service.IIotUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:12
 */
@Transactional
@Service("iotuserRoleService")
public class IotUserRoleServiceImpl extends CommonServiceImpl<IotUserRoleMapper, IotUserRole> implements IIotUserRoleService {
    @Override
    public List<String> getRoleByUserId(String userId) {
        List<String> retList = new ArrayList<>();
     List<IotUserRole> iotRoleUserList =   baseMapper.selectList(new EntityWrapper<IotUserRole>().eq("user_id",userId));
        for (IotUserRole iotRoleUser:iotRoleUserList
             ) {
            retList.add(iotRoleUser.getRoleId());

        }
     return retList;
    }


    @Override
    public void insertByRoleId(String uid, String roleId) {
        EntityWrapper<IotUserRole> entityWrapper = new EntityWrapper<>(IotUserRole.class);
        entityWrapper.eq("user_id",uid);
        entityWrapper.eq("role_id",roleId);
        int count = selectCount(entityWrapper);
        if(count == 0){
            IotUserRole userRole = new IotUserRole();
            userRole.setUserId(uid);
            userRole.setRoleId(roleId);
            insert(userRole);
        }
    }
}
