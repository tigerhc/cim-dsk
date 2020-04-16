package com.lmrj.cim.modules.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.core.sys.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
	List<Dict> selectDictList();
	List<Dict> findDictByCode(@Param(value = "code") String code);

}
