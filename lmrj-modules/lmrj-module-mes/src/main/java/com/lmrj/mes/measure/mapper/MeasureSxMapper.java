package com.lmrj.mes.measure.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.measure.entity.MeasureSx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MeasureSxMapper extends BaseMapper<MeasureSx> {

    @Select("select distinct production_no as 'label', production_no as 'value' from measure_sx_record where measure_type = #{type} and measure_judgment = 'OK'")
    List<Map<String, String>> findProductionNo(@Param("type") String type);

    @Select("select distinct production_no as 'label', production_no as 'value' from measure_sim_record where measure_type = #{type} and measure_judgment = 'OK'")
    List<Map<String, String>> findSimProductionNo(@Param("type") String type);

    @Select("select distinct production_no as 'label', production_no as 'value' from measure_gi_record where measure_type = #{type} and line_no=#{lineNo} and measure_judgment = 'OK'")
    List<Map<String, String>> findGiProductionNo(Map<String, Object> param);

    @Select("SELECT lot_no AS lotNo ,a1 ,b1 ,c1 ,d1 ,a2 ,b2 ,c2 ,d2,DATE_FORMAT(mea_date,'%Y-%m-%d %H:%i:%s') as 'meaDate' " +
            "FROM measure_sx_record WHERE production_no = #{productionName} AND serial_counter = #{number} AND measure_type = #{type}  AND measure_judgment = 'OK' AND  mea_date BETWEEN #{startDate} AND #{endDate} ORDER  BY lot_no")
    List<Map<String, String>> findSxNumber(@Param("productionName") String productionName,@Param("number")String number, @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("type") String type);

    @Select("SELECT lot_no AS lotNo ,a1 as 'a' ,b1 as 'b',c1 as 'c1',DATE_FORMAT(mea_date,'%Y-%m-%d %H:%i:%s') as 'meaDate',serial_counter as serialCounter," +
            "c2 as 'c2',c3 as 'c3',c4 as 'c4',c5 as 'c5',c6 as 'c6',c7 as 'c7',c8 as 'c8',c9 as 'c9',c10 as 'c10',c11 as 'c11',c12 as 'c12',c13 as 'c13',c14 as 'c14'," +
            "c15 as 'c15',c16 as 'c16',c17 as 'c17',c19 as 'c19',c20 as 'c20',c21 as 'c21',c23 as 'c23',c24 as 'c24',c28 as 'c28',c30 as 'c30',c31 as 'c31',c35 as 'c35'," +
            "c37 as 'c37',c40 as 'c40',c26 as 'c26' " +
            "FROM measure_sim_record WHERE production_no = #{productionName} AND measure_type = #{type}  AND measure_judgment = 'OK' " +
            "AND  mea_date BETWEEN #{startDate} AND #{endDate} GROUP BY lot_no,serial_counter ORDER BY mea_date")
    List<Map<String, String>> findSimNumber(@Param("productionName") String productionName, @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("type") String type);

    @Select("SELECT lot_no AS lotNo ,a1 as 'a' ,b1 as 'b',c1 ,c21,c2 as 'c2',c3,c4, c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c19," +
            "c20,c21,c23,c24,c28,c30,c31,c35,c37,c40,c26 FROM measure_sim_record WHERE measure_name='Limit' ORDER BY measure_counter")
    List<Map<String, String>> findSimLimit();

    @Select("SELECT burr_f, pin_f1, pin_f2, pin_f3, pin_f4, pin_f5, pin_f6, pin_f1_f2, pin_f2_f3, pin_f3_f4, pin_f4_f5, pin_f5_f6, "+
            "pin_s1, pin_s2, pin_s3, pin_s4, pin_s5, pin_s6 FROM measure_gi_record WHERE measure_name='Limit' and line_no=#{lineNo} ORDER BY measure_counter")
    List<Map<String, String>> findGiLimit(@Param("lineNo") String lineNo);

    @Select("SELECT lot_no AS lotNo ,burr_f, pin_f1, pin_f2, pin_f3, pin_f4, pin_f5, pin_f6, pin_f1_f2, pin_f2_f3, pin_f3_f4, pin_f4_f5, pin_f5_f6, " +
            "pin_s1, pin_s2, pin_s3, pin_s4, pin_s5, pin_s6, DATE_FORMAT(mea_date,'%Y-%m-%d %H:%i:%s') as 'meaDate' ,serial_counter as serialCounter " +
            "FROM measure_gi_record WHERE production_no = #{productionName} AND measure_type = #{type}  AND measure_judgment = 'OK' " +
            "AND  mea_date BETWEEN #{startDate} AND #{endDate} GROUP BY lot_no,serial_counter ORDER BY mea_date")
    List<Map<String, String>> findGiNumber(@Param("productionName") String productionName, @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("type") String type);
}
