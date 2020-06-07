package com.lmrj.ms.config.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.ms.config.service.IMsMeasureConfigDetailService;
import com.lmrj.ms.config.entity.MsMeasureConfigDetail;
import com.lmrj.ms.config.mapper.MsMeasureConfigDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.ms.config.service.impl
* @title: ms_measure_config_detail服务实现
* @description: ms_measure_config_detail服务实现
* @author: 张伟江
* @date: 2020-06-06 18:33:20
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("msMeasureConfigDetailService")
public class MsMeasureConfigDetailServiceImpl  extends CommonServiceImpl<MsMeasureConfigDetailMapper,MsMeasureConfigDetail> implements  IMsMeasureConfigDetailService {

}