package com.lmrj.gem.evt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.gem.evt.entity.GemEvtLinkEventReport;
import com.lmrj.gem.evt.mapper.GemEvtLinkEventReportMapper;
import com.lmrj.gem.evt.service.IGemEvtLinkEventReportService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事件 event和report绑定 服务实现类
 * </p>
 *
 * @author zwj
 * @since 2019-04-27
 */
@Service
public class GemEvtLinkEventReportServiceImpl extends CommonServiceImpl<GemEvtLinkEventReportMapper, GemEvtLinkEventReport> implements IGemEvtLinkEventReportService {

}
