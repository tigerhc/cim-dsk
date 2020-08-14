package com.lmrj.edc.ams.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.ams.entity.EdcAmsRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.ams.service
* @title: edc_ams_record服务接口
* @description: edc_ams_record服务接口
* @author: 张伟江
* @date: 2019-06-14 15:51:23
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcAmsRecordService extends ICommonService<EdcAmsRecord> {
    List<EdcAmsRecord> selectAmsRecord(String officeId, String lineNo,String department, String fab);
    List<Map>  selectAlarmCountByLine(String beginTime, String endTime, String lineNo);
    List<Map>  selectAlarmCountByEqp(String beginTime, String endTime, String eqpId);
    List<EdcAmsRecord> findAmsRecordByTime(Date startTime, Date endTime);
}
