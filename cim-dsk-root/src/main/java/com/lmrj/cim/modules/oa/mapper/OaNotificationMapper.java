package com.lmrj.cim.modules.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.cim.modules.oa.entity.OaNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 *  通知公告数据库控制层接口
 */
@Mapper
public interface OaNotificationMapper extends BaseMapper<OaNotification> {

    @Select("select no_subject,content,create_date from oa_notification where status = 'Y' ORDER BY create_date DESC")
    List<Map<String,Object>> findList();

}
