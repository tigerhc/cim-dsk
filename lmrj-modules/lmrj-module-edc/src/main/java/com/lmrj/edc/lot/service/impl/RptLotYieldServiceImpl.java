package com.lmrj.edc.lot.service.impl;

import com.lmrj.aps.plan.service.IApsPlanPdtYieldDetailService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.lot.entity.RptLotYield;
import com.lmrj.edc.lot.mapper.RptLotYieldMapper;
import com.lmrj.edc.lot.service.IRptLotYieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.lot.service.impl
* @title: rpt_lot_yield服务实现
* @description: rpt_lot_yield服务实现
* @author: 张伟江
* @date: 2020-05-17 21:10:40
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rptLotYieldService")
public class RptLotYieldServiceImpl  extends CommonServiceImpl<RptLotYieldMapper,RptLotYield> implements  IRptLotYieldService {
    @Autowired
    public IApsPlanPdtYieldDetailService apsPlanPdtYieldDetailService;

    @Override
    public List<Map> findLotYield(String line) {
        List<Map> yieldList = baseMapper.findLotYield(line);
        return yieldList;
    }
}
