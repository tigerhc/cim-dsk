package com.lmrj.mes.track.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.mes.track.entity.MesLotTrackLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.track.controller
 * @title: mes_lot_track_log控制器
 * @description: mes_lot_track_log控制器
 * @author: 张伟江
 * @date: 2020-04-28 14:03:29
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("mes/meslottracklog")
@ViewPrefix("mes/meslottracklog")
@RequiresPathPermission("mes:meslottracklog")
@LogAspectj(title = "mes_lot_track_log")
public class MesLotTrackLogController extends BaseCRUDController<MesLotTrackLog> {

}
