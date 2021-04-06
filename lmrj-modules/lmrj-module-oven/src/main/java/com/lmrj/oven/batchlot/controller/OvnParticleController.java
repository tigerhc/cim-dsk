package com.lmrj.oven.batchlot.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.oven.batchlot.entity.ParticleDataBean;
import com.lmrj.oven.batchlot.service.IOvnParticleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("oven/ovnparticle")
@ViewPrefix("modules/OvnParticle")
@RequiresPathPermission("OvnParticle")
@LogAspectj(title = "ovn_particle")
public class OvnParticleController extends BaseCRUDController<ParticleDataBean> {
    @Autowired
    private IOvnParticleService particleService;

    @RequestMapping("echartData")
    public Response echartData(@RequestParam String eqpId, @RequestParam String beginTime, @RequestParam String endTime){
        if(StringUtils.isEmpty(eqpId)||StringUtils.isEmpty(beginTime)||StringUtils.isEmpty(endTime)){
            return Response.error("参数不正确"+eqpId+beginTime+endTime);
        }
        Map<String, Object> param = new HashMap();
        param.put("eqpId", eqpId);
        param.put("beginTime", beginTime);
        param.put("endTime", endTime);
        Response rs = Response.ok();
        rs.put("data", particleService.getEchartData(param));
        return rs;
    }

    @RequestMapping("getParticleEqps")
    public Response getParticleEqps(){
        Response rs = Response.ok();
        rs.putList("eqps", particleService.getParticleEqps());
        return rs;
    }
}
