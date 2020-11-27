package com.lmrj.mes.kongdong.controller;

import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.kongdong.controller
 * @title: ms_measure_kogndong控制器
 * @description: ms_measure_kogndong控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasurekongdong")
@ViewPrefix("ms/msmeasurekongdong")
@RequiresPathPermission("ms:msmeasurekongdong")
@LogAspectj(title = "ms_measure_kongdong")
public class MsMeasureKongdongController extends BaseCRUDController<MsMeasureKongdong> {
//    @Autowired
//    private IMsMeasureKongdongService kongdongService;

//    @RequestMapping(value = "saveBeforeKongdong",method = {RequestMethod.GET, RequestMethod.POST})
//    public Response saveBeforeKongdong(@RequestParam("filePath") int index){
//        Response rs = Response.ok();
//        rs.putList("data",kongdongService.saveBeforeFile(index));
//        return rs;
//    }
}
