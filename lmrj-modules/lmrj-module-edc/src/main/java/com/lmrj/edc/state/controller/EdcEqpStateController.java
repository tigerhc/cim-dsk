package com.lmrj.edc.state.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.core.log.LogAspectj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.state.controller
 * @title: edc_eqp_state控制器
 * @description: edc_eqp_state控制器
 * @author: 张伟江
 * @date: 2020-02-20 01:26:46
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edceqpstate")
@ViewPrefix("edc/edceqpstate")
@RequiresPathPermission("edc:edceqpstate")
@LogAspectj(title = "edc_eqp_state")
public class EdcEqpStateController extends BaseCRUDController<EdcEqpState> {

    @Autowired
    private IEdcEqpStateService edcEqpStateService;

    @GetMapping("task")
    public Response task(Date startTime,Date endTime){
        int size=edcEqpStateService.syncEqpSate(startTime,endTime);
         return Response.ok(String.valueOf(size));
    }
}
