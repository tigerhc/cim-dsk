package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.api.entity.EdcparamApi;
import com.lmrj.core.api.service.IEdcparamApiService;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.entity.FabModelTemplate;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.entity.FabSensorModel;
import com.lmrj.fab.eqp.mapper.FabModelTemplateBodyMapper;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.fab.eqp.service.IFabModelTemplateBodyService;
import com.lmrj.fab.eqp.service.IFabModelTemplateService;
import com.lmrj.fab.eqp.service.IFabSensorModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 9:16
 */
@Transactional
@Service("fabmodeltemplatebodyservice")
public class FabModelTemplateBodyServiceImpl extends CommonServiceImpl<FabModelTemplateBodyMapper, FabModelTemplateBody> implements IFabModelTemplateBodyService {
    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;
    @Autowired
    private IFabSensorModelService fabSensorModelService;
    @Autowired
    private IFabModelTemplateService fabModelTemplateService;
    @Autowired
    private IEdcparamApiService edcparamApiService;

    @Override
    public List<Map> getOneTemplateList(String id) {
        return baseMapper.getOneTemplateList(id);
    }

    /**
     * 创建或更新
     * @param list
     */
    @Override
    public void deleteAndSave(List<FabModelTemplateBody> list, FabModelTemplate template) {
        //删除对应的绑定参数
        List<FabEquipmentModel>  fabEquipmentModelList =      fabEquipmentModelService.selectList(new EntityWrapper<FabEquipmentModel>().eq("class_code",template.getClassCode()));
        String modelId = fabEquipmentModelList.get(0).getId();
        edcparamApiService.delete(new EntityWrapper<EdcparamApi>().eq("model_id",modelId).isNotNull("param_define_id"));
        //删除所有主体记录
        baseMapper.delete(new EntityWrapper().eq("template_id",template.getId()));
        //按需生成
        for (FabModelTemplateBody body:list) {
            FabModelTemplateBody bodyi = new FabModelTemplateBody();
            bodyi.setTemplateId(template.getId());
            bodyi.setTemplateName(template.getName());
            bodyi.setParentType(body.getParentType());
            bodyi.setType(body.getType());
            bodyi.setSubClassCode(body.getSubClassCode());
            bodyi.setManufacturerName( fabEquipmentModelService.manufacturerName(body.getSubClassCode()));
            bodyi.setDelFlag("0");
            baseMapper.insert(bodyi);
            //将绑定的传感器对应的参数放入对应设备类型中
            FabSensorModel fabSensorModel =  fabSensorModelService.selectList(new EntityWrapper<FabSensorModel>().eq("class_code",body.getSubClassCode())).get(0);
            EdcparamApi api = edcparamApiService.selectOne(new EntityWrapper<EdcparamApi>().eq("class_code",body.getSubClassCode()).eq("param_define_id",fabSensorModel.getId()).isNull("model_id"));
            api.setId("");
            api.setModelId("modelId");
            edcparamApiService.insertOrUpdateAll(api);
        }

    }

    /**
     *
     * @param eqpId
     * @return
     */
    @Override
    public List<FabModelTemplateBody> getNoBindInfo(String eqpId) {
        return baseMapper.getNoBindInfo(eqpId);
    }

    @Override
    public List<String> getSubAndName(String Id) {
        List<String> retlist = new ArrayList<>();
        FabModelTemplateBody body = baseMapper.selectById(Id);
        //获取对应的模板名称
        String name = fabModelTemplateService.getName(body.getTemplateId());
        retlist.add(0,name);
        retlist.add(1,body.getSubClassCode());
        return retlist;
    }

    /**
     * 变更设备模板名称
     * @param id
     * @param name
     */
    @Override
    public void chageName(String id, String name) {
        List<FabModelTemplateBody> fabModelTemplateBodies = baseMapper.selectList(new EntityWrapper().eq("template_id",id));
       if(fabModelTemplateBodies!=null&&fabModelTemplateBodies.size()>0){
           FabModelTemplateBody fabModelTemplateBody = new FabModelTemplateBody();
           fabModelTemplateBody.setTemplateId(fabModelTemplateBodies.get(0).getId());
           fabModelTemplateBody.setTemplateName(name);
           baseMapper.updateById(fabModelTemplateBody);
       }
    }
}
