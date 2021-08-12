package com.lmrj.oven.batchlot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotDay;
import com.lmrj.oven.batchlot.mapper.OvnBatchLotDayMapper;
import com.lmrj.oven.batchlot.service.IOvnBatchLotDayService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Slf4j
@Service
public class OvnBatchLotDayServiceImpl extends CommonServiceImpl<OvnBatchLotDayMapper, OvnBatchLotDay> implements IOvnBatchLotDayService {

    @Autowired
    private OvnBatchLotDayMapper ovnBatchLotDayMapper;
    @Autowired
    private IOvnBatchLotService ovnBatchLotService;
    @Autowired
    private IOvnBatchLotParamService ovnBatchLotParamService;

    @Override
    public List<OvnBatchLotDay> findDetail(String eqpId, String start, String end) {
        List<OvnBatchLotDay> detail = ovnBatchLotDayMapper.findDetail(eqpId, start, end);
//        OvnBatchLotDay ovnBatchLotDay = detail.get( 3 );
//        OvnBatchLotDay ovnBatchLotDay1 = detail.get( 30 );
//        List<OvnBatchLotDay> list = new ArrayList<>(  );
//        list.add( ovnBatchLotDay );
//        list.add( ovnBatchLotDay1 );
        return detail;
    }

    @Override
    public List<OvnBatchLotDay> selectTime(String periodDate) {
        return ovnBatchLotDayMapper.selectTime(periodDate);
    }

    @Override
    public List<OvnBatchLotDay> selectMaxMin(String eqpId, String periodDate) {
        return ovnBatchLotDayMapper.selectMaxMin(eqpId, periodDate);
    }

    @Override
    public List<OvnBatchLotDay> selecTearlyData(String eqpId, String periodDate) {
        return ovnBatchLotDayMapper.selecTearlyData(eqpId, periodDate);
    }

    @Override
    public List<OvnBatchLotDay> selectLateData(String eqpId, String periodDate) {
        return ovnBatchLotDayMapper.selectLateData(eqpId, periodDate);
    }

    @Override
    public Integer oldData(String periodDate) {
        return ovnBatchLotDayMapper.oldData(periodDate);
    }

    /**
     * 获取拼接老版数据并生成day数据
     *
     * @param eqpId
     * @param periodDate/YYYY-MM-dd/
     */
    @Override
    public void fParamToDay(String eqpId, String periodDate) {
        //获取Lot数据
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodDate + " 00:00:00");
            Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(periodDate + " 23:59:59");
            List<OvnBatchLot> ovnBatchLots = ovnBatchLotService.selectList(new EntityWrapper<OvnBatchLot>().eq("eqp_id", eqpId).between("create_date", startTime, endTime));
            if (ovnBatchLots != null && ovnBatchLots.size() > 0) {
                List<Map> list = new ArrayList<>();
                Map<String, String> map = new HashMap<String, String>();
                String[] titles = ovnBatchLots.get(0).getOtherTempsTitle().split(",");
                Map<String, Integer> titleOrderMap = new HashMap<>();
                for (int i = 1; i < titles.length; i++) {
                    map = new HashMap<String, String>();
                    map.put("indexPv", String.valueOf((i - 1) * 4 + 1));
                    map.put("id", ovnBatchLots.get(0).getId());
                    map.put("eqpId", ovnBatchLots.get(0).getEqpId());
                    map.put("eqpTemp", titles[i]);
                    map.put("tableName", "a" + i);
                    titleOrderMap.put(titles[i],i);
                    list.add(map);
                }
                try {
                    if (list.size() > 0) {
                        List<Map> retlist = baseMapper.fParamToDay(list, startTime, endTime, periodDate);
                        int index = 0;
                        for (Map retmap : retlist) {
                            OvnBatchLotDay ovnBatchLotDay = new OvnBatchLotDay();
                            ovnBatchLotDay.setEqpId(retmap.get("eqpid").toString());
                            ovnBatchLotDay.setEqpTemp(retmap.get("eqptemp").toString());
                            ovnBatchLotDay.setTempMax(retmap.get("tempMax").toString());
                            ovnBatchLotDay.setTempMin(retmap.get("tempMin").toString());
                            ovnBatchLotDay.setTempStart(retmap.get("tempStart").toString());
                            ovnBatchLotDay.setTempEnd(retmap.get("tempEnd").toString());
                            ovnBatchLotDay.setPeriodDate(retmap.get("periodDate").toString());
                            ovnBatchLotDay.setTitleOrder(titleOrderMap.get(retmap.get("eqptemp")));
                            ovnBatchLotDay.setId(UUID.randomUUID().toString().replace("-","").replace(" ","")+(index++));
                            try {
                                baseMapper.insert(ovnBatchLotDay);
                            } catch (Exception e) {
                                log.error("ovnBatchLotDay 数据插入出错  "+ovnBatchLotDay.getId(),e);
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("ovnBatchLotDay 数据插入出错  ",e);
                    e.printStackTrace();
                }
                try {
                    //插入第一条
                    OvnBatchLotDay ovnBatchLotDay = new OvnBatchLotDay();
                    ovnBatchLotDay.setEqpId(ovnBatchLots.get(0).getEqpId());
                    ovnBatchLotDay.setEqpTemp(titles[0]);
                    List<Map> list1 = ovnBatchLotParamService.fParamToDayone(ovnBatchLots.get(0).getId(), startTime, endTime, periodDate, eqpId, titles[0]);
                    ovnBatchLotDay.setEqpId(list1.get(0).get("eqpId").toString());
                    ovnBatchLotDay.setEqpTemp(list1.get(0).get("eqpTemp").toString());
                    ovnBatchLotDay.setTempMax(list1.get(0).get("tempMax").toString());
                    ovnBatchLotDay.setTempMin(list1.get(0).get("tempMin").toString());
                    ovnBatchLotDay.setTempStart(list1.get(0).get("tempStart").toString());
                    ovnBatchLotDay.setTempEnd(list1.get(0).get("tempEnd").toString());
                    ovnBatchLotDay.setPeriodDate(list1.get(0).get("periodDate").toString());
                    ovnBatchLotDay.setTitleOrder(0);
                    try {
                        baseMapper.insert(ovnBatchLotDay);
                    } catch (Exception e) {
                        log.error("ovnBatchLotDay插入第一条数据,插入出错 "+ovnBatchLotDay.getId(),e);
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    log.error("ovnBatchLotDay插入第一条数据,插入出错 ",e);
                    e.printStackTrace();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
<<<<<<< .merge_file_a13268
=======

    @Override
    public List<String> getTitleByEqpId(String eqpId) {
        return baseMapper.selectTitle(eqpId);
    }
>>>>>>> .merge_file_a01984
}
