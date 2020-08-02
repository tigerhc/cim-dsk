package com.lmrj.map.tray.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.service.IMapTrayChipMoveService;
import com.lmrj.map.tray.entity.MapTrayChipMove;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.map.tray.service.impl
* @title: map_tray_chip_move服务实现
* @description: map_tray_chip_move服务实现
* @author: stev
* @date: 2020-08-02 15:31:58
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("mapTrayChipMoveService")
public class MapTrayChipMoveServiceImpl  extends CommonServiceImpl<MapTrayChipMoveMapper,MapTrayChipMove> implements  IMapTrayChipMoveService {

}