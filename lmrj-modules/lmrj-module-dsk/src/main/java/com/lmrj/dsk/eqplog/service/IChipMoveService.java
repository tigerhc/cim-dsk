package com.lmrj.dsk.eqplog.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.dsk.eqplog.entity.ChipMove;

import java.util.List;
import java.util.Map;

public interface IChipMoveService extends ICommonService<ChipMove> {
    int insertData(List<Map<String, Object>> dataList);

    int insertChipIdData(List<Map<String, Object>> dataList);
}
