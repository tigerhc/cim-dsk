package com.lmrj.mes.track.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.track.service.IMesLotTrackLogService;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import com.lmrj.mes.track.mapper.MesLotTrackLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

}