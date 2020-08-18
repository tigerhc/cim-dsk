package com.lmrj.edc.evt.mapper;

import com.lmrj.edc.evt.entity.EdcEvtDefine;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.evt.mapper
 * @title: edc_evt_define数据库控制层接口
 * @description: edc_evt_define数据库控制层接口
 * @author: 张伟江
 * @date: 2019-06-14 16:01:31
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcEvtDefineMapper extends BaseMapper<EdcEvtDefine> {
    @Select("select * from edc_evt_define where event_code=#{eventCode}")
    EdcEvtDefine findDataByEvtId(@Param("eventCode") String eventCode);
}