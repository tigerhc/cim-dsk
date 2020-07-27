package com.lmrj.mes.track.service.impl;

import com.lmrj.cim.CimBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
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
}
