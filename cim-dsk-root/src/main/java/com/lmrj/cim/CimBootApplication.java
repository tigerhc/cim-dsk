package com.lmrj.cim;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.cim
 * @title: Web启动入口
 * @description: Web启动入口
 * @author: 张飞
 * @date: 2018/5/22 14:56
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
@EnableSwagger2Doc
@EnableSwagger2
//@MapperScan("com.lmrj.**.mapper")
@ComponentScan({"com.lmrj.common.quartz.config","com.lmrj.common.oss","com.lmrj.common.sms", "com.lmrj.cim","com.lmrj.fab","com.lmrj.sys","com.lmrj.edc","com.lmrj.rms","com.lmrj.oven", "com.lmrj.common.rabbitmq","com.lmrj.dsk","com.lmrj.oven"})
@ServletComponentScan    //扫描自定义的WebFilter和WebListener
@SpringBootApplication
@EnableScheduling
public class CimBootApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(CimBootApplication.class, args);
    }
}

