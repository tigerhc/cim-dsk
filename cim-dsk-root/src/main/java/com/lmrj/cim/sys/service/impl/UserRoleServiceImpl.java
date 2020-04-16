package com.lmrj.cim.sys.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.sys.entity.Role;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.cim.modules.sys.mapper.UserRoleMapper;
import com.lmrj.core.sys.service.IRoleService;
import com.lmrj.core.sys.service.IUserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("userRoleService")
public class UserRoleServiceImpl extends CommonServiceImpl<UserRoleMapper,UserRole> implements IUserRoleService {

    @Autowired
    private IRoleService roleService;

    @Override
    public void insert(String uid, String roleCode) {
        //设置用户角色(单位角色)
        EntityWrapper<Role> entityWrapper = new EntityWrapper<>();
        entityWrapper .eq("code",roleCode);
        Role role = roleService.selectOne(entityWrapper);
        if (role == null){
            throw new RuntimeException("该角色编码不存在");
        }
        String roleId = role.getId();
        insertByRoleId(uid,roleId);
    }

    @Override
    public void insertByRoleId(String uid, String roleId) {
        EntityWrapper<UserRole> entityWrapper = new EntityWrapper<>(UserRole.class);
        entityWrapper.eq("user_id",uid);
        entityWrapper.eq("role_id",roleId);
        int count = selectCount(entityWrapper);
        if(count == 0){
            UserRole userRole = new UserRole();
            userRole.setUserId(uid);
            userRole.setRoleId(roleId);
            insert(userRole);
        }
    }

    //删除用户角色
    @Override
    public void deleteUserRole(String uid, String roleId) {
        EntityWrapper<UserRole> entityWrapper = new EntityWrapper<>(UserRole.class);
        entityWrapper.eq("user_id",uid);
        entityWrapper.eq("role_id",roleId);
        int count = selectCount(entityWrapper);
        if(count > 0){
            delete(entityWrapper);
        }
    }
}
