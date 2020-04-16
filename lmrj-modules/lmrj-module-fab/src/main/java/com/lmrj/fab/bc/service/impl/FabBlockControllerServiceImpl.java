package com.lmrj.fab.bc.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.bc.service.IFabBlockControllerService;
import com.lmrj.fab.bc.entity.FabBlockController;
import com.lmrj.fab.bc.mapper.FabBlockControllerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.fab.bc.service.impl
* @title: fab_block_controller服务实现
* @description: fab_block_controller服务实现
* @author: zhangweijiang
* @date: 2019-07-15 01:03:01
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("fabBlockControllerService")
public class FabBlockControllerServiceImpl  extends CommonServiceImpl<FabBlockControllerMapper,FabBlockController> implements  IFabBlockControllerService {

}