package com.lmrj.dsk.edc.handler;

import com.lmrj.dsk.eqplog.service.IChipMoveService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WaffleHandler {

    @Autowired
    private IChipMoveService mapTrayChipMoveService;

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.CHIP_MOVE"})
    public void parseWaffleMove(String dataJson) {
        log.info("WaffleHandler_parseWaffleMove:find data in queue");
        try {
            List<Map<String, Object>> dataList = JsonUtil.from(dataJson, ArrayList.class);
            int count = mapTrayChipMoveService.insertData(dataList);
            log.info("WaffleHandler_parseWaffleMove:count:"+count);
        }catch(Exception e){
            log.error("WaffleHandler_parseWaffleMove:find an error");
        }
    }
}
