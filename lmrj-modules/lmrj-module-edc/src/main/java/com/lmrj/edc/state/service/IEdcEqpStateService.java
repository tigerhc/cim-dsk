package com.lmrj.edc.state.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.state.entity.EdcEqpState;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.state.service
* @title: edc_eqp_state服务接口
* @description: edc_eqp_state服务接口
* @author: 张伟江
* @date: 2020-02-20 01:26:46
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcEqpStateService extends ICommonService<EdcEqpState> {

    int syncEqp(String time);
}