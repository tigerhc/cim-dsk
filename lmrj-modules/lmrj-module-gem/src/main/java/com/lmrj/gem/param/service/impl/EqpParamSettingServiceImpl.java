package com.lmrj.gem.param.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.gem.param.entity.EqpParamSetting;
import com.lmrj.gem.param.mapper.EqpParamSettingMapper;
import com.lmrj.gem.param.service.IEqpParamSettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**   
 * @Title: eqp_param_setting
 * @Description: eqp_param_setting
 * @author zhangweijiang
 * @date 2018-04-27 20:43:55
 * @version V1.0   
 *
 */
@Transactional
@Service("eqpParamSettingService")
public class EqpParamSettingServiceImpl  extends CommonServiceImpl<EqpParamSettingMapper,EqpParamSetting> implements IEqpParamSettingService {
    public void clearSetValue(){
        this.baseMapper.clearSetValue();
    }

    public void updateValue(EqpParamSetting eqpParamSetting){
        this.baseMapper.updateValue(eqpParamSetting);
    }
}
