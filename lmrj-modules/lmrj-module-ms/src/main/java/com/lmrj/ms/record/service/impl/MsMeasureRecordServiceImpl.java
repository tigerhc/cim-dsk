package com.lmrj.ms.record.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.ms.record.entity.MsMeasureRecordDetail;
import com.lmrj.ms.record.mapper.MsMeasureRecordMapper;
import com.lmrj.ms.record.service.IMsMeasureRecordDetailService;
import com.lmrj.ms.record.service.IMsMeasureRecordService;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.ms.record.service.impl
* @title: ms_measure_record服务实现
* @description: ms_measure_record服务实现
* @author: 张伟江
* @date: 2020-06-06 18:36:32
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("msMeasureRecordService")
public class MsMeasureRecordServiceImpl  extends CommonServiceImpl<MsMeasureRecordMapper,MsMeasureRecord> implements  IMsMeasureRecordService {

    @Autowired
    private IMsMeasureRecordDetailService msMeasureRecordDetailService;

    @Override
    public MsMeasureRecord selectById(Serializable id){
        MsMeasureRecord msMeasureRecord = super.selectById(id);
        List<MsMeasureRecordDetail> edcParamRecordDtlList = msMeasureRecordDetailService.selectList(new EntityWrapper<MsMeasureRecordDetail>(MsMeasureRecordDetail.class).eq("ms_record_id",id).orderBy("sort_no"));
        msMeasureRecord.setDetail(edcParamRecordDtlList);
        return msMeasureRecord;
    }

    @Override
    public boolean insert(MsMeasureRecord msMeasureRecord) {
        // 保存主表
        super.insert(msMeasureRecord);
        // 保存细表
        List<MsMeasureRecordDetail> edcParamRecordDtlList = msMeasureRecord.getDetail();
        for (MsMeasureRecordDetail edcParamRecordDtl : edcParamRecordDtlList) {
            edcParamRecordDtl.setMsRecordId(msMeasureRecord.getId());
        }
        msMeasureRecordDetailService.insertBatch(edcParamRecordDtlList);
        return true;
    }

    @Override
    public boolean insertOrUpdate(MsMeasureRecord msMeasureRecord) {
        try {
            // 获得以前的数据
            List<MsMeasureRecordDetail> oldMsMeasureRecordDetailList = msMeasureRecordDetailService.selectList(new EntityWrapper<MsMeasureRecordDetail>(MsMeasureRecordDetail.class).eq("ms_record_id", msMeasureRecord.getId()));
            // 字段
            List<MsMeasureRecordDetail> msMeasureRecordDetailList = msMeasureRecord.getDetail();
            // 更新主表
            super.insertOrUpdate(msMeasureRecord);
            List<String> newsMsMeasureRecordDetailIdList = new ArrayList<String>();
            // 保存或更新数据
            for (MsMeasureRecordDetail msMeasureRecordDetail : msMeasureRecordDetailList) {
                // 设置不变更的字段
                if (StringUtil.isEmpty(msMeasureRecordDetail.getId())) {
                    // 保存字段列表
                    msMeasureRecordDetail.setMsRecordId(msMeasureRecord.getId());
                    msMeasureRecordDetailService.insert(msMeasureRecordDetail);
                } else {
                    msMeasureRecordDetailService.insertOrUpdate(msMeasureRecordDetail);
                }
                newsMsMeasureRecordDetailIdList.add(msMeasureRecordDetail.getId());
            }
            // 删除老数据
            for (MsMeasureRecordDetail rmsRecipeBody : oldMsMeasureRecordDetailList) {
                String msRecordId = rmsRecipeBody.getId();
                if (!newsMsMeasureRecordDetailIdList.contains(msRecordId)) {
                    msMeasureRecordDetailService.deleteById(msRecordId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    @Override
    public List<Map> findDetailBytime(String eqpId, String beginTime, String endTime) {
        List<Map> detail =  baseMapper.findDetailBytime(eqpId,  beginTime,  endTime);
        return detail;
    }

    @Override
    public List<Map> findDetailBytimeAndPro(String eqpId, String beginTime, String endTime, String productionNo) {
        List<Map> detail =  baseMapper.findDetailBytimeAndPro(eqpId,  beginTime,  endTime, productionNo);
        return detail;
    }

    @Override
    public List<MsMeasureRecord> findRecordByRecordId(String recordId) {
        return baseMapper.findRecordByRecordId(recordId);
    }

    @Override
    public List<MsMeasureRecord> findAll() {
        return baseMapper.findAll();
    }

    @Override
    public List<Map> findWeight(Map<String, Object> param) {
        return baseMapper.findWeight(param);
    }

    @Override
    public List<Map> getEqpIdOptions(String eqpId) {
        return baseMapper.getEqpIds(eqpId);
    }

    @Override
    public List<Map> getLineNoOptions() {
        return baseMapper.getLineNoOptions();
    }

    @Override
    public List<String> getAllProductionNo(String productionNo) {
        return baseMapper.getAllProductionNo(productionNo);
    }
}
