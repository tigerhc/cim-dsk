package com.lmrj.edc.amsrpt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefineAct;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefineActEmail;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineActEmailService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineActService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptDefineService;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefine;
import com.lmrj.edc.amsrpt.mapper.EdcAmsRptDefineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.amsrpt.service.impl
* @title: edc_ams_rpt_define服务实现
* @description: edc_ams_rpt_define服务实现
* @author: zhangweijiang
* @date: 2020-02-15 02:46:43
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcAmsRptDefineService")
public class EdcAmsRptDefineServiceImpl  extends CommonServiceImpl<EdcAmsRptDefineMapper,EdcAmsRptDefine> implements  IEdcAmsRptDefineService {

    @Autowired
    private IEdcAmsRptDefineActEmailService edcAmsRptDefineActEmailService;


    @Autowired
    private IEdcAmsRptDefineActService edcAmsRptDefineActService;

    @Override
    public EdcAmsRptDefine selectById(Serializable id){
        EdcAmsRptDefine edcParamRecord = super.selectById(id);
        List<EdcAmsRptDefineActEmail> edcParamRecordDtlList = edcAmsRptDefineActEmailService.selectList(new EntityWrapper<EdcAmsRptDefineActEmail>(EdcAmsRptDefineActEmail.class).eq("rpt_alarm_id",id));
        edcParamRecord.setEdcAmsRptDefineActEmailList(edcParamRecordDtlList);
        List<EdcAmsRptDefineAct> edcAmsRptDefineActList = edcAmsRptDefineActService.selectList(new EntityWrapper<EdcAmsRptDefineAct>(EdcAmsRptDefineAct.class).eq("rpt_alarm_id",id));
        edcParamRecord.setEdcAmsRptDefineAct(edcAmsRptDefineActList);
        return edcParamRecord;
    }

    @Override
    public boolean insert(EdcAmsRptDefine edcParamRecord) {
        // 保存主表
        super.insert(edcParamRecord);
        List<EdcAmsRptDefineActEmail> edcParamRecordDtlList = edcParamRecord.getEdcAmsRptDefineActEmailList();
        for (EdcAmsRptDefineActEmail edcParamRecordDtl : edcParamRecordDtlList) {
            // 保存字段列表
            edcParamRecordDtl.setRptAlarmId(edcParamRecord.getId());
        }
        edcAmsRptDefineActEmailService.insertBatch(edcParamRecordDtlList);

        List<EdcAmsRptDefineAct> edcAmsRptDefineActList = edcParamRecord.getEdcAmsRptDefineAct();
        for (EdcAmsRptDefineAct edcParamRecordDtl : edcAmsRptDefineActList) {
            // 保存字段列表
            edcParamRecordDtl.setRptAlarmId(edcParamRecord.getId());
        }
        edcAmsRptDefineActService.insertBatch(edcAmsRptDefineActList);
        return true;
    }
}