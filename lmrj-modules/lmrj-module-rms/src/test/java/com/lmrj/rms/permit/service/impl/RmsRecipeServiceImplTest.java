package com.lmrj.rms.permit.service.impl;

import com.lmrj.rms.RmsBootApplication;
import com.lmrj.rms.permit.service.IRmsRecipePermitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.AccessType;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RmsBootApplication.class)
@RunWith(SpringRunner.class)
public class RmsRecipeServiceImplTest {

    @Autowired
    private IRmsRecipePermitService rmsRecipePermitService;

    @Test
    public void permit() throws Exception {
        rmsRecipePermitService.permit("QA","4f601a61259b4128a2cf2c35e6b6bef6","2","XX参数不符合要求");
    }
}
