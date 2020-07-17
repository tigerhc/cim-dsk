package com.lmrj.ms.handler;

import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.ms.record.entity.MsMeasureRecordDetail;
import com.lmrj.ms.record.service.IMsMeasureRecordService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class EdcMeasureHandler {

    @Autowired
    IMsMeasureRecordService msMeasureRecordService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IApsPlanPdtYieldService apsPlanPdtYieldService;


    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.MEASURE.DATA"})
    public void parseWeightlog(String msg) {
        log.info("recieved message 开始解析{}量测数据 : {} " + msg);
        MsMeasureRecord msMeasureRecord = JsonUtil.from(msg, MsMeasureRecord.class);
        if(msMeasureRecord == null){
            return;
        }

        String name=apsPlanPdtYieldService.findProductionName(msMeasureRecord.getProductionNo());
        msMeasureRecord.setProductionName(name);
        //  msMeasureRecordService.updateById(msMeasureRecord);
      //  ArrayList<MsMeasureRecord> mm=new ArrayList<>();

      //  mm.add(msMeasureRecord);


        String recordId = StringUtil.randomTimeUUID("MS");
        msMeasureRecord.setRecordId(recordId);
        msMeasureRecord.setApproveResult("Y");
        if(StringUtil.isBlank(msMeasureRecord.getStatus())){
            msMeasureRecord.setStatus("1");
        }
        for (MsMeasureRecordDetail msMeasureRecordDetail : msMeasureRecord.getDetail()) {
            if(msMeasureRecordDetail.getItemResult().contains("N")){
                msMeasureRecord.setApproveResult("N");
                break;
            }
        }

       // msMeasureRecordService.updateBatchById(mm);


        msMeasureRecordService.insert(msMeasureRecord);
    }

    @RabbitHandler
    @RabbitListener(queues = {"zx_test"})
    public void updateMs(String msg){
        MsMeasureRecord msMeasureRecord = JsonUtil.from(msg, MsMeasureRecord.class);
        System.out.println("123:"+msMeasureRecord);
        System.out.println("456"+msMeasureRecord.getProductionNo());
     String name=apsPlanPdtYieldService.findProductionName(msMeasureRecord.getProductionNo());
        System.out.println(name);
        msMeasureRecord.setProductionName(name);
        msMeasureRecord.setId("fff02229cabb43438090456cf0bfc343");

      //  msMeasureRecordService.updateById(msMeasureRecord);
        ArrayList<MsMeasureRecord> mm=new ArrayList<>();
        mm.add(msMeasureRecord);
        msMeasureRecordService.updateBatchById(mm);

    }

}
