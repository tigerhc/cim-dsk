package com.lmrj.edc.particle.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.edc.particle.entity.ParticleDataBean;
import com.lmrj.edc.particle.mapper.EdcParticleMapper;
import com.lmrj.edc.particle.service.IEdcParticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@Service("edcparticleService")
public class EdcParticleServiceImpl extends CommonServiceImpl<EdcParticleMapper, ParticleDataBean> implements IEdcParticleService {
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
                particle03.add(item.getParticle03());
                particle05.add(item.getParticle05());
                particle1.add(item.getParticle1());
                particle3.add(item.getParticle3());
                particle5.add(item.getParticle5());
                particle10.add(item.getParticle10());
                temp.add(item.getTemp());
                humidity.add(item.getHumidity());
                samplingFlow.add(item.getSamplingFlow());
                windSpeed.add(item.getWindSpeed());
                pressureDiff.add(item.getPressureDiff());
            }
        }
        rs.put("0.3??m", particle03);
        rs.put("0.5??m", particle05);
        rs.put("1??m", particle1);
        rs.put("3??m", particle3);
        rs.put("5??m", particle5);
        rs.put("10??m", particle10);
        rs.put("??????", temp);
        rs.put("??????", humidity);
        rs.put("??????", samplingFlow);
        rs.put("??????", windSpeed);
        rs.put("??????", pressureDiff);
        rs.put("date", time);
        return rs;
    }

    @Override
    public List<Map<String, Object>> getParticleEqps() {
        return baseMapper.getParticleEqps();
    }

    @Override
    public void backData() {

    }
}
