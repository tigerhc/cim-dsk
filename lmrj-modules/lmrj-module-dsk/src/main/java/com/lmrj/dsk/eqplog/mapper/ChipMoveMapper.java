package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.ChipMove;
import com.lmrj.dsk.eqplog.entity.ChipSingle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChipMoveMapper extends BaseMapper<ChipMove> {
    int insertMoveLog(@Param("dataList") List<ChipMove> dataList);

    int insertSingleChip(@Param("dataList") List<ChipSingle> dataList);
}
