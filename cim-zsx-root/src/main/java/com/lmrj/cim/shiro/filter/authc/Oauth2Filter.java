package com.lmrj.cim.shiro.filter.authc;

import com.lmrj.common.http.Response;
import com.lmrj.common.utils.MessageUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.cim.common.response.ResponseError;
import com.lmrj.cim.utils.JWTHelper;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * All rights Reserved, Designed By www.gzst.gov.cn
 *
 * @version V1.0
 * @package cn.gzst.gov.cloud.gateway.security.shiro.filter
 * @title:
 * @description: 无状态访问拦截器
 * @author: 张飞
 * @date: 2018/3/16 11:41
 * @copyright: 2017 www.gzst.gov.cn Inc. All rights reserved.
 */
public class Oauth2Filter extends AccessControlFilter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DEFAULT_ACCESS_TOKEN_PARAM = "access_token";

    private String tokenParam=DEFAULT_ACCESS_TOKEN_PARAM;
    private String jwtTokenSecret;

    public void setJwtTokenSecret(String jwtTokenSecret) {
        this.jwtTokenSecret = jwtTokenSecret;
    }

    public void setTokenParam(String tokenParam) {
        this.tokenParam = tokenParam;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (null != getSubject(request, response)
                && getSubject(request, response).isAuthenticated()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 登录失败返回false
        Response responseResult = null;
        try {
            AuthenticationToken token = createToken(request, response);
            if (token!=null) {
                Subject subject = getSubject(request, response);
                subject.login(token);
                return Boolean.TRUE;
            }else{
                HttpServletRequest request1=(HttpServletRequest) request;
                if(String.valueOf(request1.getRequestURL()).contains("/services")){
                    return Boolean.TRUE;
                }
                if(String.valueOf(request1.getRequestURL()).contains("/mes/track")){
                    return Boolean.TRUE;
                }
                if(String.valueOf(request1.getRequestURL()).contains("mes/meslottrack")){
                    return Boolean.TRUE;
                }
                if(String.valueOf(request1.getRequestURL()).contains("dashboard")){
                    return Boolean.TRUE;
                }
                if(String.valueOf(request1.getRequestURL()).contains("/ms")){
                    return Boolean.TRUE;
                }
                responseResult = Response.error(ResponseError.TOKEN_IS_NULL,MessageUtils.getMessage("error."+ResponseError.TOKEN_IS_NULL));
            }
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            responseResult = Response.error(ResponseError.AUTHENTICATION_FAIL,e.getMessage());
        }catch (UnsupportedEncodingException e) {
            responseResult = Response.error(ResponseError.INVALID_ACCESS_TOKEN,e.getMessage());
        }catch (SignatureVerificationException e){
            responseResult = Response.error(ResponseError.INVALID_ACCESS_TOKEN,e.getMessage());
        }catch (TokenExpiredException e){
            responseResult = Response.error(ResponseError.EXPIRED_ACCESS_TOKEN,e.getMessage());
        }catch (JWTDecodeException e){
            responseResult = Response.error(ResponseError.INVALID_ACCESS_TOKEN,e.getMessage());
        }
        catch (Exception e){
            responseResult = Response.error(ResponseError.UNKNOWN_ERROR,e.getMessage());
        }
        ServletUtils.printJson((HttpServletResponse)response,responseResult);
        return Boolean.FALSE;
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws UnsupportedEncodingException {
        HttpServletRequest  httpServletRequest=(HttpServletRequest)request;
        String token = httpServletRequest.getHeader(tokenParam);
        if (StringUtil.isEmpty(token)){
            token = httpServletRequest.getParameter(tokenParam);
        }
        if(!StringUtil.isEmpty(token)
                && JWTHelper.verify(token, jwtTokenSecret)){
            return new Oauth2Token(token);
        }
        return null;
    }

}
