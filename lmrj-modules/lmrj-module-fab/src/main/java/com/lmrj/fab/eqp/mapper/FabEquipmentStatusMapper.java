package com.lmrj.fab.eqp.mapper;

import com.lmrj.fab.eqp.entity.FabEquipmentStatus;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.1.mapper
 * @title: fab_equipment_status数据库控制层接口
 * @description: fab_equipment_status数据库控制层接口
 * @author: zhangweijiang
 * @date: 2019-06-18 20:41:20
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface FabEquipmentStatusMapper extends BaseMapper<FabEquipmentStatus> {

    List<Map> selectEqpStatusChart();

    int updateStatus(@Param("eqpID") String eqpID, @Param("status") String status, @Param("lotNo") String lotId, @Param("recipeCode") String recipeCode);
    int updateYield(@Param("eqpID") String eqpID, @Param("status") String status, @Param("lotNo") String lotId, @Param("recipeCode") String recipeCode
            , @Param("lotYield") int lotYield, @Param("dayYield") int dayYield);
    int updateLot(@Param("eqpID") String eqpID, @Param("lotNo") String lotNo);

    List<FabEquipmentStatus> selectEqpStatus(@Param("officeId") String officeId, @Param("lineNo") String lineNo, @Param("fab") String fab);

}
