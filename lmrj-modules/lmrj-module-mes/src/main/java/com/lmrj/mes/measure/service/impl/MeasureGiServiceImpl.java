package com.lmrj.mes.measure.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.measure.entity.MeasureGi;
import com.lmrj.mes.measure.mapper.MeasureGiMapper;
import com.lmrj.mes.measure.service.MeasureGiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("MeasureFGiService")
@Slf4j
public class MeasureGiServiceImpl extends CommonServiceImpl<MeasureGiMapper, MeasureGi> implements MeasureGiService {

}
