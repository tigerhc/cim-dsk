package com.lmrj.cim.modules.sys.mapper;

import com.lmrj.core.sys.entity.SysProject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.bbs.mapper
 * @title: sys_project数据库控制层接口
 * @description: sys_project数据库控制层接口
 * @author: zwj
 * @date: 2019-05-28 07:13:47
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */
@Mapper
public interface SysProjectMapper extends BaseMapper<SysProject> {

     Map indexList();

    List<String> filterProject(String id);
}
