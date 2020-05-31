package com.lmrj.edc.lot.mapper;

import com.lmrj.edc.lot.entity.RptLotYield;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.lot.mapper
 * @title: rpt_lot_yield数据库控制层接口
 * @description: rpt_lot_yield数据库控制层接口
 * @author: 张伟江
 * @date: 2020-05-17 21:10:40
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface RptLotYieldMapper extends BaseMapper<RptLotYield> {

    List<Map> findLotYield(@Param("officeId") String lineNo);

}
