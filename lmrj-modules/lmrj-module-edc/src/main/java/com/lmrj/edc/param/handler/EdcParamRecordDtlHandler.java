package com.lmrj.edc.param.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.lmrj.edc.param.entity.EdcParamRecord;
import com.lmrj.edc.param.entity.EdcParamRecordDtl;
import com.lmrj.edc.param.service.IEdcParamRecordDtlService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service
public class EdcParamRecordDtlHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IEdcParamRecordDtlService edcParamRecordDtlService;

    @RabbitHandler
    @RabbitListener(queues= {"C2S.Q.EDC.PARAMDTL"})
    public void cureParam(String msg) throws UnsupportedEncodingException {

        //String msg = new String(message, "UTF-8");
        logger.info("接收到的消息"+msg);
        List<EdcParamRecordDtl> list = JsonUtil.from(msg, new TypeReference<List<EdcParamRecordDtl>>(){});
        logger.info("接收到的消息时间"+list.get(0).getCreateDate());
        //插入新数据,同时备份一份到his表
        Map<String, String> statusMap = Maps.newHashMap();
        Map<String, String> lotMap = Maps.newHashMap();
        for(EdcParamRecordDtl edcParamRecord : list){
            //先删除dtl旧数据
            edcParamRecordDtlService.deleteByEqp(edcParamRecord.getEqpId());
            edcParamRecord.setCreateBy("1");
            edcParamRecordDtlService.transfer2His(edcParamRecord.getEqpId());
            //插入数据
            edcParamRecordDtlService.insert(edcParamRecord);
        }
    }
}
