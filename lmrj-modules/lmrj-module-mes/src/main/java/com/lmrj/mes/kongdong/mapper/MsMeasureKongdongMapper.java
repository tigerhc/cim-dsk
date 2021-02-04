package com.lmrj.mes.kongdong.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.mapper
 * @title: ms_measure_record数据库控制层接口
 * @description: ms_measure_record数据库控制层接口
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MsMeasureKongdongMapper extends BaseMapper<MsMeasureKongdong> {
    @Select("select count(*) from ms_measure_kongdong where line_no = #{lineNo} and production_name = #{productionName} and lot_no =#{lotNo}")
    Integer findKongdongData(@Param("lineNo")String lineNo, @Param("productionName") String productionName,@Param("lotNo") String lotNo);

    List<MsMeasureKongdong> getKongdong(Map<String, Object> param);

    List<String> getLegend(Map<String, Object> param);

    List<Map<String,Object>> getData(Map<String, Object> param);

    List<Map<String,Object>> getBar(Map<String, Object> param);

    List<Map<String, Double>> getConfig(Map<String, Object> param);

    List<String> getXasix(Map<String, Object> param);


    @Select("select IFNULL(COUNT(1),0) from ms_measure_kongdong where line_no = #{lineNo} and production_name = #{productionName} and lot_no =#{lotNo} and type = #{type}")
    Integer findKongdongExist(@Param("lineNo")String lineNo, @Param("productionName") String productionName,@Param("lotNo") String lotNo,@Param("type") String type);

    @Select("select line_type from mes_kongdong_config where eqp_id = #{eqpId} and del_flag='2' order by line_type")
    List<String> getPositionSelect(String eqpId);
}
