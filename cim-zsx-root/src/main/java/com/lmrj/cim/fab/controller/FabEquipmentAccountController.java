package com.lmrj.cim.fab.controller;

import com.lmrj.cim.fab.entity.FabEquipmentAccount;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.ms.record.entity.MsMeasureRecordDetail;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author daoda
 */
@RestController
@RequestMapping("ms/fabequipmentaccount")
@ViewPrefix("ms/fabEquipmentaccount")
@RequiresPathPermission("ms:fabEquipmentaccount")
@LogAspectj(title = "fab_equipment_account")
public class FabEquipmentAccountController extends BaseCRUDController<FabEquipmentAccount>  {

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("设备台帐", queryable,  propertyPreFilterable,  request,  response);
    }

    @RequestMapping(value = "/clientsave", method = { RequestMethod.GET, RequestMethod.POST })
    public Response insert( HttpServletRequest request,
                            HttpServletResponse response){
        Response res= new DateResponse();
        try {
            String account = request.getReader().readLine();
            FabEquipmentAccount fabEquipmentAccount = JsonUtil.from(account, FabEquipmentAccount.class);
            String id = StringUtil.randomTimeUUID("FA");
            if (fabEquipmentAccount == null) {
                return res.error("无法解析");
            }
            fabEquipmentAccount.setId(id);
            boolean flag = commonService.insert(fabEquipmentAccount);
            if(flag){
                res = DateResponse.ok(id);
                //res.put("record", msMeasureRecord);
                return res;
                //String content = JSON.toJSONString(msMeasureRecord);
                //ServletUtils.printJson(response, content);
            }else{
                return res.error("NG");
                //ServletUtils.printJson(response, "NG");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.error("无法解析");

    }
}
