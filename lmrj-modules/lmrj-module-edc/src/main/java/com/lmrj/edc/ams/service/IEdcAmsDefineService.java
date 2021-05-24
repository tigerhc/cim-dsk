package com.lmrj.edc.ams.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.ams.entity.EdcAmsDefine;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.ams.service
* @title: edc_ams_define服务接口
* @description: edc_ams_define服务接口
* @author: zhangweijiang
* @date: 2020-02-15 02:39:17
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcAmsDefineService extends ICommonService<EdcAmsDefine> {

    boolean editFlag(String id, String flag);
    public EdcAmsDefine selectByCode(String alarmCode);
}
