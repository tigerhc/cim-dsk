package com.lmrj.core.sys.service;

import com.lmrj.common.mybatis.mvc.service.ITreeCommonService;
import com.lmrj.core.sys.entity.Organization;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @author jwcg
 * @date 2014-12-20 21:33:51
 * @version V1.0
 *
 */
public interface IOrganizationService extends ITreeCommonService<Organization> {
	/**
	 * 通过用户ID查找角色
	 */
	public List<Organization> findListByUserId(String userid);

	List<Organization> findChildren(String officeId);
	List<Organization> findStep(String userId);
	List<Organization> findYieldStep(String userId,String subLineNo);
}
