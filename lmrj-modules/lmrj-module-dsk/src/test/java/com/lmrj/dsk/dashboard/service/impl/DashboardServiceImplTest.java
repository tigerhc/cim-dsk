package com.lmrj.dsk.dashboard.service.impl;

import com.lmrj.dsk.DskBootApplication;
import com.lmrj.dsk.dashboard.service.IDashboardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class DashboardServiceImplTest {
    @Autowired
    IDashboardService dashboardServiceImpl;
    @Test
    public void findEqpStatusByFabId() {
         dashboardServiceImpl.findEqpStatusByFabId("DSK");
        if(Objects.isNull(dashboardServiceImpl.findEqpStatusByFabId("DSK"))){
            System.out.println("空");
        }
    }

    @Test
    public void findAlarmByFab() {
    }

    @Test
    public void findRunTimeFab() {
    }
}
