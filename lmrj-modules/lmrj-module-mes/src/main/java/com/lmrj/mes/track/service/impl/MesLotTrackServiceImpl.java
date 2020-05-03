package com.lmrj.mes.track.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.track.entity.MesLotTrack;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.mapper.MesLotTrackMapper;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.service.IMesLotTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


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
public class MesLotTrackServiceImpl extends CommonServiceImpl<MesLotTrackMapper, MesLotTrack> implements IMesLotTrackService {

    @Autowired
    IMesLotTrackLogService mesLotTrackLogService;
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
        System.out.println("trackIn");
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
