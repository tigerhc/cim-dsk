package com.lmrj.cim;


import com.alibaba.fastjson.JSONArray;
import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
import com.lmrj.util.mapper.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)


public class TestRedis {
@Autowired
IApsPlanPdtYieldService iApsPlanPdtYieldService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ValueOperations valueOperations;


    @Autowired
    private SetOperations setOperations;
    @Test
    public  void getProName() {

       List<ApsPlanPdtYield> apsPlanPdtYieldList=
               iApsPlanPdtYieldService.selectAps();
      // apsPlanPdtYieldList.forEach(System.out::println);
       String aps= JsonUtil.toJsonString(apsPlanPdtYieldList);
        valueOperations.set("aps",aps);
        List<ApsPlanPdtYield> as= JSONArray.parseArray(aps,ApsPlanPdtYield.class);
        as.forEach(item-> System.out.println(item.getProductionName()));
        as.forEach(System.out::println);
       // System.out.println("as:"+ as);
       setOperations.add("list",apsPlanPdtYieldList);

       // System.out.println(setOperations.members("list"));




      /* String aps= JsonUtil.toJsonString(apsPlanPdtYieldList);
        valueOperations.set("aps",aps);

        System.out.println(valueOperations.get("aps"));*/




        }




}
