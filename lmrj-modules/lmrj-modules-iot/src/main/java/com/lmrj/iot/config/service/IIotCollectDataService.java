package com.lmrj.iot.config.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.iot.config.entity.IotCollectData;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.iot.config.service
* @title: ot_collect_data服务接口
* @description: ot_collect_data服务接口
* @author: wdj
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IIotCollectDataService extends ICommonService<IotCollectData> {

    void collectDataAndAlarm(IotCollectData fromdata);

}