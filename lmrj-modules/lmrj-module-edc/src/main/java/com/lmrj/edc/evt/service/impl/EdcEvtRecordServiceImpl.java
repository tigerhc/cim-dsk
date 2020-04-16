package com.lmrj.edc.evt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.evt.service.IEdcEvtRecordService;
import com.lmrj.edc.evt.entity.EdcEvtRecord;
import com.lmrj.edc.evt.mapper.EdcEvtRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.evt.service.impl
* @title: edc_evt_record服务实现
* @description: edc_evt_record服务实现
* @author: 张伟江
* @date: 2019-06-14 16:09:50
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcEvtRecordService")
public class EdcEvtRecordServiceImpl  extends CommonServiceImpl<EdcEvtRecordMapper,EdcEvtRecord> implements  IEdcEvtRecordService {

}