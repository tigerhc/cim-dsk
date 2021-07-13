package com.lmrj.fab.userRole.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.fab.userRole.entity.FabUserRole;
import com.lmrj.fab.userRole.mapper.FabUserRoleMapper;
import com.lmrj.fab.userRole.service.IFabUserRoleService;
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
public class FabUserRoleServiceImpl extends CommonServiceImpl<FabUserRoleMapper, FabUserRole> implements IFabUserRoleService {
    @Override
    public List<String> getRoleByUserId(String userId) {
        List<String> retList = new ArrayList<>();
     List<FabUserRole> iotRoleUserList =   baseMapper.selectList(new EntityWrapper<FabUserRole>().eq("user_id",userId));
        for (FabUserRole iotRoleUser:iotRoleUserList
             ) {
            retList.add(iotRoleUser.getRoleId());

        }
     return retList;
    }


    @Override
    public void insertByRoleId(String uid, String roleId) {
        EntityWrapper<FabUserRole> entityWrapper = new EntityWrapper<>(FabUserRole.class);
        entityWrapper.eq("user_id",uid);
        entityWrapper.eq("role_id",roleId);
        int count = selectCount(entityWrapper);
        if(count == 0){
            FabUserRole userRole = new FabUserRole();
            userRole.setUserId(uid);
            userRole.setRoleId(roleId);
            insert(userRole);
        }
    }
}
