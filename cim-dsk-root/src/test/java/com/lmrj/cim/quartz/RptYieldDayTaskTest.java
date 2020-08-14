package com.lmrj.cim.quartz;

import com.lmrj.cim.CimBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class RptYieldDayTaskTest {
    @Autowired
    RptYieldDayTask rptYieldDayTask;
    @Autowired
    RptYieldTask rptYieldTask;
    @Autowired
    EqpStateTask eqpStateTask;
    @Autowired
    OperationYieldTask operationYieldTask;
    @Autowired
    EdcAmsRecordYieldTask edcAmsRecordYieldTask;
    @Test
    public void test() throws Exception{
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        Date startTime = simpleDateFormat.parse("2020-07-31 13:55:36");
        Date endTime = simpleDateFormat.parse("2020-07-31 14:04:33");*/
        //rptYieldDayTask.updateDayYield();
        //rptYieldTask.updateYield();
        //eqpStateTask.eqpStateDay();
        //eqpStateTask.fixeqpState();
        //operationYieldTask.updateOperationYield();
        edcAmsRecordYieldTask.updateAmsRecordYield();
    }
}
