package com.lmrj.map.tray.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.entity.MapTrayChipMove;

import java.util.List;

public interface IMapTrayChipMovePseudoService extends ICommonService<MapTrayChipMove> {
    void tracePseudoData(MapEquipmentConfig eqp);

    /** 获得每段线中的结束点的设备
     * @return
     */
    List<MapEquipmentConfig> getLineEndEqp();

    void traceHB2();

    void traceNGData();

    List<MapTrayChipMove> selectDatas();
}
