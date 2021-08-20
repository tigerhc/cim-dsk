package com.lmrj.ms.config.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.ms.config.entity.MsMeasureConfig;
import com.lmrj.ms.config.entity.MsMeasureConfigDetail;
import com.lmrj.ms.config.mapper.MsMeasureConfigMapper;
import com.lmrj.ms.config.service.IMsMeasureConfigDetailService;
import com.lmrj.ms.config.service.IMsMeasureConfigService;
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
* @package com.lmrj.ms.config.service.impl
* @title: ms_measure_config服务实现
* @description: ms_measure_config服务实现
* @author: 张伟江
* @date: 2020-06-06 18:32:57
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("msMeasureConfigService")
public class MsMeasureConfigServiceImpl  extends CommonServiceImpl<MsMeasureConfigMapper,MsMeasureConfig> implements  IMsMeasureConfigService {

    @Autowired
    private IMsMeasureConfigDetailService msMeasureConfigDetailService;

    @Override
    public MsMeasureConfig selectById(Serializable id){
        MsMeasureConfig msMeasureConfig = super.selectById(id);
        List<MsMeasureConfigDetail> msMeasureConfigDetailList = msMeasureConfigDetailService.selectList(new EntityWrapper<MsMeasureConfigDetail>(MsMeasureConfigDetail.class).eq("ms_config_id",id));
        msMeasureConfig.setDetail(msMeasureConfigDetailList);
        return msMeasureConfig;
    }

    @Override
    public boolean insert(MsMeasureConfig msMeasureConfig) {
        // 保存主表
        super.insert(msMeasureConfig);
        // 保存细表
        List<MsMeasureConfigDetail> detailList = msMeasureConfig.getDetail();
        for (MsMeasureConfigDetail msMeasureConfigDetail : detailList) {
            msMeasureConfigDetail.setMsConfigId(msMeasureConfig.getId());
        }
        msMeasureConfigDetailService.insertBatch(detailList,100);
        return true;
    }

    @Override
    public boolean insertOrUpdate(MsMeasureConfig msMeasureConfig) {
        try {
            // 获得以前的数据
            List<MsMeasureConfigDetail> oldDetailList = msMeasureConfigDetailService.selectList(new EntityWrapper<MsMeasureConfigDetail>(MsMeasureConfigDetail.class).eq("ms_config_id", msMeasureConfig.getId()));
            // 字段
            List<MsMeasureConfigDetail> detailList = msMeasureConfig.getDetail();
            // 更新主表
            super.insertOrUpdate(msMeasureConfig);
            List<String> newsMsMeasureRecordDetailIdList = new ArrayList<String>();
            // 保存或更新数据
            for (MsMeasureConfigDetail msMeasureConfigDetail : detailList) {
                // 设置不变更的字段
                if (StringUtil.isEmpty(msMeasureConfigDetail.getId())) {
                    // 保存字段列表
                    msMeasureConfigDetail.setMsConfigId(msMeasureConfig.getId());
                    msMeasureConfigDetailService.insert(msMeasureConfigDetail);
                } else {
                    msMeasureConfigDetailService.insertOrUpdate(msMeasureConfigDetail);
                }
                newsMsMeasureRecordDetailIdList.add(msMeasureConfigDetail.getId());
            }
            // 删除老数据
            for (MsMeasureConfigDetail msMeasureConfigDetail : oldDetailList) {
                String detailId = msMeasureConfigDetail.getId();
                if (!newsMsMeasureRecordDetailIdList.contains(detailId)) {
                    msMeasureConfigDetailService.deleteById(detailId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    @Override
    public List<String> eqpModelNameList() {
        return baseMapper.eqpModelNameList();
    }
}
