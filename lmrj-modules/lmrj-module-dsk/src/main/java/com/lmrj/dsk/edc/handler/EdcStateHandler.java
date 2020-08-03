package com.lmrj.dsk.edc.handler;

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

@Service
@Slf4j
public class EdcStateHandler {

    @Autowired
    IFabEquipmentService fabEquipmentService;

    @Autowired
    IFabEquipmentStatusService fabEquipmentStatusService;

    @Autowired
    private IEdcEqpStateService edcEqpStateService;

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.STATE.DATA"})
    public void handleAlarm(String msg) {
        log.info("C2S.Q.STATE.DATA接收到的消息" + msg);
        EdcEqpState edcEqpState = JsonUtil.from(msg, EdcEqpState.class);
        edcEqpStateService.insert(edcEqpState);
        fabEquipmentStatusService.updateStatus(edcEqpState.getEqpId(),edcEqpState.getState(), "", "");
    }

}
