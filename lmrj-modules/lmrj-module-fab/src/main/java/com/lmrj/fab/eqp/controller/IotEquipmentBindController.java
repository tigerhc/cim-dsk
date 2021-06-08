package com.lmrj.fab.eqp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lmrj.cim.utils.OfficeUtils;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.query.utils.QueryableConvertUtils;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.entity.IotEquipmentBind;
import com.lmrj.fab.eqp.service.IFabModelTemplateBodyService;
import com.lmrj.fab.eqp.service.IIotEquipmentBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author wdj
 * @date 2021-06-01 21:39
 */
@RestController
@RequestMapping("fab/iotequipmentbind")
@ViewPrefix("fab/iotequipmentbind")
@RequiresPathPermission("fab:iotequipmentbind")
public class IotEquipmentBindController extends BaseCRUDController<IotEquipmentBind> {
    @Autowired
    private IFabModelTemplateBodyService fabModelTemplateBodyService;
    @Autowired
    private IIotEquipmentBindService iotEquipmentBindService;


   @Override
   @RequestMapping(value = {"page"},method = {RequestMethod.GET, RequestMethod.POST})
    public void pageList(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) throws IOException {
        EntityWrapper<IotEquipmentBind> entityWrapper = new EntityWrapper(this.entityClass);
        this.prePage(queryable, entityWrapper, request, response);
        propertyPreFilterable.addQueryProperty(new String[]{"id"});
        QueryableConvertUtils.convertQueryValueToEntityValue(queryable, this.entityClass);
        SerializeFilter filter = propertyPreFilterable.constructFilter(this.entityClass);
        PageResponse<IotEquipmentBind> pagejson = new PageResponse(this.commonService.list(queryable, entityWrapper));
       List<IotEquipmentBind> list = pagejson.getResults();
       if(list==null||list.size()==0){
           if(queryable.getCondition()==null){

           }else{
               List<FabModelTemplateBody> bodyList =  fabModelTemplateBodyService.getNoBindInfo(queryable.getCondition().getFilterFor("parentEqpId").getValue().toString());
               //如果未查询到数据，向绑定表插入空设备号信息
               iotEquipmentBindService.insertBlankInfo(bodyList,queryable.getCondition().getFilterFor("parentEqpId").getValue().toString());
               pagejson = new PageResponse(this.commonService.list(queryable, entityWrapper));
           }



       }

        this.afterPage(pagejson, request, response);
        String content = JSON.toJSONString(pagejson, filter, new SerializerFeature[0]);
        ServletUtils.printJson(response, content);
    }


    /**
     * 在返回list数据之前编辑数据
     *
     * @param pagejson
     */
    @Override
    public void afterPage(PageResponse<IotEquipmentBind> pagejson, HttpServletRequest request, HttpServletResponse response) {
        List<IotEquipmentBind> list = pagejson.getResults();
        for(IotEquipmentBind iotEquipmentBind: list){
            //获取模板名称以及子设备类型
            List<String> varlist  =   fabModelTemplateBodyService.getSubAndName(iotEquipmentBind.getTemplateBodyId());
            iotEquipmentBind.setTemplateName(varlist.get(0));
            iotEquipmentBind.setSubClassCode(varlist.get(1));
        }
    }
    @Override
    public void afterFind(IotEquipmentBind iotEquipmentBind) {
        List<String> varlist  =   fabModelTemplateBodyService.getSubAndName(iotEquipmentBind.getTemplateBodyId());
        iotEquipmentBind.setTemplateName(varlist.get(0));
        iotEquipmentBind.setSubClassCode(varlist.get(1));
    }
}
