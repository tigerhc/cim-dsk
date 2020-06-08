package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionHisService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogProductionHisMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_production_his服务实现
* @description: edc_dsk_log_production_his服务实现
* @author: 张伟江
* @date: 2020-06-08 13:56:08
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcDskLogProductionHisService")
public class EdcDskLogProductionHisServiceImpl  extends CommonServiceImpl<EdcDskLogProductionHisMapper,EdcDskLogProductionHis> implements  IEdcDskLogProductionHisService {
}
