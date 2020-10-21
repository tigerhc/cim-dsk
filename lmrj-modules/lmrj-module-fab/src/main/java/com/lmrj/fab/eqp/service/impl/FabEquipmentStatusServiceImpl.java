package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.mapper.FabEquipmentMapper;
import com.lmrj.fab.eqp.mapper.FabEquipmentStatusMapper;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.fab.1.service.impl
* @title: fab_equipment_status服务实现
* @description: fab_equipment_status服务实现
* @author: zhangweijiang
* @date: 2019-06-18 20:41:20
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("fabEquipmentStatusService")
public class FabEquipmentStatusServiceImpl  extends CommonServiceImpl<FabEquipmentStatusMapper,FabEquipmentStatus> implements  IFabEquipmentStatusService {

    @Autowired
    private FabEquipmentMapper fabEquipmentMapper;

    @Override
    public int updateStatus(String eqpID,String status, String lotNo, String recipeCode) {
        return baseMapper.updateStatus(eqpID,status, lotNo, recipeCode);
    }

    @Override
    public int updateYield(String eqpID, String status, String lotNo, String recipeCode, int lotYield, int dayYield) {
        return baseMapper.updateYield(eqpID,status, lotNo, recipeCode, lotYield, dayYield);
    }
    @Override
    public int increaseYield(String eqpID, int increasedYield) {
        return baseMapper.increaseYield(eqpID, increasedYield);
    }


    @Override
    public int updateLot(String eqpID,String lotId) {
        return baseMapper.updateLot(eqpID,lotId);
    }

    /**
     * 新建状态信息,若多条数据,则删除多余数据.保证一个设备只有一条状态数据
     * @param idList
     * @return
     */
    @Override
    public boolean initStatus(List idList){
        List<FabEquipment> fabEquipmentList = fabEquipmentMapper.selectBatchIds(idList);
        for(FabEquipment fabEquipment : fabEquipmentList){
            String eqpId = fabEquipment.getEqpId();
            List<FabEquipmentStatus> fabEquipmentStatusList = baseMapper.selectList(new EntityWrapper().eq("EQP_ID", eqpId));
            if(fabEquipmentStatusList.size() == 0){
                FabEquipmentStatus fabEquipmentStatus = new FabEquipmentStatus();
                fabEquipmentStatus.setEqpId(eqpId);
                fabEquipmentStatus.setEqpStatus("init");
                fabEquipmentStatus.setCreateDate(new Date());
                fabEquipmentStatus.setCreateBy("system");
                baseMapper.insert(fabEquipmentStatus);
            }else if(fabEquipmentStatusList.size() > 1){
                for(int i=1; i<fabEquipmentStatusList.size(); i++){
                    baseMapper.deleteById(fabEquipmentStatusList.get(i).getId());
                }
            }
        }
        return true;
    }

    @Override
    public List<FabEquipmentStatus> selectEqpStatus(String officeId, String lineNo, String fab){
        return baseMapper.selectEqpStatus(officeId, null, null);
    }

    @Override
    public List<Map> selectEqpStatusChart() {
        //List<Map> list = baseMapper.selectEqpStatusChart();
        // TODO: 2020/5/8 尽量补齐其他状态,若状态不存在,则补0
        return baseMapper.selectEqpStatusChart();
    }
    @Override
    public List<Map> selectYield(String lineNo){
        List<Map> yields =  baseMapper.selectYield(lineNo);
        List<Map> wips =  baseMapper.selectLotwip(lineNo);
        Map wipMap = Maps.newHashMap();
        for (Map wip : wips) {
            wipMap.put(wip.get("station_code"),wip.get("count")+"|"+wip.get("lot_yield") );
        }
        for (Map yield : yields) {
            String count = (String) wipMap.get(yield.get("step_code"));
            if(count == null){
                yield.put("waitwip", "0-0");
            }else{
                yield.put("waitwip", count);
            }
        }
        return yields;
    }

    @Override
    public FabEquipmentStatus findByEqpId(String eqpId) {
        List<FabEquipmentStatus> fabEquipmentStatusList = this.selectList(new EntityWrapper().eq("EQP_ID", eqpId));
        if(fabEquipmentStatusList.size() == 0){
            return null;
        }
        return fabEquipmentStatusList.get(0);
    }

}
