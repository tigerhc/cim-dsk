package com.lmrj.map.tray.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.entity.MapTrayChipMove;

import java.util.List;
import java.util.Map;
/** 常用业务类 */
public interface IMapTrayChipMoveService extends ICommonService<MapTrayChipMove> {
    /** 获得每段线中的结束点的设备 */
    List<MapEquipmentConfig> getLineEndEqp();

    /** 物料查询 */
    List<Map<String, Object>> findMaterial(String eqpId, String lotNo);
}
