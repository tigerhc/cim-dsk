package com.lmrj.cim.modules.sys.service.impl;

import com.lmrj.cim.modules.sys.mapper.UserMapper;
import com.lmrj.cim.modules.sys.service.IUserRoleServiceTest;
import com.lmrj.core.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceTestImpl implements IUserRoleServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectTestPeople() {
        List<User> users = userMapper.selectTestPeople();
        return users;
    }
}
