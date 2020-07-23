package com.lmrj.edc.config.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.config.service.IEdcConfigFileCsvService;
import com.lmrj.edc.config.entity.EdcConfigFileCsv;
import com.lmrj.edc.config.mapper.EdcConfigFileCsvMapper;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.service.IFabEquipmentModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.config.service.impl
 * @title: edc_config_file_csv服务实现
 * @description: edc_config_file_csv服务实现
 * @author: 张伟江
 * @date: 2020-07-23 16:12:15
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */
@Transactional
@Service("edcConfigFileCsvService")
public class EdcConfigFileCsvServiceImpl extends CommonServiceImpl<EdcConfigFileCsvMapper, EdcConfigFileCsv> implements IEdcConfigFileCsvService {

    @Autowired
    private IFabEquipmentModelService fabEquipmentModelService;

    @Override
    public String findTitle(String eqpId,String fileType) {
        return baseMapper.findTitle(eqpId,fileType);
    }

    @Override
    public List<FabEquipmentModel> getEqpModelMessage() {
        List<FabEquipmentModel> modelList = fabEquipmentModelService.selectList(new EntityWrapper<>());
        List<FabEquipmentModel> dataList = new ArrayList<>();
        for (FabEquipmentModel fabEquipmentModel : modelList) {
            List<String> fileTypes = baseMapper.getFileType(fabEquipmentModel.getId());
            for (String fileType : fileTypes) {
                fabEquipmentModel.setFileType(fileType);
                dataList.add(fabEquipmentModel);
            }
        }
        return dataList;
    }
}
