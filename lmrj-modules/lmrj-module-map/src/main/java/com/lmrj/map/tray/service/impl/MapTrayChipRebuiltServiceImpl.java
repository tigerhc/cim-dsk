package com.lmrj.map.tray.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.entity.MapTrayChipRebuilt;
import com.lmrj.map.tray.mapper.MapTrayChipRebuiltMapper;
import com.lmrj.map.tray.service.IMapTrayChipRebuiltService;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.map.tray.service.impl
* @title: map_tray_chip_rebuilt服务实现
* @description: map_tray_chip_rebuilt服务实现
* @author: stev
* @date: 2020-08-02 15:29:58
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("mapTrayChipRebuiltService")
public class MapTrayChipRebuiltServiceImpl  extends CommonServiceImpl<MapTrayChipRebuiltMapper,MapTrayChipRebuilt> implements  IMapTrayChipRebuiltService {

}