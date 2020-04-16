package com.lmrj.oven.batchlot.service.impl;

import com.lmrj.oven.OvenBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zwj on 2019/6/22.
 */
@SpringBootTest(classes = OvenBootApplication.class)
@RunWith(SpringRunner.class)
public class OvnBatchLotServiceImplTest {
    @Autowired
    OvnBatchLotServiceImpl ovnBatchLotServiceImpl;
    @Test
    public void selectFabStatus() throws Exception {
        ovnBatchLotServiceImpl.selectFabStatus("08966daa7a0611e895b3b05c033fd4f8");
    }

}