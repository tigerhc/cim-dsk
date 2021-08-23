package com.lmrj.mes.measure.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.measure.entity.MeasureSma;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MeasureSmaMapper extends BaseMapper<MeasureSma> {

    @Select("select distinct production_no as productionNo from measure_sim_record where measure_type = #{type}")
    List<Map<String, String>> findProductionNo(@Param("type") String type);

    @Select("SELECT lot_no AS lotNo ,d1_h,d1_l,d2_h,d2_l,a3_1,a3_2,b3_1,b3_2,c3_1,r3_1,r3_2,a4_1,a4_2,b4_1,b4_2,c4_1,r4_1,r4_2 FROM measure_sma_record WHERE production_no = #{productionName} AND serial_counter = #{number} AND measure_type = #{type}  AND measure_judgment = 'OK' AND  mea_date BETWEEN #{startDate} AND #{endDate} ORDER  BY lot_no")
    List<Map<String, String>> findSmaNumber(@Param("productionName") String productionName, @Param("number") String number, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("type") String type);
}
