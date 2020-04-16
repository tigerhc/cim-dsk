package com.lmrj.edc.state.service.impl;

import com.google.common.collect.Lists;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.edc.quartz.MapUtil;
import com.lmrj.edc.state.entity.EdcEqpState;
import com.lmrj.edc.state.entity.RptEqpStateDay;
import com.lmrj.edc.state.mapper.EdcEqpStateMapper;
import com.lmrj.edc.state.service.IEdcEqpStateService;
import com.lmrj.edc.state.service.IRptEqpStateDayService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
* All rights Reserved, Designed By www.lmrj.com
*
* @version V1.0
* @package com.lmrj.edc.state.service.impl
* @title: edc_eqp_state服务实现
* @description: edc_eqp_state服务实现
* @author: 张伟江
* @date: 2020-02-20 01:26:46
* @copyright: 2018 www.lmrj.com Inc. All rights reserved.
*/
@Transactional
@Service("edcEqpStateService")
public class EdcEqpStateServiceImpl  extends CommonServiceImpl<EdcEqpStateMapper,EdcEqpState> implements  IEdcEqpStateService {

    @Autowired
    private EdcEqpStateMapper edcEqpStateMapper;

    @Autowired
    private IRptEqpStateDayService rptEqpStateDayService;
    @Override
    public int syncEqp(String time) {

        List<EdcEqpState> eqpStateList=edcEqpStateMapper.getAllByTime(time);

        if(CollectionUtils.isEmpty(eqpStateList)){
            return 0;
        }
        List<RptEqpStateDay> rptEqpStateDayList= Lists.newArrayList();
        Map<String, List<EdcEqpState>> stringListMap = MapUtil.listToMapList(eqpStateList, e -> String.valueOf(
                e.getEqpId()));
        for (List<EdcEqpState> list : stringListMap.values()) {
            RptEqpStateDay rptEqpStateDay=new RptEqpStateDay();
            rptEqpStateDay.setEqpId(list.get(0).getEqpId());
            rptEqpStateDay.setPeriodDate(time.replaceAll("-",""));
            Double idel=0.0;
            Double run=0.0;
            Double down=0.0;
            for(EdcEqpState edcEqpState:list){
                if("run".equalsIgnoreCase(edcEqpState.getState())){
                    run=run+edcEqpState.getStateTimes();
                }
                if("down".equalsIgnoreCase(edcEqpState.getState())){
                    down=down+edcEqpState.getStateTimes();
                }
                if("idel".equalsIgnoreCase(edcEqpState.getState())){
                    idel=idel+edcEqpState.getStateTimes();
                }
            }
            rptEqpStateDay.setRunTime(run);
            rptEqpStateDay.setDownTime(down);
            rptEqpStateDay.setIdleTime(idel);
            rptEqpStateDayList.add(rptEqpStateDay);
        }
        if(CollectionUtils.isEmpty(rptEqpStateDayList)){
            return 0;
        }
        //先删除day表 按照时间删除 在插入
        rptEqpStateDayService.delete(new EntityWrapper<RptEqpStateDay>().eq("period_date",time.replaceAll("-","")));
        rptEqpStateDayService.insertBatch(rptEqpStateDayList);
        return rptEqpStateDayList.size();
    }
}
