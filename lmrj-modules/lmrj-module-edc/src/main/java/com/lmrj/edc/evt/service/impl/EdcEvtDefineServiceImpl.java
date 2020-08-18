package com.lmrj.edc.evt.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.evt.service.IEdcEvtDefineService;
import com.lmrj.edc.evt.entity.EdcEvtDefine;
import com.lmrj.edc.evt.mapper.EdcEvtDefineMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.evt.service.impl
* @title: edc_evt_define服务实现
* @description: edc_evt_define服务实现
* @author: 张伟江
* @date: 2019-06-14 16:01:31
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcEvtDefineService")
public class EdcEvtDefineServiceImpl  extends CommonServiceImpl<EdcEvtDefineMapper,EdcEvtDefine> implements  IEdcEvtDefineService {
    @Override
    public EdcEvtDefine findDataByEvtId(String eventCode){
        return baseMapper.findDataByEvtId(eventCode);
    }
}