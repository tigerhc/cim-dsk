package com.lmrj.edc.evt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.evt.entity.EdcErrLogBean;
import com.lmrj.edc.evt.mapper.EdcErrLogMapper;
import com.lmrj.edc.evt.service.IEdcErrLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("edcErrLogService")
public class IEdcErrLogServiceImpl extends CommonServiceImpl<EdcErrLogMapper, EdcErrLogBean> implements IEdcErrLogService {
    @Override
    public void saveErrMsg(EdcErrLogBean errMsg) {
        baseMapper.saveErrMsg(errMsg);
    }
}
