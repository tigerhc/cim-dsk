package com.lmrj.cim.sys.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.sys.entity.SysProject;

import java.util.List;
import java.util.Map;

/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.bbs.service
* @title: sys_project服务接口
* @description: sys_project服务接口
* @author: zwj
* @date: 2019-05-28 07:13:47
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
public interface ISysProjectService extends ICommonService<SysProject> {

    Map indexList();

    List<String> filterProject(String id);


}
