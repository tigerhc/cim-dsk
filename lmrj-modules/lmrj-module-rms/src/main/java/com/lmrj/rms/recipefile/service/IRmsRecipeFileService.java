package com.lmrj.rms.recipefile.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.rms.recipefile.entity.RmsRecipeFile;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.recipefile.service
* @title: rms_recipe_file服务接口
* @description: rms_recipe_file服务接口
* @author: zhangweijiang
* @date: 2019-07-14 02:57:51
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IRmsRecipeFileService extends ICommonService<RmsRecipeFile> {

    public List<RmsRecipeFile> findFileByRecipeId(String recipeId);
}