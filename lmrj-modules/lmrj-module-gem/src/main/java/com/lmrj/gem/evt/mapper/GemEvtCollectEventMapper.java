package com.lmrj.gem.evt.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.gem.evt.entity.GemEvtCollectEvent;
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
public interface GemEvtCollectEventMapper extends BaseMapper<GemEvtCollectEvent> {
    public void deleteByIdnoLogic(@Param("id") String id);

    public void deleteByDatenoLogic(@Param("date") Date date);
}
