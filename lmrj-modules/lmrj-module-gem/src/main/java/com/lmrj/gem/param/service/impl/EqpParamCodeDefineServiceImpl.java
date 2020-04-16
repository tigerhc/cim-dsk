package com.lmrj.gem.param.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.gem.param.entity.EqpParamCodeDefine;
import com.lmrj.gem.param.mapper.EqpParamCodeDefineMapper;
import com.lmrj.gem.param.service.IEqpParamCodeDefineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**   
 * @Title: 设备参数定义
 * @Description: 设备参数统一定义
 * @author zhangweijiang
 * @date 2018-05-22 15:28:42
 * @version V1.0   
 *
 */
@Transactional
@Service("eqpParamCodeDefineService")
public class EqpParamCodeDefineServiceImpl  extends CommonServiceImpl<EqpParamCodeDefineMapper,EqpParamCodeDefine> implements IEqpParamCodeDefineService {

    @Override
    public Map<String, String> findAllParamCodeDefine(){
        Map<String, String> paramDefineMap = new HashMap<String, String>();
        List<Map> list = baseMapper.findAllParamCodeDefine();
        for(Map map : list){
            String processTypeCode = (String) map.get("PROCESS_TYPE_ID");
            String modelId = (String) map.get("MODEL_ID");
            String paramCode = (String) map.get("PARAM_CODE");
            String protocolValue = (String) map.get("PROTOCOL_VALUE");
            paramDefineMap.put(processTypeCode+"-"+modelId+"-"+paramCode, protocolValue);
        }
        return paramDefineMap;
    }

}
