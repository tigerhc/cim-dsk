package com.lmrj.gem.ams.service.impl;

import com.lmrj.gem.GemBootApplication;
import com.lmrj.gem.ams.entity.GemAmsAlarmDefine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zwj on 2019/10/7.
 */
@SpringBootTest(classes = GemBootApplication.class)
@RunWith(SpringRunner.class)
public class GemAmsAlarmDefineServiceImplTest {

    @Autowired
    GemAmsAlarmDefineServiceImpl gemAmsAlarmDefineServiceImpl;
    private static final Logger log = LoggerFactory.getLogger(GemAmsAlarmDefineServiceImplTest.class);

    @Test
    public void findEqpByEqpId() throws Exception {

        GemAmsAlarmDefine fff = gemAmsAlarmDefineServiceImpl.selectById("08966daa7a0611e895b3b05c033fd4f8");
        log.info("info=============");
        log.debug("debug=============");
    }
}