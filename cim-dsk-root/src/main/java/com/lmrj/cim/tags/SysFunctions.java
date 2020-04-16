package com.lmrj.cim.tags;

import javax.servlet.http.Cookie;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.util.spring.SpringContext;
import com.lmrj.util.lang.StringUtil;
import org.springframework.core.env.Environment;

/**
 *
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @title: SysFunctions.java
 * @package com.lmrj.web.tags
 * @description: 提供一些公用的函数
 * @author: 张飞
 * @date: 2017年3月28日 下午10:04:07
 * @version V1.0
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 *
 */
public class SysFunctions {


	/**
	 * 加载风格
	 *
	 * @title: getTheme
	 * @return
	 * @return: String
	 */
	public static String getTheme() {
		// 默认风格
		Environment env= SpringContext.getBean(Environment.class);
		String theme = env.getProperty("lmrj.admin.default.theme");
		if (StringUtil.isEmpty(theme)) {
			theme = "uadmin";
		}
		// cookies配置中的模版
		Cookie[] cookies = ServletUtils.getRequest().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie == null || StringUtil.isEmpty(cookie.getName())) {
				continue;
			}
			if (cookie.getName().equalsIgnoreCase("theme")) {
				theme = cookie.getValue();
			}
		}
		return theme;
	}
}
