package com.lmrj.map.tray.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.map.tray.entity.MapTrayConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.controller
 * @title: map_tray_config控制器
 * @description: map_tray_config控制器
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("map/maptrayconfig")
@ViewPrefix("map/maptrayconfig")
@RequiresPathPermission("map:maptrayconfig")
@LogAspectj(title = "map_tray_config")
public class MapTrayConfigController extends BaseCRUDController<MapTrayConfig> {

}