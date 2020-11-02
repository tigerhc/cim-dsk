package com.lmrj.map.tray.controller;

import java.util.List;
import java.util.Map;

import com.lmrj.map.tray.service.IMapTrayChipMoveProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import com.lmrj.map.tray.vo.MapTrayChipMoveQueryVo;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.controller
 * @title: map_tray_chip_move控制器
 * @description: map_tray_chip_move控制器
 * @author: stev
 * @date: 2020-08-02 15:31:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("map/maptraychipmove")
@ViewPrefix("map/maptraychipmove")
@RequiresPathPermission("map:maptraychipmove")
@LogAspectj(title = "map_tray_chip_move")
public class MapTrayChipMoveController {
    @Autowired
    private IMapTrayChipMoveProcessService mapTrayChipMoveProcessService;

    @Autowired
    private MapTrayChipMoveMapper mapper;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param query
     */
    @RequestMapping(value = "page", method = {RequestMethod.GET, RequestMethod.POST})
    public Response pageList(MapTrayChipMoveQueryVo query) {
        if (query == null) {
            query = new MapTrayChipMoveQueryVo();
        }
        int total = query.getTotal();
        if (total <= 0) {
            total = mapper.countChipMove(query);
        }
        List<Map<String, Object>> list = null;
        if (total > 0) {
            list = mapper.queryChipMove(query);
        }
        Response resp = DateResponse.ok(list);
        resp.put("total", total);
        return resp;
    }

    /**
     * 查询芯片轨迹
     *
     * @param chipId
     */
    @RequestMapping(value = "moveDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public Response pageList(@RequestParam String chipId) {
        List<Map<String, Object>> list = mapper.queryChip(chipId);
        return DateResponse.ok(list);
    }

    @RequestMapping(value = "traceData", method = {RequestMethod.GET, RequestMethod.POST})
    public Response pageList() {
        mapTrayChipMoveProcessService.traceData();
        return Response.ok();
    }
}
