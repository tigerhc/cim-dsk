package com.lmrj.rms.IBMMQ.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJms

@RestController
public class mqUtil {

    @Autowired
    private JmsTemplate jmsTemplate;
    public static void main(String[] args) {
        SpringApplication.run(mqUtil.class, args);
        new mqUtil().test("1");
    }

    @RequestMapping("/test")
    public String test(@RequestParam String msg){
        //LQ1DDTESO
        jmsTemplate.convertAndSend("QM01LINSTEN",msg);
        return "ok";

    }

    @JmsListener(destination = "QM01LINSTEN")
    public void getMsg(String msg){
        System.out.println(String.format("收到的消息[%s]",msg));
    }

}
