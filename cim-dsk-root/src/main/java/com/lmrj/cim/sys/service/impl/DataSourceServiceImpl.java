package com.lmrj.cim.sys.service.impl;

import com.lmrj.cim.sys.service.IDataSourceService;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.entity.DataSource;
import com.lmrj.cim.modules.sys.mapper.DataSourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: 数据源
 * @Description: 数据源
 * @author lmrj
 * @date 2017-05-10 12:01:57
 * @version V1.0
 *
 */
@Transactional
@Service("dataSourceService")
public class DataSourceServiceImpl  extends CommonServiceImpl<DataSourceMapper,DataSource> implements IDataSourceService {

}
