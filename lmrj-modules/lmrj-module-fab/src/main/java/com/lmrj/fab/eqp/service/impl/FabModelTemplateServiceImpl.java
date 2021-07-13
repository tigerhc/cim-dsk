package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabModelTemplate;
import com.lmrj.fab.eqp.mapper.FabModelTemplateMapper;
import com.lmrj.fab.eqp.service.IFabModelTemplateBodyService;
import com.lmrj.fab.eqp.service.IFabModelTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 9:19
 */
@Transactional
@Service("fabmodeltemplateservice")
public class FabModelTemplateServiceImpl extends CommonServiceImpl<FabModelTemplateMapper, FabModelTemplate> implements IFabModelTemplateService {
    @Autowired
    private IFabModelTemplateBodyService fabModelTemplateBodyService;
    @Override
    public String getName(String id) {
        return baseMapper.selectById(id).getName();
    }

    /**
     * 生成模板并将信息返回
     * @param fabModelTemplate
     * @param a
     * @return
     */
    @Override
    public FabModelTemplate insertOrUpdate(FabModelTemplate fabModelTemplate, String a) {
        if(fabModelTemplate.getId()!=null&&!"".equals(fabModelTemplate.getId())){

            this.baseMapper.updateById(fabModelTemplate);
        }else{
            this.baseMapper.insert(fabModelTemplate) ;
        }
        return fabModelTemplate;
    }

    @Override
    public void deleteAll(String classCode) {
        FabModelTemplate fabModelTemplate = new FabModelTemplate();
        fabModelTemplate.setClassCode(classCode);
        fabModelTemplate = this.baseMapper.selectOne(fabModelTemplate);
        fabModelTemplateBodyService.deleteAndSave(new ArrayList<>(),fabModelTemplate);
        this.baseMapper.deleteById(fabModelTemplate.getId());
    }
}
