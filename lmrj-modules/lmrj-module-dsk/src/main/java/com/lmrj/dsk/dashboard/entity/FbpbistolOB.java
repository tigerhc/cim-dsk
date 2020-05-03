package com.lmrj.dsk.dashboard.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class FbpbistolOB implements Serializable {
	private String tool_id;
	private String tool_stat;
	private String port_id;
	private String unq_seq_id;
	private String port_typ;
	private String port_ng_flg;
	private String port_stat;
	private Timestamp port_timestamp;
	private Timestamp lst_xfr_timestamp;
	private String from_stk_id;
	private String to_stk_id;
	private String rtd_flg;
	private String xfr_node;
	private String xfr_id;
	private String port_active_flg;
	public String getTool_id() {
		return tool_id;
	}
	public void setTool_id(String tool_id) {
		this.tool_id = tool_id;
	}
	public String getPort_id() {
		return port_id;
	}
	public void setPort_id(String port_id) {
		this.port_id = port_id;
	}
	public String getUnq_seq_id() {
		return unq_seq_id;
	}
	public void setUnq_seq_id(String unq_seq_id) {
		this.unq_seq_id = unq_seq_id;
	}
	public String getPort_typ() {
		return port_typ;
	}
	public void setPort_typ(String port_typ) {
		this.port_typ = port_typ;
	}
	public String getPort_ng_flg() {
		return port_ng_flg;
	}
	public void setPort_ng_flg(String port_ng_flg) {
		this.port_ng_flg = port_ng_flg;
	}
	public String getPort_stat() {
		return port_stat;
	}
	public void setPort_stat(String port_stat) {
		this.port_stat = port_stat;
	}
	public Timestamp getPort_timestamp() {
		return port_timestamp;
	}
	public void setPort_timestamp(Timestamp port_timestamp) {
		this.port_timestamp = port_timestamp;
	}
	public Timestamp getLst_xfr_timestamp() {
		return lst_xfr_timestamp;
	}
	public void setLst_xfr_timestamp(Timestamp lst_xfr_timestamp) {
		this.lst_xfr_timestamp = lst_xfr_timestamp;
	}
	public String getFrom_stk_id() {
		return from_stk_id;
	}
	public void setFrom_stk_id(String from_stk_id) {
		this.from_stk_id = from_stk_id;
	}
	public String getTo_stk_id() {
		return to_stk_id;
	}
	public void setTo_stk_id(String to_stk_id) {
		this.to_stk_id = to_stk_id;
	}
	public String getRtd_flg() {
		return rtd_flg;
	}
	public void setRtd_flg(String rtd_flg) {
		this.rtd_flg = rtd_flg;
	}
	public String getXfr_node() {
		return xfr_node;
	}
	public void setXfr_node(String xfr_node) {
		this.xfr_node = xfr_node;
	}
	public String getXfr_id() {
		return xfr_id;
	}
	public void setXfr_id(String xfr_id) {
		this.xfr_id = xfr_id;
	}
	public String getPort_active_flg() {
		return port_active_flg;
	}
	public void setPort_active_flg(String port_active_flg) {
		this.port_active_flg = port_active_flg;
	}
	public String getTool_stat() {
		return tool_stat;
	}
	public void setTool_stat(String tool_stat) {
		this.tool_stat = tool_stat;
	}


}
