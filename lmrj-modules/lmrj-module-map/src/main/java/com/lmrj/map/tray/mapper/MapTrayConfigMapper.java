package com.lmrj.map.tray.mapper;

import com.lmrj.map.tray.entity.MapTrayConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.mapper
 * @title: map_tray_config数据库控制层接口
 * @description: map_tray_config数据库控制层接口
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MapTrayConfigMapper extends BaseMapper<MapTrayConfig> {

     List<Map<String, Object>> getAllTrayEqp();
 }
