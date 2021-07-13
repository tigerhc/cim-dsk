package com.lmrj.core.api.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.api.entity.EdcparamApi;
import com.lmrj.core.api.mapper.EdcparamApiMapper;
import com.lmrj.core.api.service.IEdcparamApiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("edcparamApiService")
public class EdcparamApiServiceImpl extends CommonServiceImpl<EdcparamApiMapper, EdcparamApi> implements IEdcparamApiService {
    @Override
    public void insertOrUpdateAll(EdcparamApi edcparamApi) {
        if(edcparamApi.getId()!=null&&!"".equals(edcparamApi.getId())){
            this.baseMapper.updateById(edcparamApi);
        }else{
            this.baseMapper.insert(edcparamApi);
        }

    }
}
