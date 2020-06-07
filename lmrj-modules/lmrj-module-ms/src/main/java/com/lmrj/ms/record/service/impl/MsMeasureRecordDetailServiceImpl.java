package com.lmrj.ms.record.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.ms.record.service.IMsMeasureRecordDetailService;
import com.lmrj.ms.record.entity.MsMeasureRecordDetail;
import com.lmrj.ms.record.mapper.MsMeasureRecordDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.ms.record.service.impl
* @title: ms_measure_record_detail服务实现
* @description: ms_measure_record_detail服务实现
* @author: 张伟江
* @date: 2020-06-06 18:36:47
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("msMeasureRecordDetailService")
public class MsMeasureRecordDetailServiceImpl  extends CommonServiceImpl<MsMeasureRecordDetailMapper,MsMeasureRecordDetail> implements  IMsMeasureRecordDetailService {

}