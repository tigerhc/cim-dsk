package com.lmrj.edc.ams.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.ams.service.IEdcAmsDefineI18nService;
import com.lmrj.edc.ams.entity.EdcAmsDefineI18n;
import com.lmrj.edc.ams.mapper.EdcAmsDefineI18nMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.ams.service.impl
* @title: edc_ams_define_i18n服务实现
* @description: edc_ams_define_i18n服务实现
* @author: zhangweijiang
* @date: 2020-02-15 02:42:19
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcAmsDefineI18nService")
public class EdcAmsDefineI18nServiceImpl  extends CommonServiceImpl<EdcAmsDefineI18nMapper,EdcAmsDefineI18n> implements  IEdcAmsDefineI18nService {

}