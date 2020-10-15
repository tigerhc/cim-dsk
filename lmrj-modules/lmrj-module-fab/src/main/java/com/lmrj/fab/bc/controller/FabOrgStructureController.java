package com.lmrj.fab.bc.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.bc.entity.FabOrgStructure;
import com.lmrj.fab.bc.service.IFabOrgStructureService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lmrj.common.http.Response;

@RestController
@RequestMapping("fab/faborgstructure")
@ViewPrefix("modules/FabOrgStructure")
@RequiresPathPermission("FabOrgStructure")
@LogAspectj(title = "fab_org_structure")
public class FabOrgStructureController extends BaseBeanController<FabOrgStructure> {
    @Autowired
    private IFabOrgStructureService fabOrgStructureService;

    @RequestMapping(value = "/synchro", method = { RequestMethod.GET, RequestMethod.POST })
    public Response synchro(@RequestParam String orgCode){
        if(StringUtil.isEmpty(orgCode)){
            return Response.error("参数不正确");
        }
        return Response.ok(fabOrgStructureService.synchro(orgCode));
    }
}
