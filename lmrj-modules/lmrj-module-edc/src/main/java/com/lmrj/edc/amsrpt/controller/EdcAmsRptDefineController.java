package com.lmrj.edc.amsrpt.controller;

import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.User;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefine;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

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
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "initDefine", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Response initstatus() {
        List<EdcAmsRptDefine> amsRptDefineList = edcAmsRptDefineService.selectList(new EntityWrapper<>());
        for (EdcAmsRptDefine amsRptDefine:amsRptDefineList) {
            redisTemplate.opsForList().rightPush("amsRptDefineList", amsRptDefine);
        }
        return Response.ok("初始化成功");
    }


    //@Autowired
    //private IEdcAmsDefineService edcAmsDefineService;
    //public EdcAmsRptDefine get(String id) {
    //    if (!ObjectUtil.isNullOrEmpty(id)) {
    //        return edcAmsRptDefineService.selectById(id);
    //    } else {
    //        return newModel();
    //    }
    //}

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
       // String edcParamRecordDtlListJson = StringEscapeUtils.unescapeHtml4(request.getParameter("_detail"));
       // String mails=StringEscapeUtils.unescapeHtml4(request.getParameter("_detail_mail"));
       // List<EdcAmsRptDefineAct> edcAmsRptDefineAct = JSONObject.parseArray(edcParamRecordDtlListJson, EdcAmsRptDefineAct.class);
       //List<EdcAmsRptDefineActEmail>  edcAmsRptDefineActEmailList=JSONObject.parseArray(mails, EdcAmsRptDefineActEmail.class);
       // entity.setEdcAmsRptDefineAct(edcAmsRptDefineAct);
       // entity.setEdcAmsRptDefineActEmailList(edcAmsRptDefineActEmailList);
    }

    @PutMapping("/active_flag/{id}/{flag}")
    public Response editFlag(@PathVariable String id, @PathVariable String flag) {
        edcAmsRptDefineService.editFlag(id, flag);
        return Response.ok("修改成功");
    }
}
