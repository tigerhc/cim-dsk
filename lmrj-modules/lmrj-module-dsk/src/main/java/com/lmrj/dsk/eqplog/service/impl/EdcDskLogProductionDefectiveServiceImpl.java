package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionDefective;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogProductionDefectiveMapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionDefectiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.service.impl
 * @title: EdcDskLogProductionDefective服务实现
 * @description: EdcDskLogProductionDefective服务实现
 * @author: 张伟江
 * @date: 2020-04-14 10:10:00
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("IEdcDskLogProductionDefectiveService")
@Slf4j
public class EdcDskLogProductionDefectiveServiceImpl extends CommonServiceImpl<EdcDskLogProductionDefectiveMapper, EdcDskLogProductionDefective> implements IEdcDskLogProductionDefectiveService {

}
