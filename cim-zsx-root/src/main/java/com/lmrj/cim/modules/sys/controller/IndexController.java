package com.lmrj.cim.modules.sys.controller;

import com.lmrj.cim.modules.oa.service.IOaNotificationService;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.cim.utils.ThemeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
	@Autowired
	private IOaNotificationService oaNotificationService;

	/**
	 * 跳转到后台首页
	 * @return
	 */
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public ModelAndView index() {
		return new ModelAndView("redirect:/admin");
	}

	/**
	 * Coookie设置
	 */
	@RequestMapping(value = "theme/{theme}")
	public String themeCookie(@PathVariable String theme, HttpServletRequest request) {
		if (StringUtil.isNotBlank(theme)) {
			ThemeUtils.setTheme(theme);
		}
		return "redirect:" + request.getParameter("url");
	}

	@RequestMapping("main")
	public String main() {
		return "modules/sys/index/main";
	}

}
