package com.lmrj.core.email.mapper;

import com.lmrj.core.email.entity.EmailSendLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.web.modules.email.mapper
* @title: 邮件发送日志数据库控制层接口
* @description: 邮件发送日志数据库控制层接口
* @author: 张飞
* @date: 2018-09-12 10:58:46
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Mapper
public interface EmailSendLogMapper extends BaseMapper<EmailSendLog> {

    @Select("select * from email_send_log where send_data like concat('%',#{data},'%') order by create_date desc limit 1" )
    EmailSendLog selectEmailLog(@Param("data") String data);
}
