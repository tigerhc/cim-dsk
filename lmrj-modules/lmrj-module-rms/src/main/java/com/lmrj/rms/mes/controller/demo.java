package com.lmrj.rms.mes.controller;

import com.lmrj.rms.recipe.service.DemoService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class demo {
    public static void main(String[] args) throws MalformedURLException {
        //创建WSDL文件的URL
        URL wsdlDocumentLocation=new URL("http://127.0.0.1:800/services/DemoService?wsdl");
        //创建服务名称
        //1.namespaceURI - 命名空间地址
        //2.localPart - 服务视图名
        QName serviceName=new QName("http://sercive.DemoService.lmrj.com","DemoService");
        Service service=Service.create(wsdlDocumentLocation, serviceName);

        //获取服务实现类
        DemoService port = service.getPort(DemoService.class);
        //调用方法
        String message=port.findRecipeName("123");
        System.out.println(message);
    }
}