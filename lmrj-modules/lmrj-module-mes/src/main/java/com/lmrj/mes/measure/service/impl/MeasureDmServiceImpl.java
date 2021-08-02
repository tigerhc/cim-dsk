package com.lmrj.mes.measure.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.measure.entity.MeasureDm;
import com.lmrj.mes.measure.mapper.MeasureDmMapper;
import com.lmrj.mes.measure.service.MeasureDmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("MeasureDmService")
@Slf4j
public class MeasureDmServiceImpl extends CommonServiceImpl<MeasureDmMapper, MeasureDm> implements MeasureDmService {

}
