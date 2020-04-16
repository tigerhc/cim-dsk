package com.lmrj.gem.param.service;


import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.gem.param.entity.EqpParamSetting;

/**
 * @Title: eqp_param_setting
 * @Description: eqp_param_setting
 * @author zhangweijiang
 * @date 2018-04-27 20:43:55
 * @version V1.0   
 *
 */
public interface IEqpParamSettingService extends ICommonService<EqpParamSetting> {
    void clearSetValue();

    void updateValue(EqpParamSetting eqpParamSetting);
}

