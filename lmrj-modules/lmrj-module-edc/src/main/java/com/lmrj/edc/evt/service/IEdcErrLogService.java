package com.lmrj.edc.evt.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.evt.entity.EdcErrLogBean;

public interface IEdcErrLogService extends ICommonService<EdcErrLogBean> {
    void saveErrMsg(EdcErrLogBean errMsg);
}
