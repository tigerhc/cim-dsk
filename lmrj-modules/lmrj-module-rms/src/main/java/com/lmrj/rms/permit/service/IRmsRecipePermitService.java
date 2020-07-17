package com.lmrj.rms.permit.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rms.permit.entity.RmsRecipePermit;
import org.springframework.web.bind.annotation.RequestParam;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.permit.service
* @title: rms_recipe_permit服务接口
* @description: rms_recipe_permit服务接口
* @author: 张伟江
* @date: 2020-07-15 23:08:38
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IRmsRecipePermitService extends ICommonService<RmsRecipePermit> {

    void recipePermit(String approveStep, String roleName, String recipeId,String submitResult, String submitDesc) throws Exception;

}
