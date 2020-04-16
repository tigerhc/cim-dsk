package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.service.IEdcDskLogOperationService;
import com.lmrj.dsk.eqplog.entity.EdcDskLogOperation;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogOperationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_operation服务实现
* @description: edc_dsk_log_operation服务实现
* @author: 张伟江
* @date: 2020-04-14 10:10:16
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcDskLogOperationService")
public class EdcDskLogOperationServiceImpl  extends CommonServiceImpl<EdcDskLogOperationMapper,EdcDskLogOperation> implements  IEdcDskLogOperationService {

}