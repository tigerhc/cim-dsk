package com.lmrj.mes.measure.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.measure.entity.MeasureSim;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MeasureSimMapper extends BaseMapper<MeasureSim> {

    @Select("select distinct production_no as productionNo from measure_sim_record where measure_type = #{type}")
    List<Map<String, String>> findProductionNo(@Param("type") String type);

    @Select("SELECT lot_no AS lotNo ,a1,b1,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c19,c20,c21,c23,c24,c26,c28,c30,c31,c35,c37,c40 FROM measure_sim_record WHERE production_no = #{productionName} AND serial_counter = #{number} AND measure_type = #{type}  AND measure_judgment = 'OK' AND  mea_date BETWEEN #{startDate} AND #{endDate} ORDER  BY lot_no")
    List<Map<String, String>> findSimNumber(@Param("productionName") String productionName, @Param("number") String number, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("type") String type);
}
