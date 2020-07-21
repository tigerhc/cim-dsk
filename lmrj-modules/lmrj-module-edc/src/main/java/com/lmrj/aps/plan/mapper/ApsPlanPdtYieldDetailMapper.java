package com.lmrj.aps.plan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.aps.plan.mapper
 * @title: aps_plan_pdt_yield_detail数据库控制层接口
 * @description: aps_plan_pdt_yield_detail数据库控制层接口
 * @author: 张伟江
 * @date: 2020-05-17 21:01:21
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface ApsPlanPdtYieldDetailMapper extends BaseMapper<ApsPlanPdtYieldDetail> {
    List<ApsPlanPdtYieldDetail> selectDayYield(@Param("beginTime") String beginTime, @Param("endTime") String endTime , @Param("lineNo") String lineNo );


    @Delete("delete from aps_plan_pdt_yield_detail where plan_date like concat(#{period}, '%')")
    void deleteByPeriod(@Param("period") String period);



    @Select("SELECT sum(plan_qty) FROM aps_plan_pdt_yield_detail " +
            "WHERE production_no=#{productionNo} AND lot_no  like  concat(#{lotNo}, '%')")
    int findDayPlan(@Param("productionNo") String productionNo, @Param("lotNo")  String lotNo);

    @Select("SELECT sum(plan_qty) FROM aps_plan_pdt_yield_detail " +
            "WHERE production_no=#{productionNo} AND plan_date=#{planDate}")
    int findCurrentDayPlan(@Param("productionNo") String productionNo, @Param("planDate")  String planDate);


}
