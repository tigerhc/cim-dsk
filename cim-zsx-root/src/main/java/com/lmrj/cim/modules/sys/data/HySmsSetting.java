package com.lmrj.cim.modules.sys.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.lmrj.util.io.PropertiesEditUtil;

@SuppressWarnings("serial")
public class HySmsSetting implements Serializable {
	public static final String PROPERTIES_PATH = "sms.properties";
	private String apiid;
	private String apikey;

	public HySmsSetting() {

	}

	public void load() {
		load(PROPERTIES_PATH);
	}

	public void load(String propertiesPath) {
		PropertiesEditUtil propertiesUtil = new PropertiesEditUtil(propertiesPath);
		this.apiid = propertiesUtil.getString("sms.account.apiid");
		this.apikey = propertiesUtil.getString("sms.account.apikey");
	}

	public void set() {
		set(PROPERTIES_PATH);
	}

	public void set(String propertiesPath) {
		PropertiesEditUtil propertiesUtil = new PropertiesEditUtil(propertiesPath);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("sms.account.apiid", apiid);
		dataMap.put("sms.account.apikey", apikey);

		propertiesUtil.set(dataMap);
	}

	public String getApiid() {
		return apiid;
	}

	public void setApiid(String apiid) {
		this.apiid = apiid;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

}
