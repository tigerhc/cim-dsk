package com.lmrj.cim.fab.service.impl;

import com.lmrj.cim.fab.entity.FabEquipmentAccount;
import com.lmrj.cim.fab.mapper.FabEquipmentAccountMapper;
import com.lmrj.cim.fab.service.FabEquipmentAccountService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("fabEquipmentAccountService")
public class FabEquipmentAccountServiceImpl extends CommonServiceImpl<FabEquipmentAccountMapper, FabEquipmentAccount> implements FabEquipmentAccountService {

}
