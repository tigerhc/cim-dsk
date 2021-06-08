package com.lmrj.fab.eqp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabSensor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 9:00
 */
@Mapper
public interface FabSensorMapper extends BaseMapper<FabSensor> {

    /**
     * 禁用设备
     * 主要是为了测试框架加的功能
     *
     * @param id
     * @return
     */
    Integer updateActiveFlag(@Param("id") String id, @Param("activeFlag") String activeFlag);

    List<String> findEqpIdList();

    List<Map> findEqpMap();

    List<Map> findNoSorMap(@Param("classCode") String classCode);

    //List<String> eqpIdMsList();
    List<Map> findEqpMsMap();

    @Select("select distinct station_code from  fab_sensor where line_no=#{lineNo}")
    List<String> findStationCodeByLineNo(@Param("lineNo") String lineNo);

    @Select("select sor_no from fab_sensor where sor_id= #{sorId}")
    String findeqpNoInfab(@Param("sorId") String sorId);

    List<Map<String,Object>> findDepartment (@Param("sorId") String sorId);

    List<Map<String,Object>> findEmailALL (@Param("code") String code);

    @Select("select * from fab_sensor where sor_id like concat(#{sorId},'%') order by sor_id")
    List<FabSensor> findWbEqp(@Param("sorId") String sorId);

    @Select("select * from fab_sensor where temp_flag='1'")
    List<FabSensor> findTempEqpList();
}
