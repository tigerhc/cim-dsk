package com.lmrj.fab.eqp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.eqp.entity.FabEquipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.mapper
 * @title: fab_equipment数据库控制层接口
 * @description: fab_equipment数据库控制层接口
 * @author: 张伟江
 * @date: 2019-06-04 15:42:26
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */
@Mapper
public interface FabEquipmentMapper extends BaseMapper<FabEquipment> {

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

    //List<String> eqpIdMsList();
    List<Map> findEqpMsMap();

    @Select("select distinct station_code from  fab_equipment where line_no=#{lineNo}")
    List<String> findStationCodeByLineNo(@Param("lineNo") String lineNo);

    @Select("select eqp_no from fab_equipment where eqp_id= #{eqpId}")
    String findeqpNoInfab(@Param("eqpId") String eqpId);
}
