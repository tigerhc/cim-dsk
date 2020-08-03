package com.lmrj.cim.quartz;

import com.lmrj.cim.CimBootApplication;
import com.lmrj.edc.lot.service.impl.RptLotYieldDayServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class RptYieldDayTaskTest {
    @Autowired
    RptLotYieldDayServiceImpl rptLotYieldDayService;
    @Test
    public void test() throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        Date startTime = simpleDateFormat.parse("2020-07-31 13:55:36");
        Date endTime = simpleDateFormat.parse("2020-07-31 14:04:33");
        rptLotYieldDayService.updateDayYield(startTime,endTime);
    }
}
