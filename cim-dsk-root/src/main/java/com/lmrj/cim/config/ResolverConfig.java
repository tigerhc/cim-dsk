package com.lmrj.cim.config;

import com.lmrj.common.query.resolver.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.config
 * @title:
 * @description:  HandlerMethodArgumentResolver
 * @author: 张飞
 * @date: 2018/3/3 17:12
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
@Configuration
public class ResolverConfig  implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new QueryMethodArgumentResolver());
        PageableMethodArgumentResolver pageableMethodArgumentResolver = new PageableMethodArgumentResolver();
        pageableMethodArgumentResolver.setMaxPageSize(1000);
        argumentResolvers.add(pageableMethodArgumentResolver);
        argumentResolvers.add(new FormModelMethodArgumentResolver());
        argumentResolvers.add(new RequestJsonParamMethodArgumentResolver());
        argumentResolvers.add(new PropertyPreFilterMethodArgumentResolver());
    }

}


