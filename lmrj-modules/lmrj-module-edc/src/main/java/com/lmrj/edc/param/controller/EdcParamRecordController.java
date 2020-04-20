package com.lmrj.edc.param.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.User;
import com.lmrj.edc.param.entity.EdcParamRecord;
import com.lmrj.edc.param.entity.EdcParamRecordDtl;
import com.lmrj.edc.param.service.IEdcParamRecordDtlService;
import com.lmrj.edc.param.service.IEdcParamRecordService;
import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.lmrj.fab.eqp.service.IFabEquipmentStatusService;
import com.lmrj.util.lang.ObjectUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title: edc_param_record
 * @Description: edc_param_record
 * @author zhangweijiang
 * @date 2019-06-11 21:26:40
 * @version V1.0
 *
 */
@RestController
@RequestMapping("edc/edcparamrecord")
@RequiresPathPermission("param:edcparamrecord")
@LogAspectj(title = "edc_param_record")
public class EdcParamRecordController extends BaseCRUDController<EdcParamRecord> {

    @Autowired
    protected IEdcParamRecordService edcParamRecordService;
    @Autowired
    private IEdcParamRecordDtlService edcParamRecordDtlService;
    @Autowired
    protected IFabEquipmentStatusService fabEquipmentStatusService;

    public EdcParamRecord get(String id) {
        if (!ObjectUtil.isNullOrEmpty(id)) {
            return edcParamRecordService.selectById(id);
        } else {
            return newModel();
        }
    }

    /**
     * 在异步获取数据之后
     *
     * @param pagejson
     */
    @Override
    public void afterAjaxList(PageResponse<EdcParamRecord> pagejson) {
        List<EdcParamRecord> list = pagejson.getResults();
        for(EdcParamRecord edcParamRecord: list){
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
    public void preSave(EdcParamRecord entity, HttpServletRequest request, HttpServletResponse response) {
        String edcParamRecordDtlListJson = StringEscapeUtils.unescapeHtml4(request.getParameter("_detail"));
        List<EdcParamRecordDtl> edcParamRecordDtlList = JSONObject.parseArray(edcParamRecordDtlListJson, EdcParamRecordDtl.class);
        entity.setEdcParamRecordDtlList(edcParamRecordDtlList);
    }




    //@RequestMapping(value = "/save", method = RequestMethod.POST)
    //@ResponseBody
    //public AjaxJson doSave(EdcParamRecord edcParamRecord, HttpServletRequest request, HttpServletResponse response,
    //                       BindingResult result) {
    //    AjaxJson ajaxJson = new AjaxJson();
    //    ajaxJson.success("保存成功");
    //    if (hasError(edcParamRecord, result)) {
    //        // 错误提示
    //        String errorMsg = errorMsg(result);
    //        if (!StringUtils.isEmpty(errorMsg)) {
    //            ajaxJson.fail(errorMsg);
    //        } else {
    //            ajaxJson.fail("保存失败");
    //        }
    //        return ajaxJson;
    //    }
    //    try {
    //        if (StringUtils.isEmpty(edcParamRecord.getId())) {
    //            edcParamRecordService.insert(edcParamRecord);
    //        } else {
    //            edcParamRecordService.insertOrUpdate(edcParamRecord);
    //        }
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        ajaxJson.fail("保存失败!<br />原因:" + e.getMessage());
    //    }
    //    return ajaxJson;
    //}



    //@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    //public String view(Model model, @PathVariable("id") String id, HttpServletRequest request,
    //                   HttpServletResponse response) {
    //    EdcParamRecord edcParamRecord = get(id);
    //    model.addAttribute("data", edcParamRecord);
    //    return display("edcparamrecordEdit");
    //}

    //@RequestMapping(value = "validate", method = { RequestMethod.GET, RequestMethod.POST })
    //@ResponseBody
    //public ValidJson validate(DuplicateValid duplicateValid, HttpServletRequest request) {
    //    ValidJson validJson = new ValidJson();
    //    Boolean valid = Boolean.FALSE;
    //    try {
    //        EntityWrapper<EdcParamRecord> entityWrapper = new EntityWrapper<EdcParamRecord>(entityClass);
    //        valid = edcParamRecordService.doValid(duplicateValid,entityWrapper);
    //        if (valid) {
    //            validJson.setStatus("y");
    //            validJson.setInfo("验证通过!");
    //        } else {
    //            validJson.setStatus("n");
    //            if (!StringUtils.isEmpty(duplicateValid.getErrorMsg())) {
    //                validJson.setInfo(duplicateValid.getErrorMsg());
    //            } else {
    //                validJson.setInfo("当前信息重复!");
    //            }
    //        }
    //    } catch (Exception e) {
    //        validJson.setStatus("n");
    //        validJson.setInfo("验证异常，请检查字段是否正确!");
    //    }
    //    return validJson;
    //}
}

