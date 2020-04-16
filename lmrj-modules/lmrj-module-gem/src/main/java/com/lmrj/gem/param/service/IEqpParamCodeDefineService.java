package com.lmrj.gem.param.service;


import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.gem.param.entity.EqpParamCodeDefine;

import java.util.Map;

/**   
 * @Title: 设备参数定义
 * @Description: 设备参数统一定义
 * @author zhangweijiang
 * @date 2018-05-22 15:28:42
 * @version V1.0   
 *
 */
public interface IEqpParamCodeDefineService extends ICommonService<EqpParamCodeDefine> {

    Map<String, String> findAllParamCodeDefine();

}

