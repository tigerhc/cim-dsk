package com.lmrj.edc.ams.service.impl;

import com.lmrj.edc.EdcBootApplication;
import com.lmrj.edc.ams.entity.EdcAmsDefine;
import com.lmrj.edc.ams.service.IEdcAmsDefineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zwj on 2019/6/14.
 */
@SpringBootTest(classes = EdcBootApplication.class)
@RunWith(SpringRunner.class)
public class EdcAmsDefineServiceImplTest {
    @Autowired
    IEdcAmsDefineService edcAmsDefineService;
    private static final Logger log = LoggerFactory.getLogger(EdcAmsDefineServiceImplTest.class);

    @Test
    public void findEqpByEqpId() throws Exception {

        EdcAmsDefine fff = edcAmsDefineService.selectById("1");
        log.info("info=============");
    }
}