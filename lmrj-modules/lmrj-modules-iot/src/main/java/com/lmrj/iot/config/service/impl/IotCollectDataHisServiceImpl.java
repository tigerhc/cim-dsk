package com.lmrj.iot.config.service.impl;

import com.lmrj.cim.utils.UserUtil;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.entity.User;
import com.lmrj.fab.userRole.service.IIotUserRoleService;
import com.lmrj.fab.userRole.service.IIotRoleEqpService;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.iot.config.entity.IotCollectDataHis;
import com.lmrj.iot.config.mapper.IotCollectDataHisMapper;
import com.lmrj.iot.config.service.IIotCollectDataHisService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wdj
 * @date 2021-05-13 15:46
 */
@Transactional
@Service("iotCollectDataHisService")
public class IotCollectDataHisServiceImpl extends CommonServiceImpl<IotCollectDataHisMapper, IotCollectDataHis> implements IIotCollectDataHisService {
    @Autowired
    private IIotRoleEqpService iroleEqpService;
    @Autowired
    private IIotUserRoleService iUserRoleService;
    /**
     * 采集数据归档
     * @param iotCollectDataHis
     */
    @Override
    public void endDate(IotCollectDataHis iotCollectDataHis) {
        baseMapper.insert(iotCollectDataHis);
    }

    /**
     * 获取当前登录人的设备列表
     * @return
     */
    @Override
    public List<FabEquipment> getFabList() {
        List<FabEquipment> retList = new ArrayList<>();
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String userId = getPrincipalProperty(principal, "id");
        User user = UserUtil.getUser(userId);
        List<String> roleList =  iUserRoleService.getRoleByUserId(user.getId());
        List<String> eqpList = iroleEqpService.getEqpByRoleList(roleList);
        FabEquipment fabEquipment = new FabEquipment();
        for (String eqpId:eqpList) {
            fabEquipment.setEqpId(eqpId);
            retList.add(fabEquipment);
        }
        return retList;
    }


    /**
     * 获取人员id
     * @param principal
     * @param property
     * @return
     */
    public static String getPrincipalProperty(Object principal, String property) {
        String strValue = null;

        try {
            BeanInfo bi = Introspector.getBeanInfo(principal.getClass());

            // Loop through the properties to get the string value of the
            // specified property
            boolean foundProperty = false;
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(property)) {
                    Object value = pd.getReadMethod().invoke(principal,
                            (Object[]) null);
                    strValue = String.valueOf(value);
                    foundProperty = true;
                    break;
                }
            }

            if (!foundProperty) {
                final String message = "Property [" + property
                        + "] not found in principal of type ["
                        + principal.getClass().getName() + "]";

                throw new RuntimeException(message);
            }

        } catch (Exception e) {
            final String message = "Error reading property [" + property
                    + "] from principal of type ["
                    + principal.getClass().getName() + "]";

            throw new RuntimeException(message, e);
        }

        return strValue;
    }


}
