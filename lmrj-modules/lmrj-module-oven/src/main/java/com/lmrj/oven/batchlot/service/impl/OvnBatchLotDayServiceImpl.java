package com.lmrj.oven.batchlot.service.impl;

import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;
import com.lmrj.oven.batchlot.mapper.OvnBatchLotDayMapper;
import com.lmrj.oven.batchlot.service.IOvnBatchLotDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OvnBatchLotDayServiceImpl  implements IOvnBatchLotDayService {

    @Autowired
    private OvnBatchLotDayMapper ovnBatchLotDayMapper;

    @Override
    public List<OvnBatchLotDay> findDetail(String eqpId) {
       return ovnBatchLotDayMapper.findDetail( eqpId );
    }

    @Override
    public List<OvnBatchLotDay> selectTime(String periodDate) {
        return ovnBatchLotDayMapper.selectTime( periodDate );
    }
}
