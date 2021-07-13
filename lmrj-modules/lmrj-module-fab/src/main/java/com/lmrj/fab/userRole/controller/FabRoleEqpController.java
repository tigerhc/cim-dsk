package com.lmrj.fab.userRole.controller;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.controller.BaseBeanController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.userRole.entity.FabRoleEqp;
import com.lmrj.fab.userRole.service.IFabRoleEqpService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wdj
 * @date 2021-06-05 15:08
 */
@RestController
@RequestMapping("fab/fabroleeqp")
@ViewPrefix("fab/fabroleeqp")
@RequiresPathPermission("fab:fabroleeqp")
@LogAspectj(title = "fab_fabroleeqp")
public class FabRoleEqpController extends BaseBeanController<FabRoleEqp> {
    @Autowired
    private IFabRoleEqpService iIotRoleEqpService;
    @Autowired
    private IFabEquipmentService fabEquipmentService;
    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
    @LogAspectj(logType = LogType.SELECT)
   // @RequiresMethodPermissions("list")
    public void list(HttpServletRequest request) throws IOException {
        //EntityWrapper<IotRoleEqp> entityWrapper = new EntityWrapper<>(entityClass);
        String roleid= request.getParameter("roleId");
        if (!StringUtil.isEmpty(roleid)){
          //  entityWrapper.in("roleid",roleid);

        }
        String orgid= request.getParameter("orgid");
        List<String> orgIds= Lists.newArrayList();
        if (!StringUtil.isEmpty(orgid)){

            orgIds=iIotRoleEqpService.listOrgIds(orgid);
            if(CollectionUtils.isEmpty(orgIds)){
                orgIds=Lists.newArrayList();
            }
            orgIds.add(orgid);
           // entityWrapper.in("orgid",orgIds);
        }
        List<FabEquipment> fablist = new ArrayList<>();
        fablist  = fabEquipmentService.selectPageByOffId(orgid);
            for(FabEquipment  fabEquipment:fablist){
                if(iIotRoleEqpService.getnum(fabEquipment.getEqpId(),roleid)>0){
                    fabEquipment.setIsFlag("true");
                }else{
                    fabEquipment.setIsFlag("false");
                }

            }
        FastJsonUtils.print(fablist);
    }


    @RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
    @LogAspectj(logType = LogType.SELECT)
    public void create(HttpServletRequest request) {

        String roleid= request.getParameter("roleId");
        if (!StringUtil.isEmpty(roleid)){
            //  entityWrapper.in("roleid",roleid);

        }
        String orgid= request.getParameter("orgid");

        String eqpId = request.getParameter("eqpid");

        try {

            String[] eqpStr = eqpId==null?null:eqpId.split(",");
            List<String> sourceList1 =new ArrayList<>();
            List<String> sourceList2 =new ArrayList<>();
         /*   if(eqpStr!=null){
                sourceList1 = Arrays.asList(eqpStr);
                sourceList2 = Arrays.asList(eqpStr);
            }
*/
            for (String str:eqpStr) {
                sourceList1.add(str);
                sourceList2.add(str);
            }

            List<FabEquipment> fablist = new ArrayList<>();
            List<String> eqpList = new ArrayList<>();
            fablist  = fabEquipmentService.selectPageByOffId(orgid);
            for(FabEquipment  fabEquipment:fablist){
                if(iIotRoleEqpService.getnum(fabEquipment.getEqpId(),roleid)>0){
                    eqpList.add(fabEquipment.getEqpId());
                }

            }
            sourceList1.removeAll(eqpList);//此时变为需要的数据此时变为需要新增的树
            for (String delId:sourceList1) {
                iIotRoleEqpService.insertByroleAndEqp(delId,roleid);
            }
            eqpList.removeAll(sourceList2);//此时变为需要删除的树
            for (String InId:eqpList){


                iIotRoleEqpService.deleteByroleAndEqp(InId,roleid);
            }

        } catch (Exception var6) {
            var6.printStackTrace();
            FastJsonUtils.print("保存失败!<br />原因:" + var6.getMessage());
        }
        FastJsonUtils.print("保存成功");
    }


}
