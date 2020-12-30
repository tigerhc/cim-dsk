package com.lmrj.map.tray.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.entity.MapTrayModel;
import com.lmrj.map.tray.mapper.MapTrayModelMapper;
import com.lmrj.map.tray.service.IMapTrayModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
@Service("mapTrayModelService")
public class MapTrayModelServiceImpl extends CommonServiceImpl<MapTrayModelMapper, MapTrayModel> implements IMapTrayModelService {

    @Override
    public List<MapTrayModel> getBySelect() {
        return baseMapper.getBySelect();
    }
}
