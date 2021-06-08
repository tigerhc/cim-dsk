package com.lmrj.fab.eqp.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.fab.eqp.entity.SensorNumType;
import com.lmrj.fab.eqp.service.ISensorNumTypeservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-02 16:50
 */
@RestController
@RequestMapping("fab/sensornumtype")
@ViewPrefix("fab/sensornumtype")
@RequiresPathPermission("fab:sensornumtype")
public class SensorNumTypeController extends BaseCRUDController<SensorNumType> {
    @Autowired
    private ISensorNumTypeservice sensorNumTypeservice;

    /**
     * 示数类型（设备参数绑定）
     * @param model
     * @param request
     * @param response
     * @return
     * Queryable queryable,
     * QueryableConvertUtils.convertQueryValueToEntityValue(queryable, this.entityClass);
     *         List<FabSensor>  eqps = FabSensorService.listWithNoPage(queryable);
     */
    @RequestMapping(value = "/numTypeList/{classCode}", method = { RequestMethod.GET, RequestMethod.POST })
    public void sorIdlist(Model model, @PathVariable String classCode, HttpServletRequest request,
                          HttpServletResponse response) {
        List<String> list = sensorNumTypeservice.getNumtypeList(classCode);
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }
}
