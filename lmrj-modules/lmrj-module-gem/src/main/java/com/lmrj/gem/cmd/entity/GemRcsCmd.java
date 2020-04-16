package com.lmrj.gem.cmd.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zwj
 * @since 2019-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gem_rcs_cmd")
public class GemRcsCmd extends BaseDataEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 设备号
     */
    @TableField("EQP_ID")
    private String eqpId;

    /**
     * 远程命令代码
     */
    @TableField("RCMD")
    private String rcmd;

    /**
     * 命令参数1 code
     */
    @TableField("CP_NAME1")
    private String cpName1;

    /**
     * 命令参数1 value
     */
    @TableField("CP_VALUE1")
    private String cpValue1;

    /**
     * 命令参数2 code
     */
    @TableField("CP_NAME2")
    private String cpName2;

    /**
     * 命令参数2 value
     */
    @TableField("CP_VALUE2")
    private String cpValue2;

    /**
     * 命令参数3 code
     */
    @TableField("CP_NAME3")
    private String cpName3;

    /**
     * 命令参数3 value
     */
    @TableField("CP_VALUE3")
    private String cpValue3;

    /**
     * 命令参数4 code
     */
    @TableField("CP_NAME4")
    private String cpName4;

    /**
     * 命令参数4 value
     */
    @TableField("CP_VALUE4")
    private String cpValue4;

    /**
     * 命令参数5 code
     */
    @TableField("CP_NAME5")
    private String cpName5;

    /**
     * 命令参数5 value
     */
    @TableField("CP_VALUE5")
    private String cpValue5;

    /**
     * 命令参数6 code
     */
    @TableField("CP_NAME6")
    private String cpName6;

    /**
     * 命令参数6 value
     */
    @TableField("CP_VALUE6")
    private String cpValue6;

    /**
     * 执行结果标记 0: OK, 1: WARN, 2: ERROR
     */
    @TableField("RESULT_FLAG")
    private String resultFlag;

    /**
     * 执行结果
     */
    @TableField("RESULT_DESC")
    private String resultDesc;

    /**
     * 执行结果
     */
    @TableField("CP_ACK1")
    private String cpAck1;
    @TableField("CP_ACK2")
    private String cpAck2;
    @TableField("CP_ACK3")
    private String cpAck3;
    @TableField("CP_ACK4")
    private String cpAck4;
    @TableField("CP_ACK5")
    private String cpAck5;
    @TableField("CP_ACK6")
    private String cpAck6;



}
