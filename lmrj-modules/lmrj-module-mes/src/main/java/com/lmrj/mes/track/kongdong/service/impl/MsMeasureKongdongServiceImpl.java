package com.lmrj.mes.track.kongdong.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.track.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.track.kongdong.mapper.MsMeasureKongdongMapper;
import com.lmrj.mes.track.kongdong.service.IMsMeasureKongdongService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("msMeasureKongdongService")
public class MsMeasureKongdongServiceImpl extends CommonServiceImpl<MsMeasureKongdongMapper, MsMeasureKongdong> implements IMsMeasureKongdongService {

}
