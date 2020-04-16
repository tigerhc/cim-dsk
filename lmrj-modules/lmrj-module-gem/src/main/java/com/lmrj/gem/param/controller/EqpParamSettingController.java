package com.lmrj.gem.param.controller;


import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.gem.param.entity.EqpParamSetting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**   
 * @Title: eqp_param_setting
 * @Description: eqp_param_setting
 * @author zhangweijiang
 * @date 2018-04-27 20:43:55
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("eqpparam/eqpparamsetting")
@RequiresPathPermission("eqpparam:eqpparamsetting")
public class EqpParamSettingController extends BaseCRUDController<EqpParamSetting> {

}
