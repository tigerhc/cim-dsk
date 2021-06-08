package com.lmrj.fab.eqp.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabModelTemplate;
import com.lmrj.fab.eqp.mapper.FabModelTemplateMapper;
import com.lmrj.fab.eqp.service.IFabModelTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public String getName(String id) {
        return baseMapper.selectById(id).getName();
    }
}
