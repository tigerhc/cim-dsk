package com.lmrj.oven.batchlot.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.entity.FabEquipmentOvenStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.mapper
 * @title: ovn_batch_lot数据库控制层接口
 * @description: ovn_batch_lot数据库控制层接口
 * @author: zhangweijiang
 * @date: 2019-06-09 08:49:15
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */
@Mapper
public interface OvnBatchLotMapper extends BaseMapper<OvnBatchLot> {

     List<FabEquipmentOvenStatus> selectFabStatus(String officeId);

     List<Map> selectFabStatusParam(List<FabEquipmentOvenStatus> fabEquipmentOvenStatusList);

    List<Map> selectChartByCase(String officeId);
}
