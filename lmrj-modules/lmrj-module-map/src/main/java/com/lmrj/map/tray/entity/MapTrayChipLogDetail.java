package com.lmrj.map.tray.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

@TableName("map_tray_chip_log_detail")
@SuppressWarnings("serial")
@Data
public class MapTrayChipLogDetail extends BaseDataEntity {
    /**具体内容*/
    @TableField(value = "warn_dtl")
    private String warnDtl;
    /**具体id*/
    @TableField(value = "warn_id")
    private Long warnId;
}
