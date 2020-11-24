package com.lmrj.mes.kongdong.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.ms.record.service
* @title: ms_measure_record服务接口
* @description: ms_measure_record服务接口
* @author: 张伟江
* @date: 2020-06-06 18:36:32
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IMsMeasureKongdongService extends ICommonService<MsMeasureKongdong> {
    int findKongdongData(String lineNo, String productionName, String lotNo);
}
