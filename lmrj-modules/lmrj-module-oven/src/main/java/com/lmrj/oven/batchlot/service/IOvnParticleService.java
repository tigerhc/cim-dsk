package com.lmrj.oven.batchlot.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.oven.batchlot.entity.ParticleDataBean;

import java.util.List;
import java.util.Map;

public interface IOvnParticleService extends ICommonService<ParticleDataBean> {

    Map<String, Object> getEchartData(Map<String, Object> params);

    List<Map<String, Object>> getParticleEqps();
}
