package com.lmrj.edc.amsrpt.controller;

import com.alibaba.fastjson.JSONObject;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.edc.ams.service.IEdcAmsDefineService;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefine;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefineAct;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefineActEmail;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineService;
import com.lmrj.util.lang.ObjectUtil;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.User;
import com.lmrj.cim.utils.UserUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.amsrpt.controller
 * @title: edc_ams_rpt_define控制器
 * @description: edc_ams_rpt_define控制器
 * @author: zhangweijiang
 * @date: 2020-02-15 02:46:43
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcamsrptdefine")
@ViewPrefix("edc/edcamsrptdefine")
@RequiresPathPermission("edc:edcamsrptdefine")
@LogAspectj(title = "edc_ams_rpt_define")
public class EdcAmsRptDefineController extends BaseCRUDController<EdcAmsRptDefine> {

    @Autowired
    private IEdcAmsRptDefineService edcAmsRptDefineService;


    @Autowired
    private IEdcAmsDefineService edcAmsDefineService;
    public EdcAmsRptDefine get(String id) {
        if (!ObjectUtil.isNullOrEmpty(id)) {
            return edcAmsRptDefineService.selectById(id);
        } else {
            return newModel();
        }
    }

    @Override
    public void afterAjaxList(PageResponse<EdcAmsRptDefine> pagejson) {
        List<EdcAmsRptDefine> list = pagejson.getResults();
        for(EdcAmsRptDefine edcParamRecord: list){
            if(edcParamRecord.getCreateBy() != null){
                User user = UserUtil.getUser(edcParamRecord.getCreateBy());
                if(user != null){
                    edcParamRecord.setCreateByName(user.getRealname());
                }
            }
        }
    }
    /**
     * 保存数据之前,处理细表
     *
     * @param entity
     * @param request
     * @param response
     */
    public void preSave(EdcAmsRptDefine entity, HttpServletRequest request, HttpServletResponse response) {
        String edcParamRecordDtlListJson = StringEscapeUtils.unescapeHtml4(request.getParameter("_detail"));
        String mails=StringEscapeUtils.unescapeHtml4(request.getParameter("_detail_mail"));
        List<EdcAmsRptDefineAct> edcAmsRptDefineAct = JSONObject.parseArray(edcParamRecordDtlListJson, EdcAmsRptDefineAct.class);
       List<EdcAmsRptDefineActEmail>  edcAmsRptDefineActEmailList=JSONObject.parseArray(mails, EdcAmsRptDefineActEmail.class);
        entity.setEdcAmsRptDefineAct(edcAmsRptDefineAct);
        entity.setEdcAmsRptDefineActEmailList(edcAmsRptDefineActEmailList);
    }

    @PutMapping("/active_flag/{arm_id}/{flag}")
    public Response editFlag(@PathVariable String arm_id, @PathVariable String flag) {
        EdcAmsRptDefine edcAmsRptDefine=edcAmsRptDefineService.selectById(arm_id);
        edcAmsRptDefine.setActiveFlag(flag);
        edcAmsRptDefineService.updateById(edcAmsRptDefine);
        return Response.ok("修改成功");
    }
}
