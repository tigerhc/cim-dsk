package com.lmrj.cim.modules.sys.service.impl;

import com.lmrj.cim.modules.sys.service.ISysProjectService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.entity.SysProject;
import com.lmrj.cim.modules.sys.mapper.SysProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.bbs.service.impl
* @title: sys_project服务实现
* @description: sys_project服务实现
* @author: zwj
* @date: 2019-05-28 07:13:47
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
@Transactional
@Service("sysprojectService")
public class SysProjectServiceImpl  extends CommonServiceImpl<SysProjectMapper,SysProject> implements ISysProjectService {

    @Autowired
    private SysProjectMapper sysProjectMapper;
    @Override
    public Map indexList() {
        return sysProjectMapper.indexList();
    }

    @Override
    public List<String> filterProject(String id) {
        return sysProjectMapper.filterProject(id);
    }

}
