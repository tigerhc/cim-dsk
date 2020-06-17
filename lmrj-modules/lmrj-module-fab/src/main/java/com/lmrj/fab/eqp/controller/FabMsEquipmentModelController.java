package com.lmrj.fab.eqp.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.query.annotation.QueryableDefaults;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.query.utils.QueryableConvertUtils;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.User;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.controller
 * @title: fab_equipment_model控制器
 * @description: fab_equipment_model控制器
 * @author: kang
 * @date: 2019-06-07 22:18:19
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("fab/fabmsequipmentmodel")
@ViewPrefix("eqpmodel/fabmsequipmentmodel")
@RequiresPathPermission("FabMsEquipmentModel")
@LogAspectj(title = "fab_equipment_model")
public class FabMsEquipmentModelController extends BaseCRUDController<FabEquipmentModel> {

    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    @RequestMapping(value = "page", method = { RequestMethod.GET, RequestMethod.POST })
    //@PageableDefaults(sort = "id=desc") 不再默认配置
    public void pageList(@QueryableDefaults(value = {"category||eq=MS"}) Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        EntityWrapper<FabEquipmentModel> entityWrapper = new EntityWrapper<FabEquipmentModel>(entityClass);
        prePage(queryable,entityWrapper, request, response);
        propertyPreFilterable.addQueryProperty("id");
        // 预处理
        QueryableConvertUtils.convertQueryValueToEntityValue(queryable, entityClass);
        SerializeFilter filter = propertyPreFilterable.constructFilter(entityClass);
        PageResponse<FabEquipmentModel> pagejson = new PageResponse<FabEquipmentModel>(commonService.list(queryable,entityWrapper));
        afterPage(pagejson, request, response);
        String content = JSON.toJSONString(pagejson, filter);
        ServletUtils.printJson(response,content);
    }


    /**
     * 在返回对象之前编辑数据
     *
     * @param entity
     */
    @Override
    public void afterFind(FabEquipmentModel entity) {
        //创建人
        if(StringUtil.isNotBlank(entity.getCreateBy())){
            User creater = UserUtil.getUser(entity.getCreateBy());
            if(creater != null){
                entity.setCreateByName(creater.getUsername());
            }
        }
        //更新人
        if(StringUtil.isNotBlank(entity.getUpdateBy())){
            User updater = UserUtil.getUser(entity.getUpdateBy());
            if(updater != null){
                entity.setUpdateByName(updater.getUsername());
            }
        }
    }

    /**
     * 在返回list数据之前编辑数据
     *
     * @param pagejson
     */
    @Override
    public void afterList(DateResponse pagejson, HttpServletRequest request, HttpServletResponse response) {
        List<FabEquipmentModel> list = (List<FabEquipmentModel>) pagejson.getResults();
        for(FabEquipmentModel fabEquipmentModel: list){
            fabEquipmentModel.setModelName(fabEquipmentModel.getManufacturerName()+"-"+fabEquipmentModel.getClassCode());
        }
    }

}
