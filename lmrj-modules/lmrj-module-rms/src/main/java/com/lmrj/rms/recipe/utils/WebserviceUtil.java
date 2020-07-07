package com.lmrj.rms.recipe.utils;

import com.lmrj.rms.recipe.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

@Slf4j
public class WebserviceUtil {

    //四个参数分别是webservice地址，命名空间，方法名，jsonString数据
    public static Object toWebService(String mesUrl, String mesSpace,  String function, String obj) {
        Object[] result = null;
        Object object = null;
        try {
            JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
            Client client = clientFactory.createClient(mesUrl);
//        设置连接超时
            HTTPConduit conduit = (HTTPConduit) client.getConduit();
            HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
            httpClientPolicy.setConnectionTimeout(1000);
            httpClientPolicy.setAllowChunking(false);
            httpClientPolicy.setReceiveTimeout(2000);
            conduit.setClient(httpClientPolicy);


            //如果有命名空间的话
            QName operationName = new QName(mesSpace, function); //如果有命名空间需要加上这个，第一个参数为命名空间名称，第二个参数为WebService方法名称
            result = client.invoke(operationName, obj);//后面为WebService请求参数数组
//            result = client.invoke(function, obj);
            object = result[0];
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("异常信息为[{}]", e);
            e.printStackTrace();
            log.warn("发送mes失败");
        }
        return object;

    }

    public static void main(String[] args) {
        try {
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
            String message=port.findRecipeName("SIM-DM5");
            System.out.println(message);
        }catch (Exception e){
            log.error("Exception:", e);
        }

    }
}
