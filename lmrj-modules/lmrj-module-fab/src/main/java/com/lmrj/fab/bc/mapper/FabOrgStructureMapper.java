package com.lmrj.fab.bc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.bc.entity.FabOrgStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FabOrgStructureMapper extends BaseMapper<FabOrgStructure> {
    Map<String, Object> selectOrgByCode(@Param("code")String code);

    FabOrgStructure chkExistFab(@Param("code")String code);

    int chkExistFabCode(@Param("code")String code);

    List<Map<String, Object>> selectParentOrg(@Param("pids") List<String> pids);
}
