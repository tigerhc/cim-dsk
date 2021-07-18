package com.lmrj.oven.batchlot.service.impl;

import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;
import com.lmrj.oven.batchlot.mapper.OvnBatchLotDayMapper;
import com.lmrj.oven.batchlot.service.IOvnBatchLotDayService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OvnBatchLotDayServiceImpl  implements IOvnBatchLotDayService {

    @Autowired
    private OvnBatchLotDayMapper ovnBatchLotDayMapper;

    @Override
    public List<OvnBatchLotDay> findDetail(String eqpId,String start, String end) {
        List<OvnBatchLotDay> detail = ovnBatchLotDayMapper.findDetail( eqpId ,start,end);
//        OvnBatchLotDay ovnBatchLotDay = detail.get( 3 );
//        OvnBatchLotDay ovnBatchLotDay1 = detail.get( 30 );
//        List<OvnBatchLotDay> list = new ArrayList<>(  );
//        list.add( ovnBatchLotDay );
//        list.add( ovnBatchLotDay1 );
        return detail;
    }

    @Override
    public List<OvnBatchLotDay> selectTime(String periodDate) {
        return ovnBatchLotDayMapper.selectTime( periodDate );
    }

    @Override
    public List<OvnBatchLotDay> selectMaxMin(String eqpId, String periodDate) {
        return ovnBatchLotDayMapper.selectMaxMin( eqpId,periodDate );
    }

    @Override
    public List<OvnBatchLotDay> selecTearlyData(String eqpId, String periodDate) {
        return ovnBatchLotDayMapper.selecTearlyData(eqpId,periodDate);
    }

    @Override
    public List<OvnBatchLotDay> selectLateData(String eqpId, String periodDate) {
        return ovnBatchLotDayMapper.selectLateData(eqpId,periodDate);
    }

    @Override
    public Integer oldData(String periodDate) {
        return ovnBatchLotDayMapper.oldData(periodDate );
    }
}
