package com.lmrj.cim.shiro.filter.authc;

/**
 *
 * All rights Reserved, Designed By www.lmrj.com
 * @title:  UsernamePasswordToken.java
 * @package com.lmrj.web.modules.sys.security.shiro.web.filter.authc
 * @description:  用户和密码（包含验证码）令牌类
 * @author: 张飞
 * @date:   2017年6月26日 下午5:56:18
 * @version V1.0
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 *
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;

	public UsernamePasswordToken() {
		super();
	}


	public UsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}
