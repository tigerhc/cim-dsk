package com.lmrj.cim.modules.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.cim.modules.oa.entity.OaNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 *  通知公告数据库控制层接口
 */
@Mapper
public interface OaNotificationMapper extends BaseMapper<OaNotification> {

    @Select("select title,content,create_date from oa_notification where del_flag = '0'")
    List<Map<String,Object>> findList();

}
