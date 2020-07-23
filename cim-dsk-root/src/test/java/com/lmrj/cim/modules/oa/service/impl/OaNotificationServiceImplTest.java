package com.lmrj.cim.modules.oa.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.impl.FabEquipmentServiceImpl;
import com.lmrj.cim.CimBootApplication;
import com.lmrj.cim.modules.oa.entity.OaNotification;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.impl.OvnBatchLotParamServiceImpl;
import com.lmrj.oven.batchlot.service.impl.OvnBatchLotServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * Created by zwj on 2019/5/24.
 */
@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class OaNotificationServiceImplTest {
    @Autowired
    OaNotificationServiceImpl oaNotificationServiceImpl;
    @Autowired
    FabEquipmentServiceImpl fabEquipmentServiceImpl;
    private static final Logger log = LoggerFactory.getLogger(OaNotificationServiceImplTest.class);

    @Test
    public void findEqpByEqpId() throws Exception {

        OaNotification fff = oaNotificationServiceImpl.selectById("40281e815c912406015c914e3e27006b");
        log.info("info=============");
        log.debug("debug=============");
    }

    @Test
    public void findEqpByEqpId2() throws Exception {

        FabEquipment fff = fabEquipmentServiceImpl.selectById("08966daa7a0611e895b3b05c033fd4f8");
        log.info("info=============");
        log.debug("debug=============");
    }


    @Autowired
    OvnBatchLotParamServiceImpl ovnBatchLotParamService;

    @Test
    public void find() {
        String a = "SIM-PRINTER1";
        String t1 = "2020-05-28";
        String t2 = "2020-05-29";
        List<OvnBatchLotParam> os = ovnBatchLotParamService.findById(a, t1, t2);
        os.forEach(System.out::println);
        log.info("123456789");
    }

    @Autowired
    OvnBatchLotServiceImpl ovnBatchLotService;

    @Test
    public void finda() {
        // String a="SIM-PRINTER1";
        String a = "SIM-REFLOW1";
        String t1 = "2020-05-28";
        String t2 = "2020-05-29";
        List<Map> os = ovnBatchLotService.findDetailBytime(t1, t2, a);
        OvnBatchLot ovnBatchLot = ovnBatchLotService.selectOne(new EntityWrapper<OvnBatchLot>().eq("eqp_id", a));
        System.out.println(ovnBatchLot.getOtherTempsTitle());
        Map maps = new HashMap();
        String aaa[] = ovnBatchLot.getOtherTempsTitle().split(",");
        List list = new ArrayList();
        List list1 = new ArrayList();

        for (int j = 0; j < aaa.length / 4; j++) {
            for (int i = 0; i < os.size(); i++) {
                String f = aaa[j * 4];
                String str = (String) os.get(i).get("other_temps_value");
                // System.out.println(sss.length());
                String h[] = str.split(",");
             //aaa是title数组
                    String str1 = String.valueOf(os.get(i).get("temp_pv"));
                    String str2 = String.valueOf(os.get(i).get("temp_sp"));
                    String str3 = String.valueOf(os.get(i).get("temp_min"));
                    String str4 = String.valueOf(os.get(i).get("temp_max"));
                    String sss[] = {str1, str2, str3, str4};
                    String fff = f.replace("现在值", "");
                    list1.add(sss);

                    String b = h[j * 4];
                    String c = h[j * 4 + 1];
                    String d = h[j * 4 + 2];
                    String e = h[j * 4 + 3];
                    String s[] = {b, c, d, e};
                    list.add(s);
                    String ff = f.replace("当前值", "");
                    maps.put(ff, list);
                maps.put( "第一温区",list1);
            }

           /* maps.forEach((key, value) ->
                System.out.println(key + ":" + value);
            });*/
            System.out.println(maps.get("第5温区温度"));
        }

    }


}
