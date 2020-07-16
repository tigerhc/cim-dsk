package com.lmrj.cim;

import com.alibaba.fastjson.JSONArray;
import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.ms.record.service.IMsMeasureRecordService;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)

public class TestMS {
    @Autowired
    IMsMeasureRecordService msMeasureRecordService;
    @Autowired
    ValueOperations valueOperations;
    @Autowired
    IApsPlanPdtYieldService apsPlanPdtYieldService;

 @Test
    public void find(){
        List<MsMeasureRecord> ms=msMeasureRecordService.findAll();
        String aps= (String) valueOperations.get("aps");
        List< ApsPlanPdtYield > as= JSONArray.parseArray(aps,ApsPlanPdtYield.class);
        System.out.println(aps);
        List<MsMeasureRecord> ss=new ArrayList<>();
        List<MsMeasureRecord> m=  ms.stream().filter(item->
                Objects.isNull(item.getProductionName())).collect(Collectors.toList());

            for(ApsPlanPdtYield a:as){
            for(MsMeasureRecord  mm:m){
                if(a.getProductionNo().equals(mm.getProductionNo())) {
                    mm.setProductionName(a.getProductionName());
                    ss.add(mm);
                }

            }

        }

        ss.forEach(System.out::println);

     msMeasureRecordService.updateBatchById(ss);


 //       System.out.println("123456:"+m);
     /*for(MsMeasureRecord :ms) {
             if(Objects.isNull(m.getProductionName())){
                 for(ApsPlanPdtYield a:as){
                 m.setProductionName(a.getProductionName());
                 ss.add(m);
                 }
             }
         } */
     }

        }







