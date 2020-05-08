package com.lmrj.mes.track.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.entity.MesResult;
import com.lmrj.mes.track.entity.MesLotTrack;

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
    MesResult trackin4DSK(String eqpId, String productionName, String productionNo, String orderNo, String lotNo, String recipeCode, String opId);

    MesResult trackout4DSK(String eqpId, String productionName, String productionNo, String orderNo, String lotNo, String yield, String recipeCode, String opId);
    //MesResult trackIn(String eqpId, String lotNo, String recipeCode, String opId);
}
