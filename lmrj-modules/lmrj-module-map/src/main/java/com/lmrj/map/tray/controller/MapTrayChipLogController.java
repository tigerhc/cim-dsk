package com.lmrj.map.tray.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.map.tray.entity.MapTrayChipLog;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.controller
 * @title: map_tray_chip_log控制器
 * @description: map_tray_chip_log控制器
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("map/maptraychiplog")
@ViewPrefix("map/maptraychiplog")
@RequiresPathPermission("map:maptraychiplog")
@LogAspectj(title = "map_tray_chip_log")
public class MapTrayChipLogController extends BaseCRUDController<MapTrayChipLog> {

}