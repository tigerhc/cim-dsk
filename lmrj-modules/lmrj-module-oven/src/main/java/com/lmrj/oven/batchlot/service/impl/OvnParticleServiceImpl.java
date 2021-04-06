package com.lmrj.oven.batchlot.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.oven.batchlot.entity.ParticleDataBean;
import com.lmrj.oven.batchlot.mapper.OvnParticleMapper;
import com.lmrj.oven.batchlot.service.IOvnParticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@Service("ovnparticleService")
public class OvnParticleServiceImpl extends CommonServiceImpl<OvnParticleMapper, ParticleDataBean> implements IOvnParticleService {
    @Override
    public Map<String, Object> getEchartData(Map<String, Object> params) {
        Map<String, Object> rs = new HashMap<>();
        List<Integer> particle03 = new ArrayList<>();
        List<Integer> particle05 = new ArrayList<>();
        List<Integer> particle1 = new ArrayList<>();
        List<Integer> particle3 = new ArrayList<>();
        List<Integer> particle5 = new ArrayList<>();
        List<Integer> particle10 = new ArrayList<>();
        List<Double> temp = new ArrayList<>();
        List<Double> humidity = new ArrayList<>();
        List<Double> samplingFlow = new ArrayList<>();
        List<Double> windSpeed = new ArrayList<>();
        List<Double> pressureDiff = new ArrayList<>();
        List<ParticleDataBean> dataList = baseMapper.getList(params);
        List<String> time = new ArrayList<>();
        if(dataList.size()>0){
            for(ParticleDataBean item : dataList){
                time.add(item.getStartTimeStr());
                particle03.add(item.getParticle03His());
                particle05.add(item.getParticle05His());
                particle1.add(item.getParticle1His());
                particle3.add(item.getParticle3His());
                particle5.add(item.getParticle5His());
                particle10.add(item.getParticle10His());
                temp.add(item.getTemp());
                humidity.add(item.getHumidity());
                samplingFlow.add(item.getSamplingFlow());
                windSpeed.add(item.getWindSpeed());
                pressureDiff.add(item.getPressureDiff());
            }
        }
        rs.put("0.3μm", particle03);
        rs.put("0.5μm", particle05);
        rs.put("1μm", particle1);
        rs.put("3μm", particle3);
        rs.put("5μm", particle5);
        rs.put("10μm", particle10);
        rs.put("温度", temp);
        rs.put("湿度", humidity);
        rs.put("流量", samplingFlow);
        rs.put("风速", windSpeed);
        rs.put("压差", pressureDiff);
        rs.put("date", time);
        return rs;
    }

    @Override
    public List<Map<String, Object>> getParticleEqps() {
        return baseMapper.getParticleEqps();
    }
}
