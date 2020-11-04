package com.lmrj.dsk.edc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EdcStateHandler {

    @Autowired
    IFabEquipmentService fabEquipmentService;

    @Autowired
    IFabEquipmentStatusService fabEquipmentStatusService;

    @Autowired
    private IEdcEqpStateService edcEqpStateService;

    @Autowired
    IEdcEqpStateService iEdcEqpStateService;
    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.STATE.DATA"})
    public void handleAlarm(String msg) {
        log.info("C2S.Q.STATE.DATA接收到的消息" + msg);
        List<EdcEqpState> edcEqpStateList = JsonUtil.from(msg,new TypeReference<List<EdcEqpState>>() {
        });
        if(edcEqpStateList.size()>1){
            List<EdcEqpState> eqpStateList = Lists.newArrayList();
            for (int i = 0; i < edcEqpStateList.size()-1; i++) {
                EdcEqpState edcEqpState = edcEqpStateList.get(i);
                EdcEqpState nextEqpState = edcEqpStateList.get(i+1);
                if(edcEqpState.getStartTime().before(nextEqpState.getStartTime())){
                    edcEqpState.setEndTime(nextEqpState.getStartTime());
                    Double state = (double) (nextEqpState.getStartTime().getTime() - edcEqpState.getStartTime().getTime());
                    edcEqpState.setStateTimes(state);
                    eqpStateList.add(edcEqpState);
                }
            }
            if(eqpStateList.size()>0){
                iEdcEqpStateService.insertBatch(eqpStateList);
            }
            fabEquipmentStatusService.updateStatus(edcEqpStateList.get(edcEqpStateList.size()-1).getEqpId(),edcEqpStateList.get(edcEqpStateList.size()-1).getState(), "", "");
        }
        //设置上一条数据的结束时间，并计算持续时间
        /*if(!"ALARM".equals(edcEqpState.getState())){
            EdcEqpState lastedcEqpState = iEdcEqpStateService.findLastData(edcEqpState.getStartTime(),edcEqpState.getEqpId());
            lastedcEqpState.setEndTime(edcEqpState.getStartTime());
            Double state = (double) (edcEqpState.getStartTime().getTime() - lastedcEqpState.getStartTime().getTime());
            lastedcEqpState.setStateTimes(state);
            edcEqpStateService.updateById(lastedcEqpState);
        }*/
        //修改设备实时状态
    }
}
