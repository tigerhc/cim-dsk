package com.lmrj.map.tray.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.mapper.MapEquipmentConfigMapper;
import com.lmrj.map.tray.service.IMapEquipmentConfigService;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.map.tray.service.impl
* @title: map_equipment_config服务实现
* @description: map_equipment_config服务实现
* @author: stev
* @date: 2020-08-02 15:29:58
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("mapEquipmentConfigService")
public class MapEquipmentConfigServiceImpl  extends CommonServiceImpl<MapEquipmentConfigMapper,MapEquipmentConfig> implements  IMapEquipmentConfigService {

}