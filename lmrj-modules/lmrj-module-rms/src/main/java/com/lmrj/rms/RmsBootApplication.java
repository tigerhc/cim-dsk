package com.lmrj.rms;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web
 * @title: Web启动入口
 * @description: Web启动入口
 * @author: 张飞
 * @date: 2018/5/22 14:56
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
@EnableSwagger2Doc
@EnableSwagger2
@MapperScan("com.lmrj.rms.**.mapper")
@ComponentScan({"com.lmrj.common.quartz.config","com.lmrj.rms","com.lmrj.fab"})
@SpringBootApplication
public class RmsBootApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RmsBootApplication.class, args);
    }
}

