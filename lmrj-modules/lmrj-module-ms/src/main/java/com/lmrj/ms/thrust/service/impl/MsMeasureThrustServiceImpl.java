package com.lmrj.ms.thrust.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.ms.thrust.entity.MsMeasureThrust;
import com.lmrj.ms.thrust.mapper.MsMeasureThrustMapper;
import com.lmrj.ms.thrust.service.IMsMeasureThrustService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("msMeasureThrustService")
@Slf4j
public class MsMeasureThrustServiceImpl extends CommonServiceImpl<MsMeasureThrustMapper, MsMeasureThrust> implements IMsMeasureThrustService {

    @Test
    public List<MsMeasureThrust> findDataByTime(String produictionName , String startTime , String endTime){
        return baseMapper.findDataByTime(produictionName,startTime,endTime);
    }
}
