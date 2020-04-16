package com.lmrj.core.sys.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.sys.entity.SysConfig;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.sys.config.controller
 * @title: sys_config控制器
 * @description: sys_config控制器
 * @author: zhangweijiang
 * @date: 2019-07-14 03:03:35
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("sys/sysconfig")
@ViewPrefix("sys/sysconfig")
@RequiresPathPermission("sys:sysconfig")
@LogAspectj(title = "sys_config")
public class SysConfigController extends BaseCRUDController<SysConfig> {

    @GetMapping("/{key}/getByKey")
    public String getCurrentUserProject(@PathVariable("key") String key){
        List<SysConfig> sysConfigList=commonService.selectList(new EntityWrapper<SysConfig>().eq("CONFIG_KEY", key));
        return sysConfigList.get(0).getConfigValue();
        //FastJsonUtils.print(sysConfigList.get(0).getConfigValue());
    }

}
