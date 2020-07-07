package com.lmrj.oven.batchlot.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.edc.param.entity.EdcParamRecord;
import com.lmrj.edc.param.entity.EdcParamRecordDtl;
import com.lmrj.edc.param.service.IEdcParamRecordDtlService;
import com.lmrj.edc.param.service.IEdcParamRecordService;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.gzst.gov.cn
*
* @version V1.0
* @package com.lmrj.fab.service.impl
* @title: ovn_batch_lot服务实现
* @description: ovn_batch_lot服务实现
* @author: zhangweijiang
* @date: 2019-06-09 08:49:15
* @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
*/
@Service
public class OvnEdcHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IOvnBatchLotService ovnBatchLotService;
    @Autowired
    IOvnBatchLotParamService ovnBatchLotParamService;
    @Autowired
    IEdcEvtRecordService edcEvtRecordService;
    @Autowired
    IEdcAmsRecordService edcAmsRecordService;
    @Autowired
    IEdcParamRecordService edcParamRecordService;
    @Autowired
    IEdcParamRecordDtlService edcParamRecordDtlService;
    @Autowired
    IFabEquipmentStatusService fabEquipmentStatusService;

    //{"eqpId":"OVEN-F-01","eventId":"ON","eventParams":null,"startDate":"2019-11-12 19:31:33 416"}
    @RabbitHandler
    @RabbitListener(queues= {"C2S.Q.CURE.EVENT"})
    public void cureEvent(byte[] message) throws UnsupportedEncodingException {
        String msg = new String(message, "UTF-8");
        System.out.println("接收到的消息"+msg);
        EdcEvtRecord edcEvtRecord = JsonUtil.from(msg, EdcEvtRecord.class);
        edcEvtRecord.setCreateDate(new Date());
        edcEvtRecordService.insert(edcEvtRecord);
        if("TEMPERATURE-UPLOAD".equals(edcEvtRecord.getEventId())){
            //"20190623202600.csv"
            try {
                logger.info("开始解析{}温度曲线文件{}",edcEvtRecord.getEqpId(), edcEvtRecord.getEventParams());
                ovnBatchLotService.resolveTemperatureFile(edcEvtRecord.getEqpId(), edcEvtRecord.getEventParams());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @RabbitHandler
    @RabbitListener(queues= {"C2S.Q.CURE.ALARM"})
    public void cureAlarm(byte[] message) throws UnsupportedEncodingException {
        String msg = new String(message, "UTF-8");
        System.out.println("接收到的消息"+msg);
        EdcAmsRecord edcAmsRecord = JsonUtil.from(msg, EdcAmsRecord.class);
        edcAmsRecord.setCreateDate(new Date());
        edcAmsRecordService.insert(edcAmsRecord);
        fabEquipmentStatusService.updateStatus(edcAmsRecord.getEqpId(),"ALARM","", "");
    }

    @RabbitHandler
    @RabbitListener(queues= {"C2S.Q.CURE.PARAM"})
    public void cureParam(byte[] message) throws UnsupportedEncodingException {

        String msg = new String(message, "UTF-8");
        logger.info("接收到的消息"+msg);
        List<EdcParamRecord> list = JsonUtil.from(msg, new TypeReference<List<EdcParamRecord>>(){});
        logger.info("接收到的消息时间"+list.get(0).getBizDate());
        //插入新数据,同时备份一份到his表
        Map<String, String> statusMap = Maps.newHashMap();
        Map<String, String> lotMap = Maps.newHashMap();
        for(EdcParamRecord edcParamRecord : list){
            //先删除dtl旧数据
            edcParamRecordDtlService.deleteByEqp(edcParamRecord.getEqpId());
            edcParamRecord.setCreateBy("1");
            edcParamRecord.setBizType("2");
            edcParamRecord.setBizSubType("OVEN_REPORT");
            for(EdcParamRecordDtl temp: edcParamRecord.getEdcParamRecordDtlList()){
                temp.setEqpId(edcParamRecord.getEqpId());
                if("status".equals(temp.getParamCode())){
                    if(StringUtil.isNotEmpty(temp.getParamValue())){
                        statusMap.put(edcParamRecord.getEqpId(), temp.getParamValue().replaceAll(" ","").replaceAll("OFFLINE", "DOWN"));
                    }

                }
                if("lot".equals(temp.getParamCode())){
                    if(StringUtil.isNotEmpty(temp.getParamValue())){
                        lotMap.put(edcParamRecord.getEqpId(), temp.getParamValue().split(",")[0]);
                    }

                }
            }
            edcParamRecordService.insert(edcParamRecord);
            edcParamRecordDtlService.transfer2His(edcParamRecord.getEqpId());
        }

        statusMap.forEach((k,v)->{
            if(StringUtil.isNotEmpty(k)){
                fabEquipmentStatusService.updateStatus(k,v,"","");
            }

        });

        lotMap.forEach((k,v)->{
            if(StringUtil.isNotEmpty(k)){
                fabEquipmentStatusService.updateLot(k,v);
            }
        });
        ////更新status表
        //for(EdcParamRecord edcParamRecord : statusMap.keySet()){
        //    FabEquipmentStatus fabEquipmentStatus = new FabEquipmentStatus();
        //    fabEquipmentStatus.setEqpId(edcParamRecord.getEqpId());
        //    fabEquipmentStatus.setLotNo();
        //}


    }

    ////@RabbitHandler
    ////@RabbitListener(queues= {"S2C.Q.CURE.BC1.COMMAND"})
    //public String cureParam2(byte[] message){
    //    String msg = new String(message);
    //    System.out.println("接收到的消息"+msg);
    //    List<EdcParamRecord> list = JsonMapper.from(msg, new TypeReference<List<EdcParamRecord>>(){});
    //    //插入新数据,同时备份一份到his表
    //    for(EdcParamRecord edcParamRecord : list){
    //        //先删除dtl旧数据
    //        edcParamRecordDtlService.deleteByEqp(edcParamRecord.getEqpId());
    //        edcParamRecordService.insert(edcParamRecord);
    //        edcParamRecordService.transfer2His(edcParamRecord.getEqpId());
    //    }
    //    return "TEST2";
    //
    //}

    @RabbitHandler
    @RabbitListener(queues= {"S2C.T.CURE.COMMAND"})
    public String cureParam2(byte[] message) {
        String msg = new String(message);
        System.out.println("接收到的消息" + msg);
        List<EdcParamRecord> list = JsonUtil.from(msg, new TypeReference<List<EdcParamRecord>>() {
        });
        System.out.println(list);
        //插入新数据,同时备份一份到his表
//        for(EdcParamRecord edcParamRecord : list){
//            //先删除dtl旧数据
//            edcParamRecordDtlService.deleteByEqp(edcParamRecord.getEqpId());
//            edcParamRecordService.insert(edcParamRecord);
//            edcParamRecordService.transfer2His(edcParamRecord.getEqpId());
//        }
        return "RECIPE_NAME";
    }
}
