package com.lmrj.ms.config.mapper;

import com.lmrj.ms.config.entity.MsMeasureConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.config.mapper
 * @title: ms_measure_config数据库控制层接口
 * @description: ms_measure_config数据库控制层接口
 * @author: 张伟江
 * @date: 2020-06-06 18:32:57
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MsMeasureConfigMapper extends BaseMapper<MsMeasureConfig> {

 List<String> eqpModelNameList();

}
