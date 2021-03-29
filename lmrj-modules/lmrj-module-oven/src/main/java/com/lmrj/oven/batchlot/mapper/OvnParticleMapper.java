package com.lmrj.oven.batchlot.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.oven.batchlot.entity.ParticleDataBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OvnParticleMapper extends BaseMapper<ParticleDataBean> {
    List<ParticleDataBean> getList(Map<String, Object> param);
}
