package com.lmrj.dsk.dashboard.entity;

public class FtptolalarmOA {
    private String evt_seq_id;
    private String tool_id_fk;//设备代码
    private String start_timestamp;
    private String end_timestamp;
    private String alarm_code; //警报代码
    private String line_id_fk;//线别
    private String fab_id_fk; //厂别
    private String alarm_deal_usr;//警报处理者
    private String alarm_lvl;//警报级别
    private String alarm_evt_typ;//警报类别
    private String alarm_dsc;//警报内容
    private String alarm_note;//警报备注
    private String alarm_deal_meas;//处理措施
    private String alarm_deal_result;//处理结果
    private String feat_action;//后续动作
    private String feat_act_usr;//后续处理人
    private String tool_dsc;   //设备描述
    private String fab_id_dsc;  //厂别描述
    private String line_id_dsc; //线别描述
    private String alarm_lvl_dsc; //警报级别描述
    private Integer time_consume; //报警耗时
    private String alarm_assistant; //报警协助人
    private Integer data_cnt;   //数量
    private String evt_timestamp;
    private String alarm_is_close;      //是否关闭
    private String alarm_close_time;    //关闭时间
    private String close_user;          //关闭人
    private String repair_order_fk;     //维修单号

    public String getRepair_order_fk() {
        return repair_order_fk;
    }

    public void setRepair_order_fk(String repair_order_fk) {
        this.repair_order_fk = repair_order_fk;
    }

    public String getClose_user() {
        return close_user;
    }

    public void setClose_user(String close_user) {
        this.close_user = close_user;
    }

    public String getAlarm_is_close() {
        return alarm_is_close;
    }

    public void setAlarm_is_close(String alarm_is_close) {
        this.alarm_is_close = alarm_is_close;
    }

    public String getAlarm_close_time() {
        return alarm_close_time;
    }

    public void setAlarm_close_time(String alarm_close_time) {
        this.alarm_close_time = alarm_close_time;
    }

    public String getEvt_timestamp() {
        return evt_timestamp;
    }

    public void setEvt_timestamp(String evt_timestamp) {
        this.evt_timestamp = evt_timestamp;
    }

    public Integer getData_cnt() {
        return data_cnt;
    }

    public void setData_cnt(Integer data_cnt) {
        this.data_cnt = data_cnt;
    }

    public Integer getTime_consume() {
        return time_consume;
    }

    public void setTime_consume(Integer time_consume) {
        this.time_consume = time_consume;
    }

    public String getAlarm_assistant() {
        return alarm_assistant;
    }

    public void setAlarm_assistant(String alarm_assistant) {
        this.alarm_assistant = alarm_assistant;
    }

    public String getTool_dsc() {
        return tool_dsc;
    }

    public void setTool_dsc(String tool_dsc) {
        this.tool_dsc = tool_dsc;
    }

    public String getFab_id_dsc() {
        return fab_id_dsc;
    }

    public void setFab_id_dsc(String fab_id_dsc) {
        this.fab_id_dsc = fab_id_dsc;
    }

    public String getLine_id_dsc() {
        return line_id_dsc;
    }

    public void setLine_id_dsc(String line_id_dsc) {
        this.line_id_dsc = line_id_dsc;
    }

    public String getAlarm_lvl_dsc() {
        return alarm_lvl_dsc;
    }

    public void setAlarm_lvl_dsc(String alarm_lvl_dsc) {
        this.alarm_lvl_dsc = alarm_lvl_dsc;
    }

    public String getStart_timestamp() {
        return start_timestamp;
    }

    public void setStart_timestamp(String start_timestamp) {
        this.start_timestamp = start_timestamp;
    }

    public String getEnd_timestamp() {
        return end_timestamp;
    }

    public void setEnd_timestamp(String end_timestamp) {
        this.end_timestamp = end_timestamp;
    }

    public String getEvt_seq_id() {
        return evt_seq_id;
    }

    public void setEvt_seq_id(String evt_seq_id) {
        this.evt_seq_id = evt_seq_id;
    }

    public String getFeat_action() {
        return feat_action;
    }

    public void setFeat_action(String feat_action) {
        this.feat_action = feat_action;
    }

    public String getFeat_act_usr() {
        return feat_act_usr;
    }

    public void setFeat_act_usr(String feat_act_usr) {
        this.feat_act_usr = feat_act_usr;
    }

    public String getTool_id_fk() {
        return tool_id_fk;
    }

    public void setTool_id_fk(String tool_id_fk) {
        this.tool_id_fk = tool_id_fk;
    }

    public String getAlarm_code() {
        return alarm_code;
    }

    public void setAlarm_code(String alarm_code) {
        this.alarm_code = alarm_code;
    }

    public String getLine_id_fk() {
        return line_id_fk;
    }

    public void setLine_id_fk(String line_id_fk) {
        this.line_id_fk = line_id_fk;
    }

    public String getAlarm_deal_usr() {
        return alarm_deal_usr;
    }

    public void setAlarm_deal_usr(String alarm_deal_usr) {
        this.alarm_deal_usr = alarm_deal_usr;
    }

    public String getAlarm_lvl() {
        return alarm_lvl;
    }

    public void setAlarm_lvl(String alarm_lvl) {
        this.alarm_lvl = alarm_lvl;
    }

    public String getAlarm_evt_typ() {
        return alarm_evt_typ;
    }

    public void setAlarm_evt_typ(String alarm_evt_typ) {
        this.alarm_evt_typ = alarm_evt_typ;
    }

    public String getAlarm_dsc() {
        return alarm_dsc;
    }

    public void setAlarm_dsc(String alarm_dsc) {
        this.alarm_dsc = alarm_dsc;
    }

    public String getAlarm_note() {
        return alarm_note;
    }

    public void setAlarm_note(String alarm_note) {
        this.alarm_note = alarm_note;
    }

    public String getAlarm_deal_meas() {
        return alarm_deal_meas;
    }

    public void setAlarm_deal_meas(String alarm_deal_meas) {
        this.alarm_deal_meas = alarm_deal_meas;
    }

    public String getAlarm_deal_result() {
        return alarm_deal_result;
    }

    public void setAlarm_deal_result(String alarm_deal_result) {
        this.alarm_deal_result = alarm_deal_result;
    }

    public String getFab_id_fk() {
        return fab_id_fk;
    }

    public void setFab_id_fk(String fab_id_fk) {
        this.fab_id_fk = fab_id_fk;
    }

}
