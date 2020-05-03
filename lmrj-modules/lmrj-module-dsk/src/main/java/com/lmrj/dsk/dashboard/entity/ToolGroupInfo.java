package com.lmrj.dsk.dashboard.entity;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
@Data
public class ToolGroupInfo {

    String rtn_code;
    String rtn_mesg;
    String tbl_cnt;
    String telephone;
    String trx_id;
    String type_id;
    public List<ToolGroupDetail> oary = Lists.newArrayList();
}
