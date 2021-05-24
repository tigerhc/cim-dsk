package com.lmrj.map.tray.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.map.tray.entity.MapTrayChipLog;
import com.lmrj.map.tray.entity.MapTrayChipMove;

import java.util.List;
import java.util.Map;

public interface IMapTrayChipMoveProcessService extends ICommonService<MapTrayChipMove> {
//    void traceDataNeedSpace();
    String processErrDataFlag = "ERR_TRAY";
    String processAsynchronous = "DATA_TRAY";
    String processNgDataFlag = "NG_TRAY";

    void traceData(MapTrayChipLog traceLog, String flag);

    List<Map<String, Object>> dmDetail(String chipId);

    Map<String, Object> getProductionParam(long id);
}
