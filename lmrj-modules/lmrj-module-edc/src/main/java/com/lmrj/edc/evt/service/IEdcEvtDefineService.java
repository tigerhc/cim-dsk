package com.lmrj.edc.evt.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.evt.entity.EdcEvtDefine;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.evt.service
* @title: edc_evt_define服务接口
* @description: edc_evt_define服务接口
* @author: 张伟江
* @date: 2019-06-14 16:01:31
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcEvtDefineService extends ICommonService<EdcEvtDefine> {
    EdcEvtDefine findDataByEvtId(String eventCode);
}