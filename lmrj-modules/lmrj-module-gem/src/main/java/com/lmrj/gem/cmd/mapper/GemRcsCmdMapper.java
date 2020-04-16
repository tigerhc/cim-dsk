package com.lmrj.gem.cmd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.gem.cmd.entity.GemRcsCmd;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zwj
 * @since 2019-04-17
 */
@Mapper
public interface GemRcsCmdMapper extends BaseMapper<GemRcsCmd> {

    //public void deleteByDatenoLogic(@Param("date") Date date);
}
