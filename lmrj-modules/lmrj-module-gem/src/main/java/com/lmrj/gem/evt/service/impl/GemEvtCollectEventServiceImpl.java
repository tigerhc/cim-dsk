package com.lmrj.gem.evt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.gem.evt.entity.GemEvtCollectEvent;
import com.lmrj.gem.evt.mapper.GemEvtCollectEventMapper;
import com.lmrj.gem.evt.service.IGemEvtCollectEventService;
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
public class GemEvtCollectEventServiceImpl extends CommonServiceImpl<GemEvtCollectEventMapper, GemEvtCollectEvent> implements IGemEvtCollectEventService {
    public void deleteByIdnoLogic(String id){
        this.baseMapper.deleteByIdnoLogic(id);
    }

    public void deleteByDatenoLogic(Date time) {
        this.baseMapper.deleteByDatenoLogic(time);
    }
}
