package com.lmrj.fab.userRole.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.userRole.entity.FabRole;
import com.lmrj.fab.userRole.mapper.FabRoleMapper;
import com.lmrj.fab.userRole.service.IFabRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wdj
 * @date 2021-06-05 10:12
 */
@Transactional
@Service("iotroleservice")
public class FabRoleServiceImpl extends CommonServiceImpl<FabRoleMapper, FabRole> implements IFabRoleService {
}
