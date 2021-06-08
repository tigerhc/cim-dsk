package com.lmrj.fab.eqp.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.fab.eqp.entity.FabModelTemplateBody;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 9:01
 */
@Mapper
public interface FabModelTemplateBodyMapper  extends BaseMapper<FabModelTemplateBody> {

    List<Map> getOneTemplateList(@Param("id") String id);

    List<FabModelTemplateBody> getNoBindInfo(@Param("eqpId") String eqpId);
}
