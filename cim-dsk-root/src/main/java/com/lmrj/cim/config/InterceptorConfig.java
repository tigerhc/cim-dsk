package com.lmrj.cim.config;

import com.lmrj.common.interceptor.EncodingInterceptor;
import com.lmrj.common.security.shiro.interceptor.PermissionInterceptorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.modules.config
 * @title:
 * @description: 拦截器
 * @author: 张飞
 * @date: 2018/3/3 15:06
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 编码拦截器
     * @return
     */
    @Bean
    public HandlerInterceptor encodingInterceptor(){
        EncodingInterceptor encodingInterceptor=new EncodingInterceptor();
        return encodingInterceptor;
    }

    /**
     * 安全验证拦截器
     * @return
     */
    @Bean
    public PermissionInterceptorAdapter permissionInterceptorAdapter(){
        PermissionInterceptorAdapter permissionInterceptorAdapter=new PermissionInterceptorAdapter();
        return permissionInterceptorAdapter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //编码拦截器
        registry.addInterceptor(encodingInterceptor()).addPathPatterns("/**").excludePathPatterns("/upload/**","/static/**");
        //安全验证拦截器
        registry.addInterceptor(permissionInterceptorAdapter()).addPathPatterns("/**").excludePathPatterns("/upload/**","/static/**");
    }
}
