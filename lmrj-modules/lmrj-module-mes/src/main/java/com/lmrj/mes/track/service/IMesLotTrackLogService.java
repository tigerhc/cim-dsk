package com.lmrj.mes.track.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.mes.track.entity.MesLotTrackLog;

import java.util.Date;
import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.mes.track.service
* @title: mes_lot_track_log服务接口
* @description: mes_lot_track_log服务接口
* @author: 张伟江
* @date: 2020-04-28 14:03:29
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IMesLotTrackLogService extends ICommonService<MesLotTrackLog> {

    List<MesLotTrackLog> findLatestLotEqp(Date startTime);

    List<MesLotTrackLog> findTrackLog(Date startTime);
}
