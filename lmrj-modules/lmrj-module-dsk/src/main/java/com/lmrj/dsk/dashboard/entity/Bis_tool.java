package com.lmrj.dsk.dashboard.entity;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Bis_tool entity. @author MyEclipse Persistence Tools
 */

@Component
public class Bis_tool extends  UUIDObject implements java.io.Serializable {

	// Fields

	private String tool_id;
	private String unq_seq_id;
	private String tool_dsc;
	private String unit_typ;
	private String par_tool_id;
	private String root_tool_id;
	private String tool_cate;
	private String bay_id;
	private String floor_code;
	private String tool_trns_cate;
	private String tool_ip;
	private String fab_id_fk;
	private String evt_usr;
	private Timestamp evt_timestamp;
	private String tool_code;
    private String fab_site_fk;
    private String board_flg;
    private String spc_flg;
    private String mes_flg;
    private String pms_flg;
    private String tool_lvl;
    private String sum_flg;

	// Constructors

	/** default constructor */
	public Bis_tool() {
	}


	public String getTool_id() {
		return tool_id;
	}


	public void setTool_id(String tool_id) {
		this.tool_id = tool_id;
	}


	public String getUnq_seq_id() {
		return unq_seq_id;
	}


	public void setUnq_seq_id(String unq_seq_id) {
		this.unq_seq_id = unq_seq_id;
	}


	public String getTool_dsc() {
		return tool_dsc;
	}


	public void setTool_dsc(String tool_dsc) {
		this.tool_dsc = tool_dsc;
	}


	public String getUnit_typ() {
		return unit_typ;
	}


	public void setUnit_typ(String unit_typ) {
		this.unit_typ = unit_typ;
	}


	public String getRoot_tool_id() {
		return root_tool_id;
	}


	public void setRoot_tool_id(String root_tool_id) {
		this.root_tool_id = root_tool_id;
	}


	public String getTool_cate() {
		return tool_cate;
	}


	public void setTool_cate(String tool_cate) {
		this.tool_cate = tool_cate;
	}


	public String getBay_id() {
		return bay_id;
	}


	public void setBay_id(String bay_id) {
		this.bay_id = bay_id;
	}


	public String getFloor_code() {
		return floor_code;
	}


	public void setFloor_code(String floor_code) {
		this.floor_code = floor_code;
	}


	public String getTool_trns_cate() {
		return tool_trns_cate;
	}


	public void setTool_trns_cate(String tool_trns_cate) {
		this.tool_trns_cate = tool_trns_cate;
	}


	public String getTool_ip() {
		return tool_ip;
	}


	public void setTool_ip(String tool_ip) {
		this.tool_ip = tool_ip;
	}


	public String getFab_id_fk() {
		return fab_id_fk;
	}


	public void setFab_id_fk(String fab_id_fk) {
		this.fab_id_fk = fab_id_fk;
	}

	public String getEvt_usr() {
		return evt_usr;
	}


	public void setEvt_usr(String evt_usr) {
		this.evt_usr = evt_usr;
	}


	public Timestamp getEvt_timestamp() {
		return evt_timestamp;
	}

	public void setEvt_timestamp(Timestamp evt_timestamp) {
		this.evt_timestamp = evt_timestamp;
	}


	public String getTool_code() {
		return tool_code;
	}


	public void setTool_code(String tool_code) {
		this.tool_code = tool_code;
	}

	public String getFab_site_fk() {
		return fab_site_fk;
	}

	public void setFab_site_fk(String fab_site_fk) {
		this.fab_site_fk = fab_site_fk;
	}

	public String getBoard_flg() {
		return board_flg;
	}

	public void setBoard_flg(String board_flg) {
		this.board_flg = board_flg;
	}

	public String getSpc_flg() {
		return spc_flg;
	}

	public void setSpc_flg(String spc_flg) {
		this.spc_flg = spc_flg;
	}

	public String getMes_flg() {
		return mes_flg;
	}

	public void setMes_flg(String mes_flg) {
		this.mes_flg = mes_flg;
	}

	public String getPms_flg() {
		return pms_flg;
	}

	public void setPms_flg(String pms_flg) {
		this.pms_flg = pms_flg;
	}

	public String getPar_tool_id() {
		return par_tool_id;
	}

	public void setPar_tool_id(String par_tool_id) {
		this.par_tool_id = par_tool_id;
	}

	public String getTool_lvl() {
		return tool_lvl;
	}

	public void setTool_lvl(String tool_lvl) {
		this.tool_lvl = tool_lvl;
	}

	public String getSum_flg() {
		return sum_flg;
	}

	public void setSum_flg(String sum_flg) {
		this.sum_flg = sum_flg;
	}
}
