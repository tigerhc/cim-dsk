package com.lmrj.fab.eqp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.eqp.entity.SensorNumType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-02 16:06
 */
@Mapper
public interface SensorNumTypeMapper extends BaseMapper<SensorNumType> {

    List<String> getNumtypeList(@Param("classCode") String classCode);
}
