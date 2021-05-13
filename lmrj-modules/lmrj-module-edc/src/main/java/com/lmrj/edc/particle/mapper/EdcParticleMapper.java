package com.lmrj.edc.particle.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.particle.entity.ParticleDataBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EdcParticleMapper extends BaseMapper<ParticleDataBean> {
    List<ParticleDataBean> getList(Map<String, Object> param);

    List<Map<String, Object>> getParticleEqps();

//    void backData();
}
