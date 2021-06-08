package com.lmrj.fab.eqp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package com.lmrj.fab.mapper
 * @title: fab_equipment_model数据库控制层接口
 * @description: fab_equipment_model数据库控制层接口
 * @author: kang
 * @date: 2019-06-07 22:18:19
 * @copyright: 2018 www.gzst.gov.cn Inc. All rights reserved.
 */
@Mapper
public interface FabEquipmentModelMapper extends BaseMapper<FabEquipmentModel> {
  List<Map> findLookup();
  List<String> manufacturerNameList();
  List<String> classCodeList();

  List<String> parentTypeList(@Param("classCode") String classCode);

  List<String> typeList(@Param("parentType") String parentType);

  List<Map> getAlltemplateList();

  List<String> noTemClassCodeList();

}
