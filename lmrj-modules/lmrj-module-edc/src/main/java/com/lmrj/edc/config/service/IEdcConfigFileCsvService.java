package com.lmrj.edc.config.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.edc.config.entity.EdcConfigFileCsv;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.config.service
* @title: edc_config_file_csv服务接口
* @description: edc_config_file_csv服务接口
* @author: 张伟江
* @date: 2020-07-23 16:12:15
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IEdcConfigFileCsvService extends ICommonService<EdcConfigFileCsv> {
    List<FabEquipmentModel> getEqpModelMessage();
    String findTitle(String eqpId,String fileType);
}
