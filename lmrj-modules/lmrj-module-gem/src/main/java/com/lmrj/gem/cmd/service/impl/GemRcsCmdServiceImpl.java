package com.lmrj.gem.cmd.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.gem.cmd.entity.GemRcsCmd;
import com.lmrj.gem.cmd.mapper.GemRcsCmdMapper;
import com.lmrj.gem.cmd.service.IGemRcsCmdService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zwj
 * @since 2019-04-17
 */
@Service
public class GemRcsCmdServiceImpl extends CommonServiceImpl<GemRcsCmdMapper, GemRcsCmd> implements IGemRcsCmdService {

    //public void deleteByDatenoLogic(Date time) {
    //    this.baseMapper.deleteByDatenoLogic(time);
    //}
}
