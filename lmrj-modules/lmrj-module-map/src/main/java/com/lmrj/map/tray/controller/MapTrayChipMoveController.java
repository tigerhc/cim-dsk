package com.lmrj.map.tray.controller;

import com.google.common.collect.Maps;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.entity.MesResult;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.map.tray.entity.MapTrayChipLog;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import com.lmrj.map.tray.service.IMapTrayChipLogService;
import com.lmrj.map.tray.service.IMapTrayChipMoveProcessService;
import com.lmrj.map.tray.util.TraceDateUtil;
import com.lmrj.map.tray.vo.MapTrayChipMoveQueryVo;
import com.lmrj.util.mapper.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private IMapTrayChipLogService mpTrayChipLogService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

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
        if(query.getChipIds()!=null && query.getChipIds().size()>0){
            query.setChipId(query.getLotNo()+";%"+query.getChipIds().get(0));
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

    /**
     * 查询晶圆轨迹
     *
     * @param id
     */
    @RequestMapping(value = "dmDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public Response dmDetail(@RequestParam String id) {
        List<Map<String, Object>> list = mapTrayChipMoveProcessService.dmDetail(id);
        return DateResponse.ok(list);
    }

    @RequestMapping(value = "traceData", method = {RequestMethod.GET, RequestMethod.POST})
    public Response pageList() {
        MapTrayChipLog traceLog = new MapTrayChipLog();
        try{
            //是否正在追溯中【正在追溯中的特点是，在log表中有一条记录只有开始时间，没有结束时间】
            Integer chkRunning = mapper.chkProcessRunning(TraceDateUtil.getChkTime(new Date(), -5));
            if(chkRunning == null || chkRunning==0){
                //设置为追溯中，即向log表中添加一个只有开始时间没有结束的时间的记录
                traceLog.setBeginTime(new Date());
                mpTrayChipLogService.insert(traceLog);
//                mapTrayChipMoveProcessService.traceData(traceLog, IMapTrayChipMoveProcessService.processErrDataFlag);
                mapTrayChipMoveProcessService.traceData(traceLog, IMapTrayChipMoveProcessService.processAsynchronous);
            }
        } catch (Exception e){
            e.printStackTrace();
            traceLog.setEndTime(new Date());
            mpTrayChipLogService.updateById(traceLog);
        }
        return Response.ok();
    }

    @RequestMapping(value = "getCommand",method = {RequestMethod.GET, RequestMethod.POST})
    public Response getCommand(@RequestParam String bcCode){
        Response rs = Response.ok();
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "TRAY_REUPLOAD");
        String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bcCode, JsonUtil.toJsonString(map));
        if (replyMsg != null) {
            MesResult result = JsonUtil.from(replyMsg, MesResult.class);
            if ("Y".equals(result.getFlag())) {
                rs.put("cnt", result.getContent());
                return rs;
            }
        }
        return Response.error("没有得到返回结果");
    }

    @RequestMapping(value = "getProductionParam", method = {RequestMethod.GET, RequestMethod.POST})
    public Response getProductionParam(@RequestParam String id){
        Response rs = Response.ok();
        Map<String, Object> productionParam = mapTrayChipMoveProcessService.getProductionParam(Long.parseLong(id));
        rs.put("paramObj", productionParam);
        return rs;
    }

    /** 追溯查询物料信息
     */
    @RequestMapping(value = "getPuctionGoods", method = {RequestMethod.GET, RequestMethod.POST})
    public Response getPuctionGoods(@RequestParam String startTime, String eqpId){
        Response rs = Response.ok();
        startTime = startTime.replace("T", " ");
        startTime = startTime.length()>19?startTime.substring(19):startTime;
        System.out.println(startTime+","+eqpId);
        return rs;
    }
}
