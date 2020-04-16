package com.lmrj.edc.ams.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.ams.entity.EdcAmsDefine;
import com.lmrj.edc.ams.entity.EdcAmsDefineI18n;
import com.lmrj.edc.ams.mapper.EdcAmsDefineI18nMapper;
import com.lmrj.edc.ams.mapper.EdcAmsDefineMapper;
import com.lmrj.edc.ams.service.IEdcAmsDefineI18nService;
import com.lmrj.edc.ams.service.IEdcAmsDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.ams.service.impl
* @title: edc_ams_define服务实现
* @description: edc_ams_define服务实现
* @author: zhangweijiang
* @date: 2020-02-15 02:39:17
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcAmsDefineService")
public class EdcAmsDefineServiceImpl  extends CommonServiceImpl<EdcAmsDefineMapper,EdcAmsDefine> implements  IEdcAmsDefineService {

    @Autowired
    private EdcAmsDefineMapper edcAmsDefineMapper;

    @Autowired
    private EdcAmsDefineI18nMapper edcAmsDefineI18nMapper;

    @Autowired
    private IEdcAmsDefineI18nService edcAmsDefineI18nService;
    @Override
    public boolean editFlag(String alarmId, String flag) {
        EdcAmsDefine edcAmsDefine= edcAmsDefineMapper.selectById(alarmId);
        edcAmsDefine.setMonitorFlag(flag);
        edcAmsDefineMapper.updateById(edcAmsDefine);
        return true;
    }

    @Override
    public EdcAmsDefine selectById(Serializable id){
        EdcAmsDefine edcParamRecord = super.selectById(id);
        List<EdcAmsDefineI18n> edcParamRecordDtlList = edcAmsDefineI18nMapper.selectList(new EntityWrapper<EdcAmsDefineI18n>(EdcAmsDefineI18n.class).eq("alarm_id",id));
        edcParamRecord.setEdcAmsDefineI18nList(edcParamRecordDtlList);
        return edcParamRecord;
    }

    @Override
    public boolean insert(EdcAmsDefine edcParamRecord) {
        // 保存主表
        super.insert(edcParamRecord);
        // 保存细表
        List<EdcAmsDefineI18n> edcParamRecordDtlList = edcParamRecord.getEdcAmsDefineI18nList();
        for (EdcAmsDefineI18n edcParamRecordDtl : edcParamRecordDtlList) {
            edcParamRecordDtl.setAlarmId(edcParamRecord.getId());
        }
        edcAmsDefineI18nService.insertBatch(edcParamRecordDtlList);
        return true;
    }
}
