package com.lmrj.map.tray.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.service.IMapTrayConfigService;
import com.lmrj.map.tray.entity.MapTrayConfig;
import com.lmrj.map.tray.mapper.MapTrayConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.map.tray.service.impl
* @title: map_tray_config服务实现
* @description: map_tray_config服务实现
* @author: stev
* @date: 2020-08-02 15:29:58
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("mapTrayConfigService")
public class MapTrayConfigServiceImpl  extends CommonServiceImpl<MapTrayConfigMapper,MapTrayConfig> implements  IMapTrayConfigService {
    @Override
    public List<Map<String, Object>> getAllTrayEqp(){
        return baseMapper.getAllTrayEqp();
    }
}
