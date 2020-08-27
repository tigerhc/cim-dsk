package com.lmrj.map.tray.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.mapper.MapEquipmentConfigMapper;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.controller
 * @title: map_equipment_config控制器
 * @description: map_equipment_config控制器
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("map/mapeqpconfig")
@ViewPrefix("map/mapeqpconfig")
@RequiresPathPermission("map:mapeqpconfig")
@LogAspectj(title = "map_equipment_config")
public class MapEquipmentConfigController extends BaseCRUDController<MapEquipmentConfig> {

    @Autowired
    private MapEquipmentConfigMapper mapper;

    @RequestMapping(value = "initAll", method = {RequestMethod.GET, RequestMethod.POST})
    public Response initAll() {
        mapper.initAll();
        return DateResponse.ok(null);
    }

}