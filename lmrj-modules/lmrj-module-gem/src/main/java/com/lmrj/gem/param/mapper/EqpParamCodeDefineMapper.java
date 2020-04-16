package com.lmrj.gem.param.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.gem.param.entity.EqpParamCodeDefine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**   
 * @Title: 设备参数定义数据库控制层接口
 * @Description: 设备参数统一定义数据库控制层接口
 * @author zhangweijiang
 * @date 2018-05-22 15:28:42
 * @version V1.0   
 *
 */
@Mapper
public interface EqpParamCodeDefineMapper extends BaseMapper<EqpParamCodeDefine> {

    List<Map> findAllParamCodeDefine();
    
}