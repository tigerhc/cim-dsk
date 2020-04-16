package com.lmrj.core.sys.service;


import com.lmrj.common.mybatis.mvc.service.ICommonService;
import com.lmrj.core.sys.entity.Dict;
import com.lmrj.cim.utils.DictUtils;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @author jwcg
 * @date 2017-02-09 09:05:29
 * @version V1.0
 *
 */
public interface IDictService extends ICommonService<Dict> {
    List<Dict> selectDictList();
    List<DictUtils.Dict> findDictByCode(String dictCode);
}
