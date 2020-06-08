package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionHis;
import com.lmrj.dsk.eqplog.mapper.EdcDskLogProductionMapper;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.dsk.eqplog.service.impl
* @title: edc_dsk_log_production服务实现
* @description: edc_dsk_log_production服务实现
* @author: 张伟江
* @date: 2020-04-14 10:10:00
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcDskLogProductionService")
public class EdcDskLogProductionServiceImpl  extends CommonServiceImpl<EdcDskLogProductionMapper,EdcDskLogProduction> implements  IEdcDskLogProductionService {

    //@Override
    //public boolean insert(EdcDskLogProduction edcDskLogProduction) {
    //    // 保存主表
    //    super.insert(edcDskLogProduction);
    //    // 保存细表
    //    List<EdcDskLogRecipeBody> edcDskLogRecipeBodyList = edcDskLogProduction.get();
    //    for (EdcDskLogRecipeBody edcDskLogRecipeBody : edcDskLogRecipeBodyList) {
    //        edcDskLogRecipeBody.setRecipeLogId(edcDskLogRecipe.getId());
    //    }
    //    edcDskLogRecipeBodyService.insertBatch(edcDskLogRecipeBodyList);
    //    return true;
    //}

    //production日志定时清除
    //清除批次中间运行数据,只保留开始和结束
    public boolean clearMiddleProductionLog() {
        //select lot_no,eqp_id  from edc_dsk_log_production where start_time between  '2020-05-05 09:51:44' and '2020-05-06 09:51:44'
        //group by lot_no
        //
        //select * from edc_dsk_log_production where lot_no = '0505B' and eqp_id='SIM-PRINTER1' order by start_time desc limit 1
        //
        //select * from edc_dsk_log_production where lot_no = '0505B' and eqp_id='SIM-PRINTER1' order by start_time asc limit 1
        //delete from edc_dsk_log_production where lot_no = '0505B1' and eqp_id='SIM-PRINTER1'  and (id != '11' or id != '2')
        return true;
    }
  //获取当前产量
    public boolean clearMiddleProductionLog2() {
        return true;
    }


    @Override
    public EdcDskLogProduction findNextYield(String eqpId, Date startTime) {
        EdcDskLogProduction edcDskLogProduction = baseMapper.findNextYield(eqpId, startTime);
        return edcDskLogProduction;

    }

    @Override
    public List<EdcDskLogProductionHis> findBackUpYield(Date startTime, Date endTime) {
        List<EdcDskLogProductionHis> hisList = new LinkedList<>();
        List<String> deleteList = new LinkedList<>();
        List<EdcDskLogProduction> yields = baseMapper.findYields(startTime, endTime);
        for (int i = 0;i < yields.size(); i++){
            if(yields.get(i).getDayYield() == 1 || yields.get(i).getLotYield() == 1){
                continue;
            }else {
                if (i == yields.size()-1){
                    break;
                }else {
                    if (yields.get(i + 1).getDayYield() == 1 || yields.get(i + 1).getLotYield() == 1){
                        continue;
                    }else {
                        deleteList.add(yields.get(i).getId());
                        EdcDskLogProductionHis edcDskLogProductionHis = new EdcDskLogProductionHis(yields.get(i));
                        hisList.add(edcDskLogProductionHis);
                    }
                }

            }
        }
        super.deleteBatchIds(deleteList);
        return hisList;
    }


}
