package com.lmrj.fab.eqp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.eqp.entity.FabEquipmentBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.EAN;

import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 17:21
 */
@Mapper
public interface FabEquipmentBindMapper extends BaseMapper<FabEquipmentBind> {
    /*生成传感器时，传感器信息写入bind*/
    Integer addBind(FabEquipmentBind fabEquipmentBind);
}
