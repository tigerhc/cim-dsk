package com.lmrj.map.tray.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.entity.MapTrayChipMove;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import com.lmrj.map.tray.service.IMapTrayChipMoveService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("MapTrayChipMoveService")
public class MapTrayChipMoveServiceImpl extends CommonServiceImpl<MapTrayChipMoveMapper, MapTrayChipMove> implements IMapTrayChipMoveService {
    /** 获得每段线中的结束点的设备*/
    @Override
    public List<MapEquipmentConfig> getLineEndEqp() {
        return baseMapper.getMapEqpConfig();
    }

    @Override
    public List<Map<String, Object>> findMaterial(String eqpId, String lotNo) {
        Map<String, Object> param = new HashMap<>();
        param.put("eqpId", eqpId);
        param.put("lotNo", lotNo);
        return baseMapper.findMaterial(param);
    }
}
