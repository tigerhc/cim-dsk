package com.lmrj.edc.state.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.util.calendar.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Calendar;
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

    @GetMapping("eqpState")
    public Response eqpState(String periodDate){
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = DateUtil.parseDate(periodDate+"080000", "yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.DAY_OF_MONTH,1);
            endTime = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int size=edcEqpStateService.syncEqpSate(startTime, endTime);
        return Response.ok(String.valueOf(size));
    }
}
