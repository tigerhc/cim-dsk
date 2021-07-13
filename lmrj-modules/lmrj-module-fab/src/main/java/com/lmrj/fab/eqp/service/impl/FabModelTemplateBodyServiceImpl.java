package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.mapper.FabModelTemplateBodyMapper;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import com.lmrj.fab.eqp.service.IFabModelTemplateBodyService;
import com.lmrj.fab.eqp.service.IFabModelTemplateService;
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
    private IFabModelTemplateService fabModelTemplateService;

    @Override
    public List<Map> getOneTemplateList(String id) {
        return baseMapper.getOneTemplateList(id);
    }

    /**
     * 创建或更新
     * @param list
     * @param id
     * @param name
     */
    @Override
    public void deleteAndSave(List<FabModelTemplateBody> list, String id, String name) {
        //删除所有主体记录
        baseMapper.delete(new EntityWrapper().eq("template_id",id));
        //按需生成
        for (FabModelTemplateBody body:list) {
            int i = Integer.valueOf(body.getNum());
            FabModelTemplateBody bodyi = new FabModelTemplateBody();
            bodyi.setTemplateId(id);
            bodyi.setTemplateName(name);
            bodyi.setParentType(body.getParentType());
            bodyi.setType(body.getType());
            bodyi.setSubClassCode(body.getSubClassCode());
            bodyi.setManufacturerName( fabEquipmentModelService.manufacturerName(body.getSubClassCode()));
            bodyi.setDelFlag("0");
            for (int j = 0; j < i; j++) {
                    baseMapper.insert(bodyi);
                }


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
