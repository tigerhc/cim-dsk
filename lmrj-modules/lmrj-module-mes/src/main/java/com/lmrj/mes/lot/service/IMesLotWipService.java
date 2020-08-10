package com.lmrj.mes.lot.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.mes.lot.entity.MesLotWip;
import com.lmrj.mes.track.entity.MesLotTrack;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.lot.service
 * @title: mes_lot_wip服务接口
 * @description: mes_lot_wip服务接口
 * @author: 张伟江
 * @date: 2020-08-05 10:32:55
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
public interface IMesLotWipService extends ICommonService<MesLotWip> {
    List<MesLotTrack> findIncompleteLotNo(Date startTime, Date endTime);

    MesLotWip finddata(String eqpId, String productionNo);

    List<MesLotWip> selectWip();

    String selectEndData(String lotNo, String productionNo);

    Boolean deleteEndData(String lotNo, String productionNo);

    MesLotWip findStep(String eqpId);

    List<Map> findLotYield(String line);
}