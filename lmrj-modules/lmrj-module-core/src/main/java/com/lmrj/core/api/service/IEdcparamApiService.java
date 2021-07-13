package com.lmrj.core.api.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.api.entity.EdcparamApi;

public interface IEdcparamApiService extends ICommonService<EdcparamApi> {
    void insertOrUpdateAll(EdcparamApi edcparamApi);
}
