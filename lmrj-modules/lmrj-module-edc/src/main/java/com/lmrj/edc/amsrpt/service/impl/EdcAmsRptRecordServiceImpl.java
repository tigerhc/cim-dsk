package com.lmrj.edc.amsrpt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptRecord;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptRecordDtl;
import com.lmrj.edc.amsrpt.mapper.EdcAmsRptRecordMapper;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptRecordDtlService;
import com.lmrj.edc.amsrpt.service.IEdcAmsRptRecordService;
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
* @title: edc_ams_rpt_record服务实现
* @description: edc_ams_rpt_record服务实现
* @author: zhangweijiang
* @date: 2020-02-15 02:47:52
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcAmsRptRecordService")
public class EdcAmsRptRecordServiceImpl  extends CommonServiceImpl<EdcAmsRptRecordMapper,EdcAmsRptRecord> implements  IEdcAmsRptRecordService {
    @Autowired
    private IEdcAmsRptRecordDtlService edcAmsRptRecordDtlService;
    @Override
    public EdcAmsRptRecord selectById(Serializable id){
        EdcAmsRptRecord edcAmsRptRecord = super.selectById(id);
        List<EdcAmsRptRecordDtl> edcAmsRptRecordDtlList = edcAmsRptRecordDtlService.selectList(new EntityWrapper<EdcAmsRptRecordDtl>(EdcAmsRptRecordDtl.class).eq("rpt_alarm_id",id));
        edcAmsRptRecord.setEdcAmsRptRecordDtlList(edcAmsRptRecordDtlList);
        //List<EdcAmsRptDefineAct> edcAmsRptDefineActList = edcAmsRptDefineActService.selectList(new EntityWrapper<EdcAmsRptDefineAct>(EdcAmsRptDefineAct.class).eq("rpt_alarm_id",id));
        //edcAmsRptRecord.setEdcAmsRptDefineAct(edcAmsRptDefineActList);
        return edcAmsRptRecord;
    }

}
