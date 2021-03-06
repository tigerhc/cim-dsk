package com.lmrj.edc.config.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.config.entity.EdcConfigFileCsv;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.config.mapper
 * @title: edc_config_file_csv数据库控制层接口
 * @description: edc_config_file_csv数据库控制层接口
 * @author: 张伟江
 * @date: 2020-07-23 16:12:15
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcConfigFileCsvMapper extends BaseMapper<EdcConfigFileCsv> {

 @Select("select file_type from edc_config_file_csv where eqp_model_id = #{eqpModelId} group by file_type")
List<String> getFileType(@Param("eqpModelId") String eqpModelId);

 @Select("select GROUP_CONCAT(col_name order by sort_no) from edc_config_file_csv where eqp_model_id=(select model_id from fab_equipment where eqp_id = #{eqpId} ) and file_type = #{fileType} and del_flag = '0'")
 String findTitle(@Param("eqpId") String eqpId,@Param("fileType") String fileType);
}
