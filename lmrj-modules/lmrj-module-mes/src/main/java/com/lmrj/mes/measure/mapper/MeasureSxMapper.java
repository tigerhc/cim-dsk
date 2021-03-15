package com.lmrj.mes.measure.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.measure.entity.measureSx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MeasureSxMapper extends BaseMapper<measureSx> {

    @Select("select distinct production_no as productionNo from measure_sx_record")
    List<Map<String, String>> findProductionNo();

    @Select("SELECT lot_no AS lotNo ,a1 ,b1 ,c1 ,d1 ,a2 ,b2 ,c2 ,d2  FROM measure_sx_record WHERE production_no = #{productionName} AND serial_counter = #{number} AND measure_type = #{type}  AND measure_judgment = 'OK' AND  mea_date BETWEEN #{startDate} AND #{endDate} ORDER  BY lot_no")
    List<Map<String, String>> findSxNumber(@Param("productionName") String productionName,@Param("number")String number, @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("type") String type);
}
