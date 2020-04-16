package com.lmrj.edc.param.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.param.entity.EdcParamRecordDtl;

/**   
 * @Title: edc_param_record_dtl
 * @Description: edc_param_record_dtl
 * @author zhangweijiang
 * @date 2019-06-11 21:26:40
 * @version V1.0   
 *
 */
public interface IEdcParamRecordDtlService extends ICommonService<EdcParamRecordDtl> {
    public int deleteByEqp(String eqpId);
    public void transfer2His(String eqpId);
}

