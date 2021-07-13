package com.lmrj.fab.eqp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.eqp.entity.FabNumType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-02 16:06
 */
@Mapper
public interface FabNumTypeMapper extends BaseMapper<FabNumType> {

    List<String> getNumtypeList(@Param("classCode") String classCode);
}
