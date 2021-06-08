package com.lmrj.fab.userRole.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.userRole.entity.IotRole;
import com.lmrj.fab.userRole.mapper.IotRoleMapper;
import com.lmrj.fab.userRole.service.IIotRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wdj
 * @date 2021-06-05 10:12
 */
@Transactional
@Service("iotroleservice")
public class IotRoleServiceImpl extends CommonServiceImpl<IotRoleMapper, IotRole> implements IIotRoleService {
}
