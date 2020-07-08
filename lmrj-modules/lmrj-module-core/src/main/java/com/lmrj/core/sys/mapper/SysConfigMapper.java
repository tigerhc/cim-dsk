package com.lmrj.core.sys.mapper;

import com.lmrj.core.sys.entity.SysConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.sys.config.mapper
 * @title: sys_config数据库控制层接口
 * @description: sys_config数据库控制层接口
 * @author: zhangweijiang
 * @date: 2019-07-14 03:03:35
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

 @Select("select * from sys_config where config_key = #{key}")
 List<SysConfig> queryByConfigKey(@Param("key") String key);

}
