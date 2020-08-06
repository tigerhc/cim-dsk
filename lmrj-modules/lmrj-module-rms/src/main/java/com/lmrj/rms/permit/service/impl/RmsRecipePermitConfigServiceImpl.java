package com.lmrj.rms.permit.service.impl;

import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.core.sys.entity.User;
import com.lmrj.core.sys.entity.UserRole;
import com.lmrj.core.sys.service.IUserRoleService;
import com.lmrj.rms.permit.service.IRmsRecipePermitConfigService;
import com.lmrj.rms.permit.entity.RmsRecipePermitConfig;
import com.lmrj.rms.permit.mapper.RmsRecipePermitConfigMapper;
import com.lmrj.rms.permit.utils.ShiroExt;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.permit.service.impl
* @title: rms_recipe_permit_config服务实现
* @description: rms_recipe_permit_config服务实现
* @author: 张伟江
* @date: 2020-07-15 23:08:59
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rmsRecipePermitConfigService")
public class RmsRecipePermitConfigServiceImpl  extends CommonServiceImpl<RmsRecipePermitConfigMapper,RmsRecipePermitConfig> implements  IRmsRecipePermitConfigService {

    @Autowired
    private IRmsRecipePermitConfigService rmsRecipePermitConfigService;
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public Integer updateRoleNameBySubmitLevel(String submitterRoleName, String submitLevel) {
        List<RmsRecipePermitConfig> recipePermitConfigList = rmsRecipePermitConfigService.selectList(new EntityWrapper<RmsRecipePermitConfig>().eq("submit_level", Integer.parseInt(submitLevel)));
        RmsRecipePermitConfig recipePermitConfig = null;
        if (recipePermitConfigList.size() > 0){
            recipePermitConfig = recipePermitConfigList.get(0);
        }
        recipePermitConfig.setSubmitterRoleName(submitterRoleName);
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String id = ShiroExt.getPrincipalProperty(principal, "id");
        recipePermitConfig.setUpdateByName(id);
        recipePermitConfig.setCreateDate(new Date());
        return baseMapper.updateAllColumnById(recipePermitConfig);
    }

    @Override
    public String[] getEmail(String submitLevel) {
        List<RmsRecipePermitConfig> recipePermitConfigList = baseMapper.selectList(new EntityWrapper<RmsRecipePermitConfig>().eq("submit_level", Integer.parseInt(submitLevel)));
        RmsRecipePermitConfig recipePermitConfig = null;
        if (recipePermitConfigList.size() > 0){
            recipePermitConfig = recipePermitConfigList.get(0);
        }
        String submitterRoleId = recipePermitConfig.getSubmitterRoleId();
        List<UserRole> userRoles = userRoleService.selectList(new EntityWrapper<UserRole>().eq("role_id", submitterRoleId));
        if (userRoles.size() > 0){
            String[] email = new String[userRoles.size()];
            for (int i = 0; i < userRoles.size(); i++){
                email[i] = UserUtil.getUser(userRoles.get(i).getUserId()).getEmail();
            }
            return email;
        }
        return null;
    }
}
