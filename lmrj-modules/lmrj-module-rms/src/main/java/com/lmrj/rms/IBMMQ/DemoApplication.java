package com.lmrj.rms.IBMMQ;

import com.lmrj.rms.RmsBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(RmsBootApplication.class, args);

        System.out.println("WebDemoApplication is success!");
    }
}
