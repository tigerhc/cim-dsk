package com.lmrj.map.tray.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.entity.MapTrayChipMove;

import java.util.List;

public interface IMapTrayChipMovePseudoService extends ICommonService<MapTrayChipMove> {
    void tracePseudoData(String eqpId);

    void traceHB2();

    void traceHB2Desc();

    void traceNGData();
}
