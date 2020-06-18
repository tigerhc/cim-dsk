package com.lmrj.cim.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Configuration;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.config
 * @title:
 * @description: Redis缓存类 https://blog.csdn.net/guokezhongdeyuzhou/article/details/79789629
 * @author: 张飞
 * @date: 2018/3/10 10:12
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
@Configuration
//@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    //@Bean
    //public KeyGenerator keyGenerator() {
    //    return new KeyGenerator() {
    //        @Override
    //        public Object generate(Object target, Method method, Object... params) {
    //            StringBuilder sb = new StringBuilder();
    //            sb.append(target.getClass().getName());
    //            sb.append(method.getName());
    //            for (Object obj : params) {
    //                sb.append(obj.toString());
    //            }
    //            return sb.toString();
    //        }
    //    };
    //}
}
