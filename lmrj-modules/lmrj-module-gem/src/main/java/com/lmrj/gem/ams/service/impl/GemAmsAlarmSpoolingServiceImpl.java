package com.lmrj.gem.ams.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.gem.ams.entity.GemAmsAlarmSpooling;
import com.lmrj.gem.ams.mapper.GemAmsAlarmSpoolingMapper;
import com.lmrj.gem.ams.service.IGemAmsAlarmSpoolingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zwj
 * @since 2019-04-21
 */
@Service
public class GemAmsAlarmSpoolingServiceImpl extends CommonServiceImpl<GemAmsAlarmSpoolingMapper, GemAmsAlarmSpooling> implements IGemAmsAlarmSpoolingService {
    public void transfer(String id){
        this.baseMapper.transfer(id);
        this.baseMapper.deleteByIdnoLogic(id);
    }
}
