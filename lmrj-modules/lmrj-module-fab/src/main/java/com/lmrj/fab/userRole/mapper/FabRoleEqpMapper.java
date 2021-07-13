package com.lmrj.fab.userRole.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.userRole.entity.FabRoleEqp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:13
 */
@Mapper
public interface FabRoleEqpMapper extends BaseMapper<FabRoleEqp> {
    List<FabRoleEqp> getEqpByRoleList(@Param("roleIdList") List<String> roleIdList);

    List<String> listOrgIds(@Param("orgid") String orgid);
}
