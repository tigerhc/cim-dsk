package com.lmrj.edc.evt.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.edc.evt.entity.EdcErrLogBean;
import com.lmrj.edc.evt.service.IEdcErrLogService;
import com.lmrj.util.mapper.JsonUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("edc/edcerrlog")
@ViewPrefix("edc/edcerrlog")
@RequiresPathPermission("edc:edcerrlog")
@LogAspectj(title = "edc_err_log")
public class EdcErrLogController extends BaseCRUDController<EdcErrLogBean> {
    @Autowired
    private IEdcErrLogService errLogService;

    @RabbitHandler
    @RabbitListener(queues = {"C2S.Q.ERR_LOG"})
    public void saveErrMsg(String dataJson){
        try {
            EdcErrLogBean errMsg = JsonUtil.from(dataJson, EdcErrLogBean.class);
            errLogService.saveErrMsg(errMsg);
        } catch (Exception e){
            logger.error("服务端保存edcerr数据出错", e);
        }
    }


}
