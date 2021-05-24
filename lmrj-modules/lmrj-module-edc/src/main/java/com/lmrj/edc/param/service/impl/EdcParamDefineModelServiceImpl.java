package com.lmrj.edc.param.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.param.service.IEdcParamDefineModelService;
import com.lmrj.edc.param.entity.EdcParamDefineModel;
import com.lmrj.edc.param.mapper.EdcParamDefineModelMapper;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.param.service.impl
* @title: edc_param_define_model服务实现
* @description: edc_param_define_model服务实现
* @author: zhangweijiang
* @date: 2019-06-14 23:14:33
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcParamDefineModelService")
public class EdcParamDefineModelServiceImpl  extends CommonServiceImpl<EdcParamDefineModelMapper,EdcParamDefineModel> implements  IEdcParamDefineModelService {
    @Autowired
    private IFabEquipmentService iFabEquipmentService;
    /**
     * 通过设备号及示数类型获取设定值或阈值
     * @param eqpId
     * @param numType
     * @return
     */
    @Override
    public List<String> getParamValues(String eqpId, String numType) {
        List<String> retList = new ArrayList<>();
        //获取设备类型及厂商
       FabEquipment fab = iFabEquipmentService.findEqpByCode(eqpId);
       //拼接获取参数（厂商+"-"+设备型号+示数类型）
       String paramCode = fab.getModelName()+numType;
       Map<String,Object> map = new HashMap();
       map.put("param_code",paramCode);
       map.put("sub_eqp_id",eqpId);
       map.put("del_flag","0");
       map.put("model_name",fab.getModelName());
       boolean flag = true;
       List<EdcParamDefineModel>  retModel =  baseMapper.selectList(new EntityWrapper<EdcParamDefineModel>().allEq(map));
        //判断设定值和阈值的存在(EdcParamDefineModel)
       if(retModel.size()>0){
           if(retModel.get(0).getSetValue()!=null&&!"".equals(retModel.get(0).getSetValue())){
               flag = false;
               retList.add(0,retModel.get(0).getSetValue());

           } else if(retModel.get(0).getMaxValue()!=null&&!"".equals(retModel.get(0).getMaxValue())
           &&retModel.get(0).getMinValue()!=null&&!"".equals(retModel.get(0).getMinValue())){
               flag = false;
               retList.add(0,retModel.get(0).getMaxValue());
               retList.add(1,retModel.get(0).getMinValue());
           }
       }
       //如果子类设备未有则只需按设备类型索引(EdcParamDefineModel)
        if(flag){
            map.put("sub_eqp_id","");
            retModel =  baseMapper.selectList(new EntityWrapper<EdcParamDefineModel>().allEq(map));
            if(retModel.size()>0){
                if(retModel.get(0).getSetValue()!=null&&!"".equals(retModel.get(0).getSetValue())){
                    flag = false;
                    retList.add(0,retModel.get(0).getSetValue());

                } else if(retModel.get(0).getMaxValue()!=null&&!"".equals(retModel.get(0).getMaxValue())
                        &&retModel.get(0).getMinValue()!=null&&!"".equals(retModel.get(0).getMinValue())){
                    flag = false;
                    retList.add(0,retModel.get(0).getMaxValue());
                    retList.add(1,retModel.get(0).getMinValue());
                }
            }
        }

        return retList;
    }
}