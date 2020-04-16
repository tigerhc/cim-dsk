package com.lmrj.cim.sys.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.TreeCommonServiceImpl;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.cim.modules.sys.mapper.OrganizationMapper;
import com.lmrj.core.sys.service.IOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("organizationService")
public class OrganizationServiceImpl extends TreeCommonServiceImpl<OrganizationMapper, Organization>
		implements IOrganizationService {

	@Override
	public List<Organization> findListByUserId(String userid) {
		return baseMapper.findListByUserId(userid);
	}

}
