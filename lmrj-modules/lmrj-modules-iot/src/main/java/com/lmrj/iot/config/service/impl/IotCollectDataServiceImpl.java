package com.lmrj.iot.config.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.iot.config.entity.IotCollectData;
import com.lmrj.iot.config.mapper.IotCollectDataMapper;
import com.lmrj.iot.config.service.IIotCollectDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.iot.config.service.impl
* @title: ot_collect_data服务实现
* @description:ot_collect_data服务实现
* @author: wdj
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("iotCollectDataService")
public class IotCollectDataServiceImpl extends CommonServiceImpl<IotCollectDataMapper, IotCollectData> implements IIotCollectDataService {

}