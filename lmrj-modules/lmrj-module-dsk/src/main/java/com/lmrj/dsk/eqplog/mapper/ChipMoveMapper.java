package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.ChipMove;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChipMoveMapper extends BaseMapper<ChipMove> {
    int insertMoveLog(@Param("dataList") List<ChipMove> dataList);

    List<String> findXrayData(String toTrayId);

    void finishXrayData(String toTrayId);
}
