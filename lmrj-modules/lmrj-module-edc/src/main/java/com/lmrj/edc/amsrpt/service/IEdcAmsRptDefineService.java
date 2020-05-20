package com.lmrj.edc.amsrpt.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.amsrpt.entity.EdcAmsRptDefine;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.amsrpt.service
* @title: edc_ams_rpt_define服务接口
* @description: edc_ams_rpt_define服务接口
* @author: zhangweijiang
* @date: 2020-02-15 02:46:43
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcAmsRptDefineService extends ICommonService<EdcAmsRptDefine> {
    boolean editFlag(String id, String flag);
}
