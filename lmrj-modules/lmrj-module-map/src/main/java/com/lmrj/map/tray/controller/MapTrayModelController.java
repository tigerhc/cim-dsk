package com.lmrj.map.tray.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.map.tray.entity.MapTrayModel;
import com.lmrj.map.tray.service.IMapTrayConfigService;
import com.lmrj.map.tray.service.IMapTrayModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.controller
 * @title: map_tray_model控制器
 * @description: map_tray_model控制器
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("map/maptraytype")
@ViewPrefix("map/maptraytype")
@RequiresPathPermission("map:maptraytype")
@LogAspectj(title = "map_tray_type")
public class MapTrayModelController extends BaseCRUDController<MapTrayModel> {
    @Autowired
    private IMapTrayModelService trayModelService;
    @Autowired
    private IMapTrayConfigService trayConfigService;

    @RequestMapping("getModelsBySelect")
    public Response getModelsBySelect(){
        Response rs = Response.ok();
        rs.putList("models", trayModelService.getBySelect());
        return rs;
    }

    @RequestMapping("getTrayEqpList")
    public Response getTrayEqpList(){
        Response rs = Response.ok();
        rs.putList("eqps", trayConfigService.getAllTrayEqp());
        return rs;
    }
}
