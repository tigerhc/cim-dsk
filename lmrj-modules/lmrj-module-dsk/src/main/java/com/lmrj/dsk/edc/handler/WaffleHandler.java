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
        try {
            List<Map<String, Object>> dataList = JsonUtil.from(dataJson, ArrayList.class);
            log.info("WaffleHandler_parseWaffleMove:find data in queue,count:"+dataList.size());
            int count = mapTrayChipMoveService.insertData(dataList);
            log.info("WaffleHandler_parseWaffleMove:count:"+count);
        }catch(Exception e){
            log.error("WaffleHandler_parseWaffleMove:find an error", e);
        }
    }

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.CHIP_ID_DATA"})
//   @RabbitListener(queues = {"QUEUE_JUNIT"})
    public void saveChipIdData(String dataJson) {
        try {
            List<Map<String, Object>> dataList = JsonUtil.from(dataJson, ArrayList.class);
            log.info("WaffleHandler_saveChipIdData:find data in queue,count:"+dataList.size());
            int count = mapTrayChipMoveService.insertChipIdData(dataList);
            log.info("WaffleHandler_saveChipIdData:count:"+count);
        }catch(Exception e){
            log.error("WaffleHandler_saveChipIdData:find an error",e);
        }
    }
}
