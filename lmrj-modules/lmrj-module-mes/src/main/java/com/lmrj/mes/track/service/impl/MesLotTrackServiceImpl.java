package com.lmrj.mes.track.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.entity.MesResult;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.mapper.MesLotTrackMapper;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.service.impl
 * @title: mes_lot_track服务实现
 * @description: mes_lot_track服务实现
 * @author: 张伟江
 * @date: 2020-04-28 14:03:17
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("mesLotTrackService")
@Slf4j
public class MesLotTrackServiceImpl extends CommonServiceImpl<MesLotTrackMapper, MesLotTrack> implements IMesLotTrackService {

    @Autowired
    IMesLotTrackLogService mesLotTrackLogService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void trackIn(String eqpId, String lotNo, String recipeCode, String opId) {
        List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo));
        MesLotTrack mesLotTrack = new MesLotTrack();
        if (mesLotTrackList.size() > 0) {
            mesLotTrack = mesLotTrackList.get(0);
            mesLotTrack.setStartTime(new Date());
            mesLotTrack.setCreateBy(opId);
        } else {
            mesLotTrack = new MesLotTrack();
            mesLotTrack.setEqpId(eqpId);
            mesLotTrack.setLotNo(lotNo);
            mesLotTrack.setCreateBy(opId);
            mesLotTrack.setStartTime(new Date());
        }
        this.insertOrUpdate(mesLotTrack);

        MesLotTrackLog mesLotTrackLog = new MesLotTrackLog();
        mesLotTrackLog.setEqpId(eqpId);
        mesLotTrackLog.setLotNo(lotNo);
        mesLotTrackLog.setCreateBy(opId);
        mesLotTrackLog.setEventCode("TRACKIN");
        mesLotTrackLogService.insert(mesLotTrackLog);

        //发送至EAP客户端 Map
        Map<String, String> map = Maps.newHashMap();
        map.put("LOT_ID", lotNo);
        map.put("EQP_ID", eqpId);
        String bc = "SH_FOL_OV1";
        if(eqpId.indexOf("OVEN")>=0 || eqpId.indexOf("CM-EC-")>=0){
            bc = "SH_FOL_OV1";
        }else if(eqpId.indexOf("-PC")>=0){
            bc = "BC3";
        }
        Object test=rabbitTemplate.convertSendAndReceive("S2C.T.CURE.COMMAND", bc, JsonUtil.toJsonString(map));
        byte[] message = (byte[]) test;
        String msg = null;
        try {
            msg = new String(message, "UTF-8");
            String msg2 = new String(message, "GBK");
            log.info("encode UTF-8: {}", msg);
            log.info("encode GBK: {}", msg2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // TODO: 2019/6/23 打印返回结果
        MesResult result = JsonUtil.from(msg, MesResult.class);
        if("Y".equals(result.flag)){
            //Map<String, String> content = Maps.newHashMap();
            //content.put("RECIPE_NAME", recipeName);
            //result.setContent(content);
            //简单处理
            //result.setContent(recipeName);
        }

        // TODO: 2019/6/23 判断结果,成功则更新设备状态表
        //return JsonUtil.toJsonString(result);
    }

    public void trackOut(String eqpId, String lotNo, String recipeCode, String opId) {
        List<MesLotTrack> mesLotTrackList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId).eq("lot_no", lotNo));
        MesLotTrack mesLotTrack = new MesLotTrack();
        if (mesLotTrackList.size() > 0) {
            mesLotTrack = mesLotTrackList.get(0);
            mesLotTrack.setEndTime(new Date());
            mesLotTrack.setCreateBy(opId);
        } else {
            mesLotTrack = new MesLotTrack();
            mesLotTrack.setEqpId(eqpId);
            mesLotTrack.setLotNo(lotNo);
            mesLotTrack.setCreateBy(opId);
            mesLotTrack.setEndTime(new Date());
        }
        this.insertOrUpdate(mesLotTrack);
        MesLotTrackLog mesLotTrackLog = new MesLotTrackLog();
        mesLotTrackLog.setEqpId(eqpId);
        mesLotTrackLog.setLotNo(lotNo);
        mesLotTrackLog.setCreateBy(opId);
        mesLotTrackLog.setEventCode("TRACKOUT");
        mesLotTrackLogService.insert(mesLotTrackLog);
    }
}
