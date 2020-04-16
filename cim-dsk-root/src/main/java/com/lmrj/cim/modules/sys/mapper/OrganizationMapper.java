package com.lmrj.cim.modules.sys.mapper;

import java.util.List;

import com.lmrj.common.mybatis.mvc.mapper.BaseTreeMapper;
import com.lmrj.core.sys.entity.Organization;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrganizationMapper extends BaseTreeMapper<Organization> {

	/**
	 *
	 * @title: findListByUserId
	 * @description: 通过用户查找组织机构
	 * @param userId
	 * @return
	 * @return: List<Organization>
	 */
	List<Organization> findListByUserId(String userId);

    List<String> listOrgIds(String orgid);
}
