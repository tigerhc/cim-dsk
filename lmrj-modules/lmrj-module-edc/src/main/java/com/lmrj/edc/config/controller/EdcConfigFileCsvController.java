package com.lmrj.edc.config.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.config.entity.EdcConfigFileCsv;
import com.lmrj.edc.config.service.IEdcConfigFileCsvService;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.config.controller
 * @title: edc_config_file_csv控制器
 * @description: edc_config_file_csv控制器
 * @author: 张伟江
 * @date: 2020-07-23 16:12:15
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcconfigfilecsv")
@ViewPrefix("edc/edcconfigfilecsv")
@RequiresPathPermission("edc:edcconfigfilecsv")
@LogAspectj(title = "edc_config_file_csv")
public class EdcConfigFileCsvController extends BaseCRUDController<EdcConfigFileCsv> {

    @Autowired
    private IEdcConfigFileCsvService edcConfigFileCsvService;

    @RequestMapping(value = "/getEqpModelMessage", method = { RequestMethod.GET, RequestMethod.POST })
    public void getEqpModelMessage(HttpServletRequest request, HttpServletResponse response) {
        List<FabEquipmentModel> eqpModelMessage = edcConfigFileCsvService.getEqpModelMessage();
        DateResponse listjson = new DateResponse(eqpModelMessage);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response,content);
    }

}
