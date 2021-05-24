package com.lmrj.edc.ams.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.ams.entity.EdcAmsDefine;
import com.lmrj.edc.ams.entity.EdcAmsDefineI18n;
import com.lmrj.edc.ams.mapper.EdcAmsDefineMapper;
import com.lmrj.edc.ams.service.IEdcAmsDefineI18nService;
import com.lmrj.edc.ams.service.IEdcAmsDefineService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
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
    private IEdcAmsDefineI18nService edcAmsDefineI18nService;
    @Override
    public boolean editFlag(String id, String flag) {
        EdcAmsDefine edcAmsDefine= this.selectById(id);
        edcAmsDefine.setMonitorFlag(flag);
        this.updateById(edcAmsDefine);
        return true;
    }

    @Override
    public EdcAmsDefine selectById(Serializable id){
        EdcAmsDefine edcParamRecord = super.selectById(id);
        List<EdcAmsDefineI18n> edcParamRecordDtlList = edcAmsDefineI18nService.selectList(new EntityWrapper<EdcAmsDefineI18n>(EdcAmsDefineI18n.class).eq("alarm_id",id));
        edcParamRecord.setEdcAmsDefineI18nList(edcParamRecordDtlList);
        return edcParamRecord;
    }

    /**
     * 获取警报定义
     * edit by wdj
     * @param alarmCode
     * @return
     */
    @Override
    public EdcAmsDefine selectByCode(String alarmCode){
        EdcAmsDefine from = new EdcAmsDefine();
        from.setAlarmCode(alarmCode);
        EdcAmsDefine edcParamRecord = baseMapper.selectOne(from);
        List<EdcAmsDefineI18n> edcParamRecordDtlList = edcAmsDefineI18nService.selectList(new EntityWrapper<EdcAmsDefineI18n>(EdcAmsDefineI18n.class).eq("alarm_id",edcParamRecord.getId()));
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
        edcAmsDefineI18nService.insertBatch(edcParamRecordDtlList,100);
        return true;
    }

    @Override
    public boolean insertOrUpdate(EdcAmsDefine edcAmsDefine) {
        try {
            // 获得以前的数据
            List<EdcAmsDefineI18n> oldEdcAmsDefineI18nList = edcAmsDefineI18nService.selectList(new EntityWrapper<EdcAmsDefineI18n>(EdcAmsDefineI18n.class).eq("alarm_id", edcAmsDefine.getId()));
            // 字段
            List<EdcAmsDefineI18n> edcAmsDefineI18nList = edcAmsDefine.getEdcAmsDefineI18nList();
            // 更新主表
            super.insertOrUpdate(edcAmsDefine);
            List<String> newsEdcAmsDefineI18nIdList = new ArrayList<String>();
            // 保存或更新数据
            for (EdcAmsDefineI18n edcAmsDefineI18n : edcAmsDefineI18nList) {
                // 设置不变更的字段
                if (StringUtil.isEmpty(edcAmsDefineI18n.getId())) {
                    // 保存字段列表
                    edcAmsDefineI18n.setAlarmId(edcAmsDefine.getId());
                    edcAmsDefineI18nService.insert(edcAmsDefineI18n);
                } else {
                    edcAmsDefineI18nService.insertOrUpdate(edcAmsDefineI18n);
                }
                newsEdcAmsDefineI18nIdList.add(edcAmsDefineI18n.getId());
            }
            // 删除老数据
            for (EdcAmsDefineI18n rmsRecipeBody : oldEdcAmsDefineI18nList) {
                String rmsRecipeBodyId = rmsRecipeBody.getId();
                if (!newsEdcAmsDefineI18nIdList.contains(rmsRecipeBodyId)) {
                    edcAmsDefineI18nService.deleteById(rmsRecipeBodyId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }
}
