package com.lmrj.cim.quartz;

import com.lmrj.cim.CimBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class MesLotWipTaskTest {
    @Autowired
    MesLotWipTask mesLotWipTask;
    @Test
    public void wipTask() {
        mesLotWipTask.buildWipData();
    }
}
