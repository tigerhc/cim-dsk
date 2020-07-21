package com.lmrj.rms.permit.mapper;

import com.lmrj.rms.permit.entity.RmsRecipePermitConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.permit.mapper
 * @title: rms_recipe_permit_config数据库控制层接口
 * @description: rms_recipe_permit_config数据库控制层接口
 * @author: 张伟江
 * @date: 2020-07-15 23:08:59
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface RmsRecipePermitConfigMapper extends BaseMapper<RmsRecipePermitConfig> {

 @Update("update rms_recipe_permit_config set submitter_role_name = #{submitterRoleName} where submit_level = #{submitLevel}")
 boolean updateRoleNameBySubmitLevel(String submitterRoleName, String submitLevel);

}
