package com.lmrj.edc.evt.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.evt.entity.EdcErrLogBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EdcErrLogMapper extends BaseMapper<EdcErrLogBean> {
    void saveErrMsg(EdcErrLogBean err);
}
