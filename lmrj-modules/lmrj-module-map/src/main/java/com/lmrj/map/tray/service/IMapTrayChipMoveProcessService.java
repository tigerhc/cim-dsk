package com.lmrj.map.tray.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.map.tray.entity.MapTrayChipLog;
import com.lmrj.map.tray.entity.MapTrayChipMove;

public interface IMapTrayChipMoveProcessService extends ICommonService<MapTrayChipMove> {
//    void traceDataNeedSpace();
    String processErrDataFlag = "err";
    String processAsynchronous = "asynchronous";
    void traceData(MapTrayChipLog traceLog, String flag);
}
