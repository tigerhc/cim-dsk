package com.lmrj.gem.param.controller;


import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.gem.param.entity.EqpParamCodeDefine;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**   
 * @Title: 设备参数定义
 * @Description: 设备参数统一定义
 * @author zhangweijiang
 * @date 2018-05-22 15:28:42
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("eqpparam/eqpparamcodedefine")
@RequiresPathPermission("eqpparam:eqpparamcodedefine")
public class EqpParamCodeDefineController extends BaseCRUDController<EqpParamCodeDefine> {

}
