package com.lmrj.fab.eqp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import com.lmrj.fab.eqp.entity.FabEquipmentBind;
import com.lmrj.fab.eqp.mapper.FabEquipmentBindMapper;
import com.lmrj.fab.eqp.service.IFabEquipmentBindService;
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
public class FabEquipmentBindServiceImpl extends CommonServiceImpl<FabEquipmentBindMapper, FabEquipmentBind> implements IFabEquipmentBindService {

    /**
     * 展示关联传感器信息
     * @param parentEqpId
     * @return
     */
    @Override
    public List<FabEquipmentBind> getIotEquipmenetBindList(String parentEqpId) {

        return baseMapper.selectList(new EntityWrapper<FabEquipmentBind>().eq("parent_eqp_id",parentEqpId));
    }

    /**
     * 保存或更新绑定
     * @param inList
     */
    @Override
    public void saveBindList(List<FabEquipmentBind> inList) {
        for (FabEquipmentBind bind:inList) {
            if(ObjectUtil.isNullOrEmpty(bind.getId())){
               baseMapper.insert(bind) ;
            }else{
                FabEquipmentBind oldbind =     baseMapper.selectById(bind.getId())  ;
                BeanUtils.copyProperties(bind, oldbind);
                baseMapper.updateById(oldbind);
            }
        }
    }

    @Override
    public void insertBlankInfo(List<FabModelTemplateBody> list,String eqpId) {
        FabEquipmentBind a = new FabEquipmentBind();
        for (FabModelTemplateBody body:list) {
            a.setEqpId("");
            a.setParentEqpId(eqpId);
            a.setTemplateId(body.getTemplateId());
            a.setTemplateBodyId(body.getId());
            baseMapper.insert(a);

        }
    }
}
