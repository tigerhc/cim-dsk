package com.lmrj.edc.particle.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.particle.entity.ParticleDataBean;

import java.util.List;
import java.util.Map;

public interface IEdcParticleService extends ICommonService<ParticleDataBean> {

    Map<String, Object> getEchartData(Map<String, Object> params);

    List<Map<String, Object>> getParticleEqps();

    void backData();
}
