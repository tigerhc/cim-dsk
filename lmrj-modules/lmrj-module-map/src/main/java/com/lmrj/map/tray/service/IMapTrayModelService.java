package com.lmrj.map.tray.service;

import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.map.tray.entity.MapTrayModel;

import java.util.List;

/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.map.tray.service
* @title: map_tray_config服务接口
* @description: map_tray_config服务接口
* @author: stev
* @date: 2020-08-02 15:29:58
* @copyright: 2019 www.lmrj.com Inc. All rights reserved.
*/
public interface IMapTrayModelService extends ICommonService<MapTrayModel> {
    List<MapTrayModel> getBySelect();
}
