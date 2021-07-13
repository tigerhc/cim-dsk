package com.lmrj.core.api.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.api.entity.EdcparamApi;
import com.lmrj.core.log.LogAspectj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/edcparamapi")
@ViewPrefix("api/edcparamapi")
@RequiresPathPermission("api:edcparamapi")
@LogAspectj(title = "api_edc_param_api")
public class EdcparamApiController extends BaseCRUDController<EdcparamApi> {
}
