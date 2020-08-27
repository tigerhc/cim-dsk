package com.lmrj.map.tray.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.map.tray.entity.MapEquipmentConfig;

 /**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.mapper
 * @title: map_equipment_config数据库控制层接口
 * @description: map_equipment_config数据库控制层接口
 * @author: stev
 * @date: 2020-08-02 15:29:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MapEquipmentConfigMapper extends BaseMapper<MapEquipmentConfig> {
    
    public int initAll();
    
}