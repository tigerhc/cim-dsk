package com.lmrj.mes.lot.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.lot.service.IMesLotWipService;
import com.lmrj.mes.lot.entity.MesLotWip;
import com.lmrj.mes.lot.mapper.MesLotWipMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.mes.lot.service.impl
* @title: mes_lot_wip服务实现
* @description: mes_lot_wip服务实现
* @author: 张伟江
* @date: 2020-08-05 10:32:55
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("mesLotWipService")
public class MesLotWipServiceImpl  extends CommonServiceImpl<MesLotWipMapper,MesLotWip> implements  IMesLotWipService {

}