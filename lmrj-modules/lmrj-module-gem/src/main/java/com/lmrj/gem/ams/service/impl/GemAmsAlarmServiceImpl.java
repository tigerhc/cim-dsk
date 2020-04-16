package com.lmrj.gem.ams.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.gem.ams.entity.GemAmsAlarm;
import com.lmrj.gem.ams.mapper.GemAmsAlarmMapper;
import com.lmrj.gem.ams.service.IGemAmsAlarmService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zwj
 * @since 2019-04-21
 */
@Service
public class GemAmsAlarmServiceImpl extends CommonServiceImpl<GemAmsAlarmMapper, GemAmsAlarm> implements IGemAmsAlarmService {
    public void deleteByIdnoLogic(String id){
        this.baseMapper.deleteByIdnoLogic(id);
    }

    public void deleteByDatenoLogic(Date date){
        this.baseMapper.deleteByDatenoLogic(date);
    }

    // TODO: 2019/5/12
    public void deleteCMDByDatenoLogic(Date date){
        this.baseMapper.deleteCMDByDatenoLogic(date);
    }
}
