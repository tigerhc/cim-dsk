package com.lmrj.mes.lot.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.lot.entity.MesLotWip;
import com.lmrj.mes.lot.mapper.MesLotWipMapper;
import com.lmrj.mes.lot.service.IMesLotWipService;
import com.lmrj.mes.track.entity.MesLotTrack;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.lot.service.impl
 * @title: mes_lot_wip服务实现
 * @description: mes_lot_wip服务实现
 * @author: 张伟江
 * @date: 2020-08-05 10:32:55
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("mesLotWipService")
public class MesLotWipServiceImpl extends CommonServiceImpl<MesLotWipMapper, MesLotWip> implements IMesLotWipService {
    @Override
    public int findSortNo(String eqpId){
        return baseMapper.findSortNo(eqpId);
    }
    @Override
    public int findDayLotYield( String eqpId, Date startTime, Date endTime){
        return baseMapper.findDayLotYield(eqpId,startTime,endTime);
    }

    @Override
    public MesLotTrack findWByYield(String eqpId, String lotNo, String productionNo){
        return baseMapper.findWByYield(eqpId,lotNo,productionNo);
    }

    @Override
    public List<MesLotTrack> findIncompleteLotNo(Date startTime, Date endTime) {
        return baseMapper.findIncompleteLotNo(startTime, endTime);
    }

    @Override
    public String selectEndData(String lotNo, String productionNo) {
        return baseMapper.selectEndData(lotNo, productionNo);
    }

    @Override
    public Boolean deleteEndData(String lotNo, String productionNo) {
        return baseMapper.deleteEndData(lotNo, productionNo);
    }

    @Override
    public List<MesLotWip> selectWip() {
        return baseMapper.selectWip();
    }

    @Override
    public MesLotWip findStep(String eqpId) {
        return baseMapper.findStep(eqpId);
    }
    @Override
    public List<Map> findLotYield(String line) {
        List<Map> yieldList = baseMapper.findLotYield(line);
        return yieldList;
    }

    @Override
    public MesLotWip finddata(String lotNo, String productionNo) {
        return baseMapper.finddata(lotNo, productionNo);
    }

    public boolean updateLotYieldEqp(MesLotTrack mesLotTrack){
        boolean flag=false;


        return false;
    }
}