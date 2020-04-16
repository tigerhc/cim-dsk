package com.lmrj.cim.sys.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.entity.UserOrganization;
import com.lmrj.cim.modules.sys.mapper.OrganizationMapper;
import com.lmrj.cim.modules.sys.mapper.UserOrganizationMapper;
import com.lmrj.cim.sys.service.IUserOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service("userOrganizationService")
public class UserOrganizationServiceImpl extends CommonServiceImpl<UserOrganizationMapper, UserOrganization>
		implements IUserOrganizationService {

	@Autowired
	private OrganizationMapper organizationMapper;
	@Override
	public List<String> listOrgIds(String orgid) {
		return organizationMapper.listOrgIds(orgid);
	}
}
