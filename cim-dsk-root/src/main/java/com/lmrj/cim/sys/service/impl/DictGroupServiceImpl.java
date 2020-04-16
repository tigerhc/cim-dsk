package com.lmrj.cim.sys.service.impl;

import com.lmrj.cim.sys.service.IDictGroupService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.entity.DictGroup;
import com.lmrj.cim.modules.sys.mapper.DictGroupMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("dictGroupService")
public class DictGroupServiceImpl extends CommonServiceImpl<DictGroupMapper,DictGroup> implements IDictGroupService {
}
