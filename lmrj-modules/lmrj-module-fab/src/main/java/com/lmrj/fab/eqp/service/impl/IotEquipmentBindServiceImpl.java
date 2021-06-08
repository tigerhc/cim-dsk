package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.entity.IotEquipmentBind;
import com.lmrj.fab.eqp.mapper.IotEquipmentBindMapper;
import com.lmrj.fab.eqp.service.IIotEquipmentBindService;
import com.lmrj.util.lang.ObjectUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-01 17:25
 */
@Transactional
@Service("iotequipmentbindservice")
public class IotEquipmentBindServiceImpl extends CommonServiceImpl<IotEquipmentBindMapper, IotEquipmentBind> implements IIotEquipmentBindService {

    /**
     * 展示关联传感器信息
     * @param parentEqpId
     * @return
     */
    @Override
    public List<IotEquipmentBind> getIotEquipmenetBindList(String parentEqpId) {

        return baseMapper.selectList(new EntityWrapper<IotEquipmentBind>().eq("parent_eqp_id",parentEqpId));
    }

    /**
     * 保存或更新绑定
     * @param inList
     */
    @Override
    public void saveBindList(List<IotEquipmentBind> inList) {
        for (IotEquipmentBind bind:inList) {
            if(ObjectUtil.isNullOrEmpty(bind.getId())){
               baseMapper.insert(bind) ;
            }else{
                IotEquipmentBind oldbind =     baseMapper.selectById(bind.getId())  ;
                BeanUtils.copyProperties(bind, oldbind);
                baseMapper.updateById(oldbind);
            }
        }
    }

    @Override
    public void insertBlankInfo(List<FabModelTemplateBody> list,String eqpId) {
        IotEquipmentBind a = new IotEquipmentBind();
        for (FabModelTemplateBody body:list) {
            a.setEqpId("");
            a.setParentEqpId(eqpId);
            a.setTemplateId(body.getTemplateId());
            a.setTemplateBodyId(body.getId());
            baseMapper.insert(a);

        }
    }
}
