package com.lmrj.cim.sys.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.core.sys.entity.Dict;
import com.lmrj.cim.modules.sys.mapper.DictMapper;
import com.lmrj.core.sys.service.IDictService;
import com.lmrj.cim.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("dictService")
public class DictServiceImpl extends CommonServiceImpl<DictMapper, Dict> implements IDictService {

	@Override
	public List<Dict> selectDictList() {
		return baseMapper.selectDictList();
	}

	@Override
	public List<DictUtils.Dict> findDictByCode(String dictCode) {
		return DictUtils.getDictList(dictCode);
		//return baseMapper.findDictByCode(dictCode);
	}

}
