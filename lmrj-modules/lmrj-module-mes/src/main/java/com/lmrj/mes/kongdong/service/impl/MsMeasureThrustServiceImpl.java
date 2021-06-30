package com.lmrj.mes.kongdong.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.kongdong.entity.MsMeasureThrust;
import com.lmrj.mes.kongdong.mapper.MsMeasureThrustMapper;
import com.lmrj.mes.kongdong.service.IMsMeasureThrustService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("msMeasureThrustService")
@Slf4j
public class MsMeasureThrustServiceImpl extends CommonServiceImpl<MsMeasureThrustMapper, MsMeasureThrust> implements IMsMeasureThrustService {

}
