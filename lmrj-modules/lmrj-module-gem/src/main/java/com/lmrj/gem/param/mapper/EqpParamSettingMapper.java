package com.lmrj.gem.param.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.gem.param.entity.EqpParamSetting;
import org.apache.ibatis.annotations.Mapper;

/**   
 * @Title: eqp_param_setting数据库控制层接口
 * @Description: eqp_param_setting数据库控制层接口
 * @author zhangweijiang
 * @date 2018-04-27 20:43:55
 * @version V1.0   
 *
 */
@Mapper
public interface EqpParamSettingMapper extends BaseMapper<EqpParamSetting> {
    public int clearSetValue();

    public int updateValue(EqpParamSetting eqpParamSetting);

}