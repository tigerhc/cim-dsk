package com.lmrj.dsk.dashboard.entity;

import java.io.Serializable;
import java.util.List;

public class FbpbistolO implements Serializable {
	private String trx_id;
	private String type_id;
	private String rtn_code;
	private String rtn_mesg;
	private long   tbl_cnt;
	private String tool_stat_zh;

	private int total_tools;
	private int total_tools_down;
	private double down_rate;

	private List<FbpbistolOA> oary;
	private List<FbpbistolOB> oaryB;
	private List<Bis_tool> oaryC;
	private long   tbl_cnt_B;
	private String pv_pm_date;
	private String nx_pm_date;
	private String tool_stat;
	private String evt_timestamp;//上次状态变更时间

	public int getTotal_tools() {
		return total_tools;
	}

	public void setTotal_tools(int total_tools) {
		this.total_tools = total_tools;
	}

	public int getTotal_tools_down() {
		return total_tools_down;
	}

	public void setTotal_tools_down(int total_tools_down) {
		this.total_tools_down = total_tools_down;
	}

	public double getDown_rate() {
		return down_rate;
	}

	public void setDown_rate(double down_rate) {
		this.down_rate = down_rate;
	}

	public String getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getRtn_code() {
		return rtn_code;
	}

	public void setRtn_code(String rtn_code) {
		this.rtn_code = rtn_code;
	}

	public String getRtn_mesg() {
		return rtn_mesg;
	}

	public void setRtn_mesg(String rtn_mesg) {
		this.rtn_mesg = rtn_mesg;
	}

	public long getTbl_cnt() {
		return tbl_cnt;
	}

	public void setTbl_cnt(long tbl_cnt) {
		this.tbl_cnt = tbl_cnt;
	}

	public List<FbpbistolOA> getOary() {
		return oary;
	}

	public void setOary(List<FbpbistolOA> oary) {
		this.oary = oary;
	}

	public List<FbpbistolOB> getOaryB() {
		return oaryB;
	}

	public void setOaryB(List<FbpbistolOB> oaryB) {
		this.oaryB = oaryB;
	}

	public long getTbl_cnt_B() {
		return tbl_cnt_B;
	}

	public void setTbl_cnt_B(long tbl_cnt_B) {
		this.tbl_cnt_B = tbl_cnt_B;
	}

	public List<Bis_tool> getOaryC() {
		return oaryC;
	}

	public void setOaryC(List<Bis_tool> oaryC) {
		this.oaryC = oaryC;
	}

	public String getPv_pm_date() {
		return pv_pm_date;
	}

	public void setPv_pm_date(String pv_pm_date) {
		this.pv_pm_date = pv_pm_date;
	}

	public String getNx_pm_date() {
		return nx_pm_date;
	}

	public void setNx_pm_date(String nx_pm_date) {
		this.nx_pm_date = nx_pm_date;
	}

	public String getTool_stat() {
		return tool_stat;
	}

	public void setTool_stat(String tool_stat) {
		this.tool_stat = tool_stat;
	}

	public String getEvt_timestamp() {
		return evt_timestamp;
	}

	public void setEvt_timestamp(String evt_timestamp) {
		this.evt_timestamp = evt_timestamp;
	}

	public String getTool_stat_zh() {
		return tool_stat_zh;
	}

	public void setTool_stat_zh(String tool_stat_zh) {
		this.tool_stat_zh = tool_stat_zh;
	}
}
