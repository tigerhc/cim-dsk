package com.lmrj.rms.recipe.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "DemoService", // 暴露服务名称
        targetNamespace = "http://sercive.DemoService.lmrj.com")   //命名空间,一般是接口的包名倒序
public interface DemoService {
    //接口方法名
    @WebMethod
    @WebResult(name = "String", targetNamespace = "")
    public String findRecipeName(@WebParam(name = "eqpId") String eqpId) throws Exception;

}
