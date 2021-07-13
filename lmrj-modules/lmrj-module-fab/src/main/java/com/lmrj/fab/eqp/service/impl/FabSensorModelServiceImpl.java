package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.lmrj.common.http.DuplicateValid;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.query.data.Page;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.entity.FabSensor;
import com.lmrj.fab.eqp.entity.FabSensorModel;
import com.lmrj.fab.eqp.mapper.FabSensorModelMapper;
import com.lmrj.fab.eqp.service.IFabSensorModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Transactional
@Service("fabsensormodelService")
public class FabSensorModelServiceImpl   extends CommonServiceImpl<FabSensorModelMapper, FabSensorModel> implements IFabSensorModelService {
    @Override
    public List<Map> findLookup() {
        return baseMapper.findLookup();
    }

    @Override
    public List<String> manufacturerNameList() {
        return baseMapper.manufacturerNameList();
    }

    @Override
    public List<String> classCodeList() {
        return baseMapper.classCodeList();
    }

    @Override
    public List<String> noTemClassCodeList() {
        return baseMapper.noTemClassCodeList();
    }

    @Override
    public List<String> getTypeList(String flag, String ID) {
        if(flag!=null&&"1".equals(flag)){
            return   baseMapper.parentTypeList(ID) ;
        }else{
            return baseMapper.typeList(ID);
        }

    }

    /**
     * 获取所有模板
     * @return
     */
    @Override
    public List<Map> getAlltemplateList(){
        return baseMapper.getAlltemplateList();
    }

    /**
     * 获取厂商
     * @param modelId
     * @return
     */
    @Override
    public String manufacturerName(String modelId) {
        FabSensorModel a = new FabSensorModel();
        a.setClassCode(modelId);
        a = baseMapper.selectOne(a);
        return a.getManufacturerName();

    }
}
