package com.lmrj.cim.modules.sys;

import com.lmrj.cim.CimBootApplication;
import com.lmrj.core.email.service.IEmailSendService;
import com.lmrj.fab.FabBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zwj on 2019/5/24.
 */
@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class MailServiceImplTest {
    @Autowired
    private IEmailSendService emailSendService;

    @Test
    public void emailSendTest() throws InterruptedException {
        String[] emails = {"1518798637@qq.com","472109366@qq.com"};
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("EQP_ID","TEST");
        msgMap.put("ALARM_CODE","RTP_ALARM");
        emailSendService.send(emails,"RTP_ALARM",msgMap);
        Thread.sleep(100000);
    }

}
