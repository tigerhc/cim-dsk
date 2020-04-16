package com.lmrj.edc.param.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.param.service.IEdcParamRecordHisService;
import com.lmrj.edc.param.entity.EdcParamRecordHis;
import com.lmrj.edc.param.mapper.EdcParamRecordHisMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.param.service.impl
* @title: edc_param_record_his服务实现
* @description: edc_param_record_his服务实现
* @author: zhangweijiang
* @date: 2019-06-14 23:31:46
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcParamRecordHisService")
public class EdcParamRecordHisServiceImpl  extends CommonServiceImpl<EdcParamRecordHisMapper,EdcParamRecordHis> implements  IEdcParamRecordHisService {

}