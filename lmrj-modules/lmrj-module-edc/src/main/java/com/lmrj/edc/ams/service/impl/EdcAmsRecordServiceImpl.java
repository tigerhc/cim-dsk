package com.lmrj.edc.ams.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.mapper.EdcAmsRecordMapper;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.ams.service.impl
 * @title: edc_ams_record服务实现
 * @description: edc_ams_record服务实现
 * @author: 张伟江
 * @date: 2019-06-14 15:51:23
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("edcAmsRecordService")
public class EdcAmsRecordServiceImpl extends CommonServiceImpl<EdcAmsRecordMapper, EdcAmsRecord> implements IEdcAmsRecordService {

    @Override
    public List<EdcAmsRecord> selectAmsRecord(String officeId, String lineNo, String department, String fab) {
        return baseMapper.selectAmsRecord(officeId, lineNo, department, fab);
    }

    @Override
    public List<EdcAmsRecord> selectAmsRecordByTime(String department,Date startTime){
        return baseMapper.selectAmsRecordByTime(department,startTime);
    }

    @Override
    public List<Map> selectAlarmCountByLine(String beginTime, String endTime, String lineNo) {
        return baseMapper.selectAlarmCountByLine(beginTime, endTime, lineNo);
    }

    @Override
    public List<Map> selectAlarmCountByLineOther(String stationCode,String beginTime, String endTime, String lineNo) {
        return baseMapper.selectAlarmCountByLineOther(stationCode,beginTime, endTime, lineNo);
    }

    @Override
    public List<Map> selectAlarmCountByEqp(String beginTime, String endTime, String eqpId) {
        return baseMapper.selectAlarmCountByEqp(beginTime, endTime, eqpId);
    }

    @Override
    public List<EdcAmsRecord> findAmsRecordByTime(Date startTime, Date endTime) {
        return baseMapper.findAmsRecordByTime(startTime, endTime);
    }

    @Override
    public List<Map> selectAlarmCountByStation(String beginTime, String endTime, String lineNo, String stationCode) {
        return baseMapper.selectAlarmCountByStation(beginTime, endTime, lineNo, stationCode);
    }

    @Override
    public void addRecord(EdcAmsRecord amsRecord){
        baseMapper.insert(amsRecord);
    }
}
