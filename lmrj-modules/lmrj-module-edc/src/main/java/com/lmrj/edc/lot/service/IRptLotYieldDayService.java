package com.lmrj.edc.lot.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.lot.entity.RptLotYieldDay;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.lot.service
 * @title: rpt_lot_yield_day服务接口
 * @description: rpt_lot_yield_day服务接口
 * @author: 张伟江
 * @date: 2020-05-17 21:10:56
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
public interface IRptLotYieldDayService extends ICommonService<RptLotYieldDay> {
    RptLotYieldDay findDayYield(String eqpId,Date startTime, Date endTime);

    List<RptLotYieldDay> selectDayYieldList(Date startTime, Date endTime);

    List<Map> pdtChart(String beginTime, String endTime, String line, String stationCode);

    List<Map> pdtChart(String beginTime, String endTime, String line, String stationCode,String eqpId);


    Integer findLotYield(String eqpId, String lotNo, Date startTime, Date endTime);

    List<String> findEqpId(String lineNo, String stationCode);

    List<Map<String,Object>> searchStand(String lineNo);

    List<Map<String,Object>> searchOperStand(String lineNo);

    List<Map<String,Object>> findEqp(String lineNo,String stationId,String date);

    List<Map<String,Object>> searchStandAndEqp(String lineNo);

    List<Map<String,Object>> findSonEqp(String lineNo,List<String> stationId);

    List<Map<String,Object>> findAllEqp(String beginTime, String endTime, String line, String stationCode);

    int deleteByDate(String periodDate,String lineNo);
}
