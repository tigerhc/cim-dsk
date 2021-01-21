package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.ChipBox;
import com.lmrj.dsk.eqplog.entity.ChipMove;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChipMoveMapper extends BaseMapper<ChipMove> {
    int insertMoveLog(@Param("dataList") List<ChipMove> dataList);

    int insertChipBox(@Param("dataList") List<ChipBox> dataList);
}
