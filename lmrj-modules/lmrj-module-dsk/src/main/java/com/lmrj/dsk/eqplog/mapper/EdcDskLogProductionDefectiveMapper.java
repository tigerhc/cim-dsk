package com.lmrj.dsk.eqplog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProductionDefective;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.mapper
 * @title: EdcDskLogProductionDefective数据库控制层接口
 * @description: EdcDskLogProductionDefective数据库控制层接口
 * @author: 张伟江
 * @date: 2020-04-14 10:10:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcDskLogProductionDefectiveMapper extends BaseMapper<EdcDskLogProductionDefective> {

    @Select("select * from edc_dsk_log_production_defective where lot_no = #{lotNo} and eqp_id = #{eqpId} and production_no = #{productionNo} order by start_time")
    List<EdcDskLogProductionDefective> findDataBylotNo(@Param("lotNo") String lotNo, @Param("eqpId") String eqpId, @Param("productionNo") String productionNo);
}
