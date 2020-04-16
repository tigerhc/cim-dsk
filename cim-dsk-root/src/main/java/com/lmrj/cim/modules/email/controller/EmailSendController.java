package com.lmrj.cim.modules.email.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.controller.BaseController;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.log.LogType;
import com.lmrj.cim.modules.email.service.IEmailSendService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @title 短信发送
 * @description 短信发送
 * @author 张飞
 * @date 2017-06-08 12:56:37
 * @version V1.0
 *
 */
@RestController
@RequestMapping("/email/send")
@LogAspectj(title = "邮件发送")
public class EmailSendController extends BaseController {
	@Autowired
	private IEmailSendService emailSendService;

	@PostMapping(value = "/massSendEmailByCode")
	@LogAspectj(logType = LogType.OTHER,title = "发送邮件")
	public Response massSendEmailByCode(HttpServletRequest request,
										@RequestParam("email") String email,
										@RequestParam("code") String code,
										@RequestParam("data") String data) {
		try {
			String[] emails=email.split(",");
			for (String emailItem:emails) {
				if (!org.springframework.util.StringUtils.isEmpty(data)) {
					Map<String,Object> datas = JSON.parseObject(StringEscapeUtils.unescapeHtml4(data),Map.class);
					emailSendService.send(emailItem, code,datas);
				} else {
					emailSendService.send(emailItem, code, Maps.newHashMap());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error("邮件发送失败");
		}
		return Response.ok("删除成功");
	}
}
