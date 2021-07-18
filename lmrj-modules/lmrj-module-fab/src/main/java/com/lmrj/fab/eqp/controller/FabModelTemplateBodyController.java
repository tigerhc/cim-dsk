package com.lmrj.fab.eqp.controller;

import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mvc.entity.AbstractEntity;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.entity.FabModelTemplate;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.entity.TreeModel;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.fab.eqp.service.IFabModelTemplateBodyService;
import com.lmrj.fab.eqp.service.IFabSensorModelService;
import com.lmrj.util.lang.ObjectUtil;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * @author wdj
 * @date 2021-06-01 8:57
 */
@RestController
@RequestMapping("fab/fabModeltemplatebody")
@ViewPrefix("fab/fabModeltemplatebody")
@RequiresPathPermission("fab:fabModeltemplatebody")
@LogAspectj(title = "fab_model_template_body")
public class FabModelTemplateBodyController extends BaseCRUDController<FabModelTemplateBody> {
    @Autowired
    private IFabSensorModelService fabEquipmentModelService;
    @Autowired
    private IFabModelTemplateBodyService fabModelTemplateBodyService;
    /**
     * 设备大类小类记录（全）
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/modelTemplateList", method = { RequestMethod.GET, RequestMethod.POST })
    public void modelTemplateList(Model model, HttpServletRequest request,
                                  HttpServletResponse response) {
        List<FabModelTemplateBody> templateList = new ArrayList<>();
        List<Map> List = fabEquipmentModelService.getAlltemplateList();
        FabModelTemplateBody body = new FabModelTemplateBody();
        for (Map map:List) {
            body = new FabModelTemplateBody();
            body.setParentType(map.get("parentType").toString());
            body.setType(map.get("type").toString());
            body.setSubClassCode(map.get("classCode").toString());
            templateList.add(body);
        }
        List<TreeModel> retList =   listToTree(templateList);
        DateResponse listjson = new DateResponse(retList);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    /**
     * 树形转化
     * @param sourelist
     * @return
     */
    List<TreeModel> listToTree(List<FabModelTemplateBody> sourelist){
        List<TreeModel> retList1 = new ArrayList<>();
        List<TreeModel> retList2 = new ArrayList<>();
        List<TreeModel> retList3 = new ArrayList<>();
        TreeModel treeModel1 = new TreeModel();
        TreeModel treeModel2 = new TreeModel();
        TreeModel treeModel3 = new TreeModel();
        String str1 = "";
        String str2 = "";
        String str3 = "";
        Set<String> set=new HashSet<String>();
        Set<String> set2=new HashSet<String>();
        Set<String> set3=new HashSet<String>();
        for (FabModelTemplateBody body:sourelist) {
            set.add(body.getParentType());
            set2.add(body.getParentType()+","+body.getType());
            set3.add(body.getParentType()+","+body.getType()+","+body.getSubClassCode());
        }
        for( Iterator<String> it = set.iterator();  it.hasNext(); )  //打印集合
        {

            str1 = it.next();
            treeModel1 = new TreeModel();
            retList2 = new ArrayList<>();
            for( Iterator<String> it2 = set2.iterator();  it2.hasNext(); ){
                str2 = it2.next();
                if(str2.startsWith(str1+",")){
                    String[] stra2 = str2.split(",");
                    treeModel2 = new TreeModel();
                    retList3 = new ArrayList<>();
                for( Iterator<String> it3 = set3.iterator();  it3.hasNext(); ){
                    str3 = it3.next();
                    if(str3.startsWith(str2+",")){
                        treeModel3 = new TreeModel();
                       String[] stra3 = str3.split(",");
                        treeModel3.setTreeNode("subClassCode");
                        treeModel3.setTreeValue(stra3[2]);
                        treeModel3.setNum("0");
                        retList3.add(treeModel3);
                    }
                    }
                    treeModel2.setTreeNode("type");
                    treeModel2.setTreeValue(stra2[1]);
                    treeModel2.setTreeModelList(retList3);
                    retList2.add(treeModel2);
                }

            }
            treeModel1.setTreeNode("parentType");
            treeModel1.setTreeValue(str1);
            treeModel1.setTreeModelList(retList2);
            retList1.add(treeModel1);

        }

        return retList1;
    }

    /**
     * 备大类小类记录（单模板）
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/oneTemplateList/{modelId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void oneTemplateList(Model model, @PathVariable("modelId") String modelId, HttpServletRequest request,
                                HttpServletResponse response) {
        List<FabModelTemplateBody> templateList = new ArrayList<>();
        FabModelTemplateBody body = new FabModelTemplateBody();
        List<Map> List = fabModelTemplateBodyService.getOneTemplateList(modelId);
        for (Map map:List) {
            body = new FabModelTemplateBody();
            body.setParentType(map.get("parentType").toString());
            body.setType(map.get("type").toString());
            body.setNum(map.get("num").toString());
            body.setSubClassCode(map.get("subClassCode").toString());
            templateList.add(body);
        }
        DateResponse listjson = new DateResponse(templateList);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }



}
