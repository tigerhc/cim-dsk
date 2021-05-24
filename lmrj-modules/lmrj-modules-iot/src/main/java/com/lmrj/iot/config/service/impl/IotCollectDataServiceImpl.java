package com.lmrj.iot.config.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.ams.entity.EdcAmsDefine;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import com.lmrj.edc.ams.service.IEdcAmsDefineService;
import com.lmrj.edc.ams.service.IEdcAmsRecordService;
import com.lmrj.edc.param.service.IEdcParamDefineModelService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.iot.config.entity.IotCollectData;
import com.lmrj.iot.config.entity.IotCollectDataHis;
import com.lmrj.iot.config.mapper.IotCollectDataMapper;
import com.lmrj.iot.config.service.IIotCollectDataHisService;
import com.lmrj.iot.config.service.IIotCollectDataService;
import com.lmrj.util.mapper.JsonUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.iot.config.service.impl
* @title: ot_collect_data服务实现
* @description:ot_collect_data服务实现
* @author: wdj
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("iotCollectDataService")
public class IotCollectDataServiceImpl extends CommonServiceImpl<IotCollectDataMapper, IotCollectData> implements IIotCollectDataService {
    @Autowired
    private IIotCollectDataHisService hisService;
    @Autowired
    private IEdcAmsRecordService edcAmsRecordService;
    @Autowired
    private IEdcAmsDefineService edcAmsDefineService;
    @Autowired
    private IEdcParamDefineModelService iEdcParamDefineModelService;
    @Autowired
    private IFabEquipmentService iFabEquipmentService;

   /* @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.IOT.COLLECTDATA"})*/
    public void repeatAlarm(String msg) {
        Map<String, String> msgMap = JsonUtil.from(msg, Map.class);
        IotCollectData iotCollectData = JsonUtil.from(msgMap.get("IOTCOLLECTDATA"), IotCollectData.class);
        try {
            this.collectDataAndAlarm(iotCollectData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 采集数据并校验下发警报
     * @param fromdata
     */
    @Override
    public void collectDataAndAlarm(IotCollectData fromdata) {
        //mq的消息先进入快照 并归档上次数据
        IotCollectData data = new IotCollectData();
        data.setCollectType(fromdata.getCollectType());
        data.setEqpId(fromdata.getEqpId());
        data.setNumType(fromdata.getNumType());
        data.setNumMultiple(fromdata.getNumMultiple());
        IotCollectData lastdata = baseMapper.selectOne(data);
        if(lastdata!=null){
            fromdata.setLastNum(lastdata.getThisNum());
            IotCollectDataHis enddata = new IotCollectDataHis();
            //SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            BeanUtils.copyProperties(lastdata,enddata);
            enddata.setEndDate(new Date());
            hisService.endDate(enddata);
            baseMapper.deleteById(lastdata.getId());
        }else{
            fromdata.setLastNum("0");
         }
        baseMapper.insert(fromdata);
      //判断是否进入报警record
        //1直接判断
        if(fromdata.getAlarmFlag()!=null&&"1".equals(fromdata.getAlarmFlag())){
            EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
            EdcAmsDefine edcAmsDefine = edcAmsDefineService.selectByCode(fromdata.getAlarmCode());
            edcAmsRecord.setEqpId(fromdata.getEqpId());
            edcAmsRecord.setAlarmCode(fromdata.getAlarmCode());
            edcAmsRecord.setAlarmName(edcAmsDefine.getAlarmName());
            edcAmsRecord.setAlarmDetail(edcAmsDefine.getAlarmDesc());
            edcAmsRecord.setCreateDate(new Date());
            edcAmsRecord.setStartDate(fromdata.getCollectDate());
            edcAmsRecord.setAlarmSwitch("1");
            edcAmsRecordService.addRecord(edcAmsRecord);
        }
        //edit by wdj 2021.5.20  2值判断(edc_param_define edc_param_define_model)
        /**
         * 校验失败写入警报记录表
         */
        //1  校验开始
        if(fromdata.getAlarmFlag()==null||"".equals(fromdata.getAlarmFlag())){
          //获取设定值没有则取阈值 输入参数
            // 1 示数类型
            // 2 设备Id
          List<String> valueList =  iEdcParamDefineModelService.getParamValues(fromdata.getEqpId(),fromdata.getNumType());
          FabEquipment fabEquipment = iFabEquipmentService.findEqpByCode(fromdata.getEqpId());
          String alarmCode = fabEquipment.getModelName()+fromdata.getNumType();
          if(valueList!=null&&valueList.size()>0){
              if(valueList.size()==1){
            //设定值判读
                    if(!valueList.get(0).equals(fromdata.getThisNum())){
                    //写入警报
                        EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
                        EdcAmsDefine edcAmsDefine = edcAmsDefineService.selectByCode(alarmCode);
                        edcAmsRecord.setEqpId(fromdata.getEqpId());
                        edcAmsRecord.setAlarmCode(alarmCode);
                        edcAmsRecord.setAlarmName(edcAmsDefine.getAlarmName());
                        edcAmsRecord.setAlarmDetail(edcAmsDefine.getAlarmDesc());
                        edcAmsRecord.setCreateDate(new Date());
                        edcAmsRecord.setStartDate(fromdata.getCollectDate());
                        edcAmsRecord.setAlarmSwitch("1");
                        edcAmsRecordService.addRecord(edcAmsRecord);
              }
              }else{
            //阈值判断
                  if(Integer.valueOf(valueList.get(0))<Integer.valueOf(fromdata.getThisNum())||Integer.valueOf(valueList.get(1))>Integer.valueOf(fromdata.getThisNum())){
                      //写入警报
                      EdcAmsRecord edcAmsRecord = new EdcAmsRecord();
                      EdcAmsDefine edcAmsDefine = edcAmsDefineService.selectByCode(alarmCode);
                      edcAmsRecord.setEqpId(fromdata.getEqpId());
                      edcAmsRecord.setAlarmCode(alarmCode);
                      edcAmsRecord.setAlarmName(edcAmsDefine.getAlarmName());
                      edcAmsRecord.setAlarmDetail(edcAmsDefine.getAlarmDesc());
                      edcAmsRecord.setCreateDate(new Date());
                      edcAmsRecord.setStartDate(fromdata.getCollectDate());
                      edcAmsRecord.setAlarmSwitch("1");
                      edcAmsRecordService.addRecord(edcAmsRecord);
                  }


              }
          }

        }


    }


}
