package com.lmrj.ms.eqp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.ms.eqp.entity.MsEqpAblility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.eqp.controller
 * @title: ms_eqp_ablility控制器
 * @description: ms_eqp_ablility控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:20:21
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/mseqpablility")
@ViewPrefix("ms/mseqpablility")
@RequiresPathPermission("ms:mseqpablility")
@LogAspectj(title = "ms_eqp_ablility")
public class MsEqpAblilityController extends BaseCRUDController<MsEqpAblility> {
    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;
    @RequestMapping(value = {"list/{eqpModelId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Response findByModelId(@PathVariable String eqpModelId){
        FabEquipmentModel model = fabEquipmentModelService.selectOne(new EntityWrapper<FabEquipmentModel>().eq("eqp_model_id",eqpModelId));
        if(model == null){
            return DateResponse.error(eqpModelId+"不存在");
        }
        List<MsEqpAblility> list = commonService.selectList(new EntityWrapper<MsEqpAblility>().eq("eqp_model_id",eqpModelId));
        Response res = DateResponse.ok(list);

        res.put("eqpModelId", eqpModelId);
        res.put("eqpModelName", model.getManufacturerName());
        return DateResponse.ok(list);
    }

}
