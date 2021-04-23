package com.lmrj.edc.particle.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.particle.entity.ParticleDataBean;
import com.lmrj.edc.particle.service.IEdcParticleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("ovn/ovnparticle")
@ViewPrefix("modules/EdcParticle")
@RequiresPathPermission("EdcParticle")
@LogAspectj(title = "edc_particle")
public class EdcParticleController extends BaseCRUDController<ParticleDataBean> {
    @Autowired
    private IEdcParticleService particleService;

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
