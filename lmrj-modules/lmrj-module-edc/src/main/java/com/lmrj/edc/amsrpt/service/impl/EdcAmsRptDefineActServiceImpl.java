package com.lmrj.edc.amsrpt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineActService;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefineAct;
import com.lmrj.edc.amsrpt.mapper.EdcAmsRptDefineActMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.amsrpt.service.impl
* @title: edc_ams_rpt_define_act服务实现
* @description: edc_ams_rpt_define_act服务实现
* @author: zhangweijiang
* @date: 2020-02-15 02:46:34
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcAmsRptDefineActService")
public class EdcAmsRptDefineActServiceImpl  extends CommonServiceImpl<EdcAmsRptDefineActMapper,EdcAmsRptDefineAct> implements  IEdcAmsRptDefineActService {

}