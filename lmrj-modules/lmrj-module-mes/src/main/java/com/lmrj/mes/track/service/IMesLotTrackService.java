package com.lmrj.mes.track.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.entity.MesResult;
import com.lmrj.mes.track.entity.MesLotTrack;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.service
 * @title: mes_lot_track服务接口
 * @description: mes_lot_track服务接口
 * @author: 张伟江
 * @date: 2020-04-28 14:03:17
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
public interface IMesLotTrackService extends ICommonService<MesLotTrack> {
    MesResult trackin(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String recipeCode, String opId);

    MesResult trackout(String eqpId, String productionNo, String productionName, String orderNo, String lotNo, String yield, String recipeCode, String opId);

    MesResult findRecipeName(String eqpId, String opId);

    MesResult findTemp(String eqpId, String opId);

    MesResult findParam(String eqpId, String param, String opId, String lotNo, String productionNo);

    MesLotTrack findLotNo(String startTime, String eqpId);

    MesLotTrack findNextStartTime(String endTime, String eqpId);

    List<MesLotTrack> findDataLotNo(String eqpId, Date startTime, Date endTime);

    MesLotTrack findLotNo1(String eqpId,Date startTime);

    Boolean updateTrackLotYeildEqp(String eqpId,String lotNo,Integer lotYieldEqp);

    MesLotTrack selectEndTime(String eqpId,String lotNo);

    List<MesLotTrack> findCorrectData(Date startTime, Date endTime);

    MesLotTrack findLotByStartTime(String eqpId,Date startTime);

    MesLotTrack findNoEndLotNo(String eqpId,Date startTime);

    MesLotTrack findLotTrack(String eqpId,String lotNo,String productionNo);

    MesLotTrack findLastTrack(String eqpId,String lotNo,Date startTime);

    List<Map> lotTrackQuery(String lineNo, String startTime, String endTime);

    List<Map<String, Object>> kongDongBar(String lotNo, String productionNo, String startDate, String endDate);
    Map<String, Object> chartKongDong(String productionNo, String startDate, String endDate);
    List<Map<String, Object>> getkongDongConfig(String productionNo);

    List<String> findAllProName(String proName);

    String getKeyence(String mode,String lotNo,String production);

    List<MesLotTrack> findLotsByTime(String eqpId, Date startTime, Date endTime);
}
