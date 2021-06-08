package com.lmrj.fab.eqp.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.SensorNumType;
import com.lmrj.fab.eqp.mapper.SensorNumTypeMapper;
import com.lmrj.fab.eqp.service.ISensorNumTypeservice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**示数类型实体
 * @author wdj
 * @date 2021-06-02 16:40
 */
@Transactional
@Service("sensornumtypeservice")
public class SensorNumTypeserviceImpl extends CommonServiceImpl<SensorNumTypeMapper, SensorNumType> implements ISensorNumTypeservice {
    @Override
    public List<String> getNumtypeList(String classCode) {
        return baseMapper.getNumtypeList(classCode);
    }

    @Override
    public String getNumType(String id) {

        return baseMapper.selectById(id)==null?"":baseMapper.selectById(id).getNumType();
    }

    @Override
    public String getTypeId(String classCode, String numType) {
        SensorNumType sensorNumType = new SensorNumType();
        sensorNumType.setNumType(numType);
        sensorNumType.setClassCode(classCode);
        return baseMapper.selectOne(sensorNumType)==null?"":baseMapper.selectOne(sensorNumType).getId();
    }
}
