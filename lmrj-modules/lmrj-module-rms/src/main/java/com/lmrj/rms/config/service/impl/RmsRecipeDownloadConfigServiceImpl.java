package com.lmrj.rms.config.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.rms.config.service.IRmsRecipeDownloadConfigService;
import com.lmrj.rms.config.entity.RmsRecipeDownloadConfig;
import com.lmrj.rms.config.mapper.RmsRecipeDownloadConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.rms.config.service.impl
* @title: rms_recipe_download_config服务实现
* @description: rms_recipe_download_config服务实现
* @author: 何思国
* @date: 2020-07-29 16:21:03
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("rmsRecipeDownloadConfigService")
public class RmsRecipeDownloadConfigServiceImpl  extends CommonServiceImpl<RmsRecipeDownloadConfigMapper,RmsRecipeDownloadConfig> implements  IRmsRecipeDownloadConfigService {

}