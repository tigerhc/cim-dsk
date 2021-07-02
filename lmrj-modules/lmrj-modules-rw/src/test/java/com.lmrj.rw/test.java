package com.lmrj.rw;

import com.lmrj.rw.plan.controller.RwPlanHisController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootTest
public class test {

//    @Autowired
//    private RwPlanHisController rwPlanHisController;
//
//    @Before
//    public void init() {
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//        rwPlanHisController = (RwPlanHisController) context.getBean("rwPlanHisController");
//    }
//
//    @Test
//    public void moTest(){
//        HttpServletResponse response = null;
//        HttpServletRequest request = null;
//        String s = rwPlanHisController.rwTest( "11", request, response );
//        System.out.println( s );
//    }
}
