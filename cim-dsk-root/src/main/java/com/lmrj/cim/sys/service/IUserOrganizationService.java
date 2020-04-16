package com.lmrj.cim.sys.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.sys.entity.UserOrganization;

import java.util.List;

public interface IUserOrganizationService extends ICommonService<UserOrganization> {

    List<String> listOrgIds(String orgid);
}
