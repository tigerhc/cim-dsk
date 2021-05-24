package com.lmrj.core.userRole.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.userRole.entity.IotRoleUser;
import com.lmrj.core.userRole.mapper.IotUserRoleMapper;
import com.lmrj.core.userRole.service.IIotUserRoleService;
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
public class IotUserRoleServiceImpl extends CommonServiceImpl<IotUserRoleMapper, IotRoleUser> implements IIotUserRoleService {
    @Override
    public List<String> getRoleByUserId(String userId) {
        List<String> retList = new ArrayList<>();
     List<IotRoleUser> iotRoleUserList =   baseMapper.selectList(new EntityWrapper<IotRoleUser>().eq("user_id",userId));
        for (IotRoleUser iotRoleUser:iotRoleUserList
             ) {
            retList.add(iotRoleUser.getRoleId());

        }
     return retList;
    }
}
