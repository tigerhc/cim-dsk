package com.lmrj.aps.plan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.aps.plan.mapper
 * @title: aps_plan_pdt_yield数据库控制层接口
 * @description: aps_plan_pdt_yield数据库控制层接口
 * @author: 张伟江
 * @date: 2020-05-17 21:00:52
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface ApsPlanPdtYieldMapper extends BaseMapper<ApsPlanPdtYield> {

    @Delete("delete from aps_plan_pdt_yield where plan_date = #{period} ")
    void deleteByPeriod(@Param("period") String period);


  /*  @Select(" select  production_name,production_no,plan_date  from aps_plan_pdt_yield_detail t where t.plan_date between #{beginTime} and #{endTime} order by t.plan_date")
    @ResultType(com.lmrj.aps.plan.entity.ApsPlanPdtYield.class)
    List<ApsPlanPdtYield> selectAps(@Param("beginTime")String beginTime, @Param("endTime")String endTime);
*/

    /**
     *
     * @return
     */
    @Select(" select  production_name,production_no,plan_date  from aps_plan_pdt_yield ")
    @ResultType(com.lmrj.aps.plan.entity.ApsPlanPdtYield.class)
    List<ApsPlanPdtYield> selectAps();

    @Select(" select  production_name  from aps_plan_pdt_yield where production_no=#{proNo} limit 1")
    String findProName(@Param("proNo") String proNo);

}
