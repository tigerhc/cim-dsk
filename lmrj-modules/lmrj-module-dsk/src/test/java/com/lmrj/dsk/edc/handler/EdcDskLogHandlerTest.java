package com.lmrj.dsk.edc.handler;

import com.lmrj.dsk.DskBootApplication;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.OvnBatchLotParam;
import com.lmrj.oven.batchlot.service.IOvnBatchLotParamService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = DskBootApplication.class)
@RunWith(SpringRunner.class)
public class EdcDskLogHandlerTest {
    @Autowired
    IFabEquipmentService fabEquipmentService;
    @Autowired
    IOvnBatchLotService ovnBatchLotService;
    @Autowired
    IOvnBatchLotParamService iOvnBatchLotParamService;

    @Test
    public void parseTempHlog(){
        String msg = "{\"createDate\":\"2021-01-13 15:18:43 781\",\"delFlag\":\"0\",\"eqpId\":\"SIM-DM2\",\"startTime\":\"2021-01-13 15:18:43\",\"otherTempsTitle\":\"温度2当前值,温度3当前值,温度4当前值,温度5当前值,温度6当前值,温度7当前值,温度8当前值,温度9当前值,温度10当前值,\",\"ovnBatchLotParamList\":[{\"createDate\":\"2021-01-13 15:18:43 781\",\"delFlag\":\"0\",\"tempPv\":\"1000.0\",\"tempMin\":\"0\",\"tempMax\":\"0\",\"otherTempsValue\":\"1000.0,50,10,200,1000.0,50,10,200,1000.0,50,10,200,1000.0,50,10,200,1000.0,50,10,200,1000.0,50,10,200,1000.0,50,10,200,1000.0,50,10,200,1000.0,50,10,200,\"}]}";
        OvnBatchLot ovnBatchLot = JsonUtil.from(msg, OvnBatchLot.class);
        String eqpId = ovnBatchLot.getEqpId();
        if (StringUtil.isNotBlank(eqpId)) {
            if(!eqpId.equals("")){
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(eqpId);
                ovnBatchLot.setOfficeId(fabEquipment.getOfficeId());
            }
            Long time = ovnBatchLot.getStartTime().getTime()-24*60*60*1000;
            Date startTime = new Date(time);
            OvnBatchLot ovnBatchLot1 = ovnBatchLotService.findBatchData(eqpId,startTime);
            if(ovnBatchLot1!=null){
                List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                ovnBatchLot1.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                ovnBatchLotService.updateById(ovnBatchLot1);
                for (OvnBatchLotParam ovnBatchLotParam : OvnBatchLotParamList) {
                    ovnBatchLotParam.setBatchId(ovnBatchLot1.getId());
                }
                iOvnBatchLotParamService.insertBatch(OvnBatchLotParamList);
            }else{
                List<OvnBatchLotParam> OvnBatchLotParamList = ovnBatchLot.getOvnBatchLotParamList();
                ovnBatchLot.setEndTime(OvnBatchLotParamList.get(OvnBatchLotParamList.size() - 1).getCreateDate());
                ovnBatchLotService.insert(ovnBatchLot);
            }
        }
    }
}
