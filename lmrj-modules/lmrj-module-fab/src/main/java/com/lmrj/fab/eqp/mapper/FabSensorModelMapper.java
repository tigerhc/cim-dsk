package com.lmrj.fab.eqp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.eqp.entity.FabSensorModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FabSensorModelMapper extends BaseMapper<FabSensorModel> {
    List<Map> findLookup();
    List<String> manufacturerNameList();
    List<String> classCodeList();

    List<String> parentTypeList(@Param("classCode") String classCode);

    List<String> typeList(@Param("parentType") String parentType);

    List<Map> getAlltemplateList();

    List<String> noTemClassCodeList();
}
