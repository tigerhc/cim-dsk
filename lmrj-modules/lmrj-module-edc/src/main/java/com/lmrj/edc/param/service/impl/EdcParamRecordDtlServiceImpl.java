package com.lmrj.edc.param.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.param.entity.EdcParamRecordDtl;
import com.lmrj.edc.param.mapper.EdcParamRecordDtlMapper;
import com.lmrj.edc.param.service.IEdcParamRecordDtlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**   
 * @Title: edc_param_record_dtl
 * @Description: edc_param_record_dtl
 * @author zhangweijiang
 * @date 2019-06-11 21:26:40
 * @version V1.0   
 *
 */
@Transactional
@Service("edcParamRecordDtlService")
public class EdcParamRecordDtlServiceImpl  extends CommonServiceImpl<EdcParamRecordDtlMapper,EdcParamRecordDtl> implements  IEdcParamRecordDtlService {

    @Override
    public int deleteByEqp(String eqpId){
         baseMapper.deleteByEqp(eqpId);
        return 1;
    }

    @Override
    public void transfer2His(String eqpId) {
        baseMapper.transfer2His(eqpId);
    }
}
