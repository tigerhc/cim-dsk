package com.lmrj.cim.config;

import com.lmrj.common.disruptor.TaskHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package cn.dataact.lmrj.modules.config
 * @title:
 * @description: 公用任务配置
 * @author: 张飞
 * @date: 2018/3/1 16:06
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
@Configuration
public class TaskConfig {

    @Bean
    public TaskHelper taskHelper(){
        TaskHelper taskHelper=  new TaskHelper();
        taskHelper.setHandlerCount(1);
        taskHelper.setBufferSize(1024);
        return taskHelper;
    }

}
