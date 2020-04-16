package com.lmrj.edc.amsrpt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptRecordDtlService;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptRecordDtl;
import com.lmrj.edc.amsrpt.mapper.EdcAmsRptRecordDtlMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.amsrpt.service.impl
* @title: edc_ams_rpt_record_dtl服务实现
* @description: edc_ams_rpt_record_dtl服务实现
* @author: zhangweijiang
* @date: 2020-02-15 02:49:43
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcAmsRptRecordDtlService")
public class EdcAmsRptRecordDtlServiceImpl  extends CommonServiceImpl<EdcAmsRptRecordDtlMapper,EdcAmsRptRecordDtl> implements  IEdcAmsRptRecordDtlService {

}