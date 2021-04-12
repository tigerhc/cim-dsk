package com.lmrj.edc.param.service.impl;

import com.google.common.collect.Maps;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.param.entity.EdcEqpLogParam;
import com.lmrj.edc.param.mapper.EdcEqpLogParamMapper;
import com.lmrj.edc.param.service.IEdcEqpLogParamService;
import com.lmrj.util.mapper.JsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.param.service.impl
* @title: edc_eqp_log_param服务实现
* @description: edc_eqp_log_param服务实现
 * @author: 高雪君
 * @date: 2021-03-30 09:05:34
 * @copyright: 2020 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcEqpLogParamService")
public class EdcEqpLogParamServiceImpl extends CommonServiceImpl<EdcEqpLogParamMapper, EdcEqpLogParam> implements IEdcEqpLogParamService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public List<String> findParamEqpId(){
        return baseMapper.findParamEqpId();
    }
    @Override
    public List<EdcEqpLogParam> findLogParamList(){
        return baseMapper.findLogParamList();
    }

    public String findCsvLogParam(){
        Map<String, Map<String,String>> map = Maps.newHashMap();
        List<EdcEqpLogParam> paramList = findLogParamList();
        List<String> eqpIdList = findParamEqpId();
        for (String eqpId : eqpIdList) {
            Map<String,String> paramMap = new HashMap<>();
            map.put(eqpId,paramMap);
        }
        if(paramList == null || paramList.size()==0){
            return null;
        }
        for (EdcEqpLogParam eqpLogParam : paramList) {
            String eqpId = eqpLogParam.getEqpId();
            map.get(eqpId).put(eqpLogParam.getParamKey(),eqpLogParam.getParamValue());
        }
        String msg = JsonUtil.toJsonString(map);
        return msg;
    }
}