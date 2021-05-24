package com.lmrj.iot.config.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.iot.config.entity.IotCollectDataHis;

import java.util.List;

/**
 * @author wdj
 * @date 2021-05-13 15:44
 */
public interface IIotCollectDataHisService extends ICommonService<IotCollectDataHis> {
    void endDate(IotCollectDataHis iotCollectDataHis);
    List<FabEquipment> getFabList();
}
