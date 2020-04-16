package com.lmrj.gem.evt.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.gem.evt.entity.GemEvtCollectEventSpooling;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zwj
 * @since 2019-04-21
 */
public interface IGemEvtCollectEventSpoolingService extends ICommonService<GemEvtCollectEventSpooling> {
    void insertEvent(String eventId);
}
