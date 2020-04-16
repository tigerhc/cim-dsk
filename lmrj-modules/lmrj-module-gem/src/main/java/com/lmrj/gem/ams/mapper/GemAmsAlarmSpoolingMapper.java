package com.lmrj.gem.ams.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.gem.ams.entity.GemAmsAlarmSpooling;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zwj
 * @since 2019-04-21
 */
@Mapper
public interface GemAmsAlarmSpoolingMapper extends BaseMapper<GemAmsAlarmSpooling> {
    public void transfer(@Param("id") String id);
    public void deleteByIdnoLogic(@Param("id") String id);
}
