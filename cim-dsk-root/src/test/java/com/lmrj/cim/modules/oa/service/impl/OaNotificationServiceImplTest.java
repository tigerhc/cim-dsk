package com.lmrj.cim.modules.oa.service.impl;

import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.impl.FabEquipmentServiceImpl;
import com.lmrj.cim.CimBootApplication;
import com.lmrj.cim.modules.oa.entity.OaNotification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}
