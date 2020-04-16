package com.lmrj.gem.evt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.gem.evt.entity.GemEvtCollectEventSpooling;
import com.lmrj.gem.evt.mapper.GemEvtCollectEventSpoolingMapper;
import com.lmrj.gem.evt.service.IGemEvtCollectEventSpoolingService;
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
public class GemEvtCollectEventSpoolingServiceImpl extends CommonServiceImpl<GemEvtCollectEventSpoolingMapper, GemEvtCollectEventSpooling> implements IGemEvtCollectEventSpoolingService {

    public void transfer(String id){
        this.baseMapper.transfer(id);
        this.baseMapper.deleteByIdnoLogic(id);
    }

    public void insertEvent(String eventId){
        GemEvtCollectEventSpooling gemEvtCollectEventSpooling = new GemEvtCollectEventSpooling();
        gemEvtCollectEventSpooling.setEventId(eventId);
        gemEvtCollectEventSpooling.setCreateDate(new Date());
        this.insert(gemEvtCollectEventSpooling);
    }
}
