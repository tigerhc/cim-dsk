package com.lmrj.edc.ams.controller;

import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.User;
import com.lmrj.edc.ams.entity.EdcAmsDefine;
import com.lmrj.edc.ams.service.IEdcAmsDefineService;
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
 * @package com.lmrj.edc.ams.controller
 * @title: edc_ams_define控制器
 * @description: edc_ams_define控制器
 * @author: zhangweijiang
 * @date: 2020-02-15 02:39:16
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("edc/edcamsdefine")
@ViewPrefix("edc/edcamsdefine")
@RequiresPathPermission("edc:edcamsdefine")
@LogAspectj(title = "edc_ams_define")
public class EdcAmsDefineController extends BaseCRUDController<EdcAmsDefine> {

    @Autowired
    private IEdcAmsDefineService edcAmsDefineService;

    /**
     * 在返回数据之前编辑数据
     *
     * @param entity
     */
    @Override
    public void afterFind(EdcAmsDefine entity) {
        UserUtil.updateUserName(entity);
    }

    @Override
    public void afterAjaxList(PageResponse<EdcAmsDefine> pagejson) {
        List<EdcAmsDefine> list = pagejson.getResults();
        for (EdcAmsDefine edcParamRecord : list) {
            if (edcParamRecord.getCreateBy() != null) {
                User user = UserUtil.getUser(edcParamRecord.getCreateBy());
                if (user != null) {
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
    public void preSave(EdcAmsDefine entity, HttpServletRequest request, HttpServletResponse response) {
        //String edcParamRecordDtlListJson = StringEscapeUtils.unescapeHtml4(request.getParameter("_detail"));
        //List<EdcAmsDefineI18n> edcParamRecordDtlList = JSONObject.parseArray(edcParamRecordDtlListJson, EdcAmsDefineI18n.class);
        //entity.setEdcAmsDefineI18nList(edcParamRecordDtlList);
    }

    @PutMapping("/active_flag/{alarmId}/{flag}")
    public Response editFlag(@PathVariable String alarmId, @PathVariable String flag) {
        edcAmsDefineService.editFlag(alarmId, flag);
        return Response.ok("修改成功");
    }

}
