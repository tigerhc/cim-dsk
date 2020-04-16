package com.lmrj.gem.ams.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.gem.ams.entity.GemAmsAlarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zwj
 * @since 2019-04-21
 */
@Mapper
public interface GemAmsAlarmMapper extends BaseMapper<GemAmsAlarm> {
    public void deleteByIdnoLogic(@Param("id") String id);

    public void deleteByDatenoLogic(@Param("date") Date date);

    public void deleteCMDByDatenoLogic(@Param("date") Date date);

}
