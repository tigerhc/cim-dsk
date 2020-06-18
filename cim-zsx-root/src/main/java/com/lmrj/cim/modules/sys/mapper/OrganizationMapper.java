package com.lmrj.cim.modules.sys.mapper;

import com.lmrj.common.mybatis.mvc.mapper.BaseTreeMapper;
import com.lmrj.core.sys.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select("select * from sys_organization where parent_ids like '%a04d30fbb9a545a5a7b3b7e1d6df8e71/%' and org_type='4' and del_flag = '0' order by sort_no")
	List<Organization> findStep(String userId);


}
