package com.lmrj.cim.utils;

import com.lmrj.common.utils.CookieUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.util.spring.SpringContext;
import com.lmrj.util.lang.StringUtil;
import org.springframework.core.env.Environment;

import javax.servlet.http.Cookie;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.utils
 * @title:
 * @description: 模板样式
 * @author: 张飞
 * @date: 2018/10/6 15:06
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
public class ThemeUtils {
    /**
     * 设置样式
     * @param theme 央视
     */
    public static void setTheme(String theme){
        CookieUtils.setCookie(ServletUtils.getResponse(), "theme", theme);
    }

    /**
     * 获得样式
     * @return
     */
    public static String getTheme(){
        // 默认风格
        Environment env = SpringContext.getBean(Environment.class);
        String theme = env.getProperty("lmrj.admin.default.theme");
        if (StringUtil.isEmpty(theme)) {
            theme = "uadmin";
        }
        // cookies配置中的模版
        Cookie[] cookies = ServletUtils.getRequest().getCookies();
        if (cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie == null || StringUtil.isEmpty(cookie.getName())) {
                    continue;
                }
                if (cookie.getName().equalsIgnoreCase("theme")) {
                    theme = cookie.getValue();
                }
            }
        }
        return theme;
    }

}
