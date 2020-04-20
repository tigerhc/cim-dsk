package com.lmrj.edc.param.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.param.entity.EdcParamRecordDtl;

/**
 * @Title: edc_param_record_dtl数据库控制层接口
 * @Description: edc_param_record_dtl数据库控制层接口
 * @author zhangweijiang
 * @date 2019-06-11 21:26:40
 * @version V1.0
 *
 */
public interface EdcParamRecordDtlMapper extends BaseMapper<EdcParamRecordDtl> {

    void deleteByEqp(String eqpId);

    void transfer2His(String eqpId);

}
