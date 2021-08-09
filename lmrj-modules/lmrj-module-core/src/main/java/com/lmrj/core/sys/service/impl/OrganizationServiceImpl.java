package com.lmrj.core.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.TreeCommonServiceImpl;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.core.sys.mapper.OrganizationMapper;
import com.lmrj.core.sys.service.IOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("organizationService")
public class OrganizationServiceImpl extends TreeCommonServiceImpl<OrganizationMapper, Organization>
		implements IOrganizationService {

	@Override
	public List<Organization> findListByUserId(String userid) {
		return baseMapper.findListByUserId(userid);
	}

	@Override
	public List<Organization> findChildren(String officeId) {
		return this.selectList(new EntityWrapper<Organization>().eq("parent_id",officeId));
	}
	@Override
	public List<Organization> findStep(String userId){
		return baseMapper.findStep(userId);
	}
	@Override
	public List<Organization> findYieldStep(String userId,String wipSubLineNo){
		List<Organization> organizations = new ArrayList<>();
		String publicSubLineNo = null;
		if("IGBT".equals(wipSubLineNo) || "FRD".equals(wipSubLineNo)){
			publicSubLineNo = "HB1";
		}else if("DBCT".equals(wipSubLineNo) || "DBCB".equals(wipSubLineNo)){
			publicSubLineNo = "HB2";
		}
		if(publicSubLineNo != null){
			organizations = baseMapper.findYieldStep(userId,publicSubLineNo);
		}
		if("SIM".equals(userId)){
			wipSubLineNo = "SIM";
		}
		List<Organization> organizationList = baseMapper.findYieldStep(userId,wipSubLineNo);
		if(organizations.size()>0){
			for (Organization organization : organizations) {
				organizationList.add(organization);
			}
		}
		return organizationList;
	}

}
