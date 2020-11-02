package com.lmrj.edc.param.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.common.mybatis.mvc.wrapper.EntityWrapper;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.edc.param.entity.EdcParamRecord;
import com.lmrj.edc.param.entity.EdcParamRecordDtl;
import com.lmrj.edc.param.mapper.EdcParamRecordMapper;
import com.lmrj.edc.param.service.IEdcParamRecordDtlService;
import com.lmrj.edc.param.service.IEdcParamRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**   
 * @Title: edc_param_record
 * @Description: edc_param_record
 * @author zhangweijiang
 * @date 2019-06-11 21:26:40
 * @version V1.0   
 *
 */
@Transactional
@Service("edcParamRecordService")
@Slf4j
public class EdcParamRecordServiceImpl  extends CommonServiceImpl<EdcParamRecordMapper,EdcParamRecord> implements  IEdcParamRecordService {
	@Autowired
	private IEdcParamRecordDtlService edcParamRecordDtlService;

	@Override
	public EdcParamRecord selectById(Serializable id){
		EdcParamRecord edcParamRecord = super.selectById(id);
		List<EdcParamRecordDtl> edcParamRecordDtlList = edcParamRecordDtlService.selectList(new EntityWrapper<EdcParamRecordDtl>(EdcParamRecordDtl.class).eq("RECORD_ID",id));
		edcParamRecord.setEdcParamRecordDtlList(edcParamRecordDtlList);
		return edcParamRecord;
	}

	@Override
	public boolean insert(EdcParamRecord edcParamRecord) {
		// 保存主表
		super.insert(edcParamRecord);
		// 保存edc_param_record_dtl
		//StringEscapeUtils.unescapeHtml4()
		//String edcParamRecordDtlListJson = StringEscapeUtils
		//		.unescapeHtml4(ServletUtils.getRequest().getParameter("_detail"));
		//List<EdcParamRecordDtl> edcParamRecordDtlList = JSONObject.parseArray(edcParamRecordDtlListJson, EdcParamRecordDtl.class);
		List<EdcParamRecordDtl> edcParamRecordDtlList = edcParamRecord.getEdcParamRecordDtlList();
		for (EdcParamRecordDtl edcParamRecordDtl : edcParamRecordDtlList) {
			// 保存字段列表
			edcParamRecordDtl.setRecordId(edcParamRecord.getId());
			//edcParamRecordDtlService.insert(edcParamRecordDtl);
			//edcParamRecordDtlService.insertBatch()
		}
		edcParamRecordDtlService.insertBatch(edcParamRecordDtlList,1000);
		return true;
	}
	
	@Override
	public boolean insertOrUpdate(EdcParamRecord edcParamRecord) {
		try {
			// 获得以前的数据
			List<EdcParamRecordDtl> oldEdcParamRecordDtlList = edcParamRecordDtlService.selectList(new EntityWrapper<EdcParamRecordDtl>(EdcParamRecordDtl.class).eq("recordId.id",edcParamRecord.getId()));
			// 字段
			String edcParamRecordDtlListJson = StringEscapeUtils
					.unescapeHtml4(ServletUtils.getRequest().getParameter("_detail"));
			List<EdcParamRecordDtl> edcParamRecordDtlList = JSONObject.parseArray(edcParamRecordDtlListJson,
					EdcParamRecordDtl.class);
			// 更新主表
			super.insertOrUpdate(edcParamRecord);
			edcParamRecordDtlList = JSONObject.parseArray(edcParamRecordDtlListJson, EdcParamRecordDtl.class);
			List<String> newsEdcParamRecordDtlIdList = new ArrayList<String>();
			// 保存或更新数据
			for (EdcParamRecordDtl edcParamRecordDtl : edcParamRecordDtlList) {
				// 设置不变更的字段
				if (StringUtil.isEmpty(edcParamRecordDtl.getId())) {
					// 保存字段列表
					edcParamRecordDtl.setRecordId(edcParamRecord.getId());
					edcParamRecordDtlService.insert(edcParamRecordDtl);
				} else {
					edcParamRecordDtlService.insertOrUpdate(edcParamRecordDtl);
				}
				newsEdcParamRecordDtlIdList.add(edcParamRecordDtl.getId());
			}

			// 删除老数据
			for (EdcParamRecordDtl edcParamRecordDtl : oldEdcParamRecordDtlList) {
				String edcParamRecordDtlId = edcParamRecordDtl.getId();
				if (!newsEdcParamRecordDtlIdList.contains(edcParamRecordDtlId)) {
					edcParamRecordDtlService.deleteById(edcParamRecordDtlId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return true;
	}

}
