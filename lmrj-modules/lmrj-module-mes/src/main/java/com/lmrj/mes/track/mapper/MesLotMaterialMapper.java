package com.lmrj.mes.track.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.mes.track.entity.MesLotMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.mapper
 * @title: mes_lot_track数据库控制层接口
 * @description: mes_lot_track数据库控制层接口
 * @author: 张伟江
 * @date: 2020-04-28 14:03:17
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MesLotMaterialMapper extends BaseMapper<MesLotMaterial> {

    @Select("select * from mes_lot_material where eqp_id = #{eqpId} and lot_no = #{lotNo}")
    MesLotMaterial selectMaterialData(String eqpId,String lotNo);
}
