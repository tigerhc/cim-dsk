package com.lmrj.mes.track.service.impl;

import com.lmrj.cim.CimBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class MesLotTrackServiceImplTest {
    @Autowired
    MesLotTrackServiceImpl mesLotTrackService;
    @Test
    public void buildWipData(){
        mesLotTrackService.buildWipData();
    }
    @Test
    /*@Scheduled(cron = "0 0 0 2 * ?")*/
    public void deleteWip() throws Exception{
        String startTime="2020-07-20 06:20:04";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(startTime);
        mesLotTrackService.deleteWip(date);
    }
}
