package com.lmrj.mes.track.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.mapper.MesLotTrackLogMapper;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.mes.track.service.impl
* @title: mes_lot_track_log服务实现
* @description: mes_lot_track_log服务实现
* @author: 张伟江
* @date: 2020-04-28 14:03:29
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("mesLotTrackLogService")
public class MesLotTrackLogServiceImpl  extends CommonServiceImpl<MesLotTrackLogMapper,MesLotTrackLog> implements  IMesLotTrackLogService {
    @Override
    public List<MesLotTrackLog> findLatestLotEqp(Date startTime){
        return baseMapper.findLatestLotEqp(startTime);
    }

    @Override
    public List<MesLotTrackLog> findTrackLog(Date startTime){
        return baseMapper.findTrackLog(startTime);
    }
}
