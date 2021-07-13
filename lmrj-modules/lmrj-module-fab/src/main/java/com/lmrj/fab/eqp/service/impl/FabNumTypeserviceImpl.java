package com.lmrj.fab.eqp.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabNumType;
import com.lmrj.fab.eqp.mapper.FabNumTypeMapper;
import com.lmrj.fab.eqp.service.IFabNumTypeservice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**示数类型实体
 * @author wdj
 * @date 2021-06-02 16:40
 */
@Transactional
@Service("sensornumtypeservice")
public class FabNumTypeserviceImpl extends CommonServiceImpl<FabNumTypeMapper, FabNumType> implements IFabNumTypeservice {
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
        FabNumType sensorNumType = new FabNumType();
        sensorNumType.setNumType(numType);
        sensorNumType.setClassCode(classCode);
        return baseMapper.selectOne(sensorNumType)==null?"":baseMapper.selectOne(sensorNumType).getId();
    }
}
