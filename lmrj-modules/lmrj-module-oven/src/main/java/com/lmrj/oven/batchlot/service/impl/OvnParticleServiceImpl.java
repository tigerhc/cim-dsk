package com.lmrj.oven.batchlot.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.oven.batchlot.entity.ParticleDataBean;
import com.lmrj.oven.batchlot.mapper.OvnParticleMapper;
import com.lmrj.oven.batchlot.service.IOvnParticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service("ovnparticleService")
public class OvnParticleServiceImpl extends CommonServiceImpl<OvnParticleMapper, ParticleDataBean> implements IOvnParticleService {
}
