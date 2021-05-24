package com.lmrj.iot.config.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.iot.config.entity.IotCollectDataHis;
import com.lmrj.iot.config.service.IIotCollectDataHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 历史数据查询
 * @author wdj
 * @date 2021-05-20 14:43
 */
@RestController
@RequestMapping("iot/iotcollectdatahis")
@ViewPrefix("iot/iotcollectdatahis")
@RequiresPathPermission("iot:iotcollectdatahis")
@LogAspectj(title = "iot_iotcollectdata_his")
public class IotCollectDataHisController extends BaseCRUDController<IotCollectDataHis> {
    @Autowired
    private IIotCollectDataHisService iIotCollectDataHisService;
    /**
     * 获取设备列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/recipePermitList", method = { RequestMethod.GET, RequestMethod.POST })
    public void recipePermitList(HttpServletRequest request, HttpServletResponse response) {

        List<FabEquipment> retList = iIotCollectDataHisService.getFabList();
        String content = JSON.toJSONString(new DateResponse(retList));
        ServletUtils.printJson(response, content);
    }
}
