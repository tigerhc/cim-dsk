package com.lmrj.core.userRole.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.core.userRole.entity.IotRoleEqp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-19 9:13
 */
@Mapper
public interface RoleEqpMapper  extends BaseMapper<IotRoleEqp> {
    List<IotRoleEqp> getEqpByRoleList(@Param("roleIdList") List<String> roleIdList);
}
