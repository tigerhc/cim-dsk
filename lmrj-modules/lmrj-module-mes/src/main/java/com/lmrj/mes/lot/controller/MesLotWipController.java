package com.lmrj.mes.lot.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.mes.lot.entity.MesLotWip;
import com.lmrj.mes.lot.service.IMesLotWipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.lot.controller
 * @title: mes_lot_wip控制器
 * @description: mes_lot_wip控制器
 * @author: 张伟江
 * @date: 2020-08-05 10:32:55
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("mes/meslotwip")
@ViewPrefix("mes/meslotwip")
@RequiresPathPermission("mes:meslotwip")
@LogAspectj(title = "mes_lot_wip")
public class MesLotWipController extends BaseCRUDController<MesLotWip> {
    @Autowired
    private IMesLotWipService iMesLotWipService;

    //批次在制品(仕掛)批次状态图
    @RequestMapping("/findLotYield")
    public Response findLotYield(@RequestParam String lineNo, HttpServletRequest request, HttpServletResponse response) {
        Response res=new Response();
        List<Map> maps =  iMesLotWipService.findLotYield(lineNo);
        res.put("yield",maps);
        return res;
    }
}