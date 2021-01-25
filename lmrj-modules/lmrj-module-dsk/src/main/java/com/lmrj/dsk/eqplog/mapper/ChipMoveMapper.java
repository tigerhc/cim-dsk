package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.ChipBox;
import com.lmrj.dsk.eqplog.entity.ChipMove;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChipMoveMapper extends BaseMapper<ChipMove> {
    int insertMoveLog(@Param("dataList") List<ChipMove> dataList);

    int insertChipBox(@Param("dataList") List<ChipBox> dataList);

    Map<String, Object> findChipBoxStartTime(Map<String, Object> param);

    void updateChipBox(Map<String, Object> param);
}
