package com.lmrj.rms.recipefile.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rms.recipefile.entity.RmsRecipeFile;
import com.lmrj.rms.recipefile.mapper.RmsRecipeFileMapper;
import com.lmrj.rms.recipefile.service.IRmsRecipeFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.recipefile.service.impl
* @title: rms_recipe_file服务实现
* @description: rms_recipe_file服务实现
* @author: zhangweijiang
* @date: 2019-07-14 02:57:51
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rmsRecipeFileService")
public class RmsRecipeFileServiceImpl  extends CommonServiceImpl<RmsRecipeFileMapper,RmsRecipeFile> implements  IRmsRecipeFileService {

    @Override
    public List<RmsRecipeFile> findFileByRecipeId(String recipeId){
        Wrapper wrapper = new EntityWrapper().eq("RECIPE_ID", recipeId).orderBy("SORT_NO");
        List<RmsRecipeFile> rmsRecipeFileList = this.selectList(wrapper);
        return rmsRecipeFileList;
    }
}