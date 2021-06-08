package com.lmrj.fab.eqp.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @author wdj
 * @date 2021-06-02 14:21
 */
@SuppressWarnings("serial")
@Data
public class TreeModel  extends BaseDataEntity {
    @TableField(exist = false)
    private String treeNode;
    @TableField(exist = false)
    private String treeLabel;
    @TableField(exist = false)
    private String treeValue;
    @TableField(exist = false)
    List<TreeModel> treeModelList;
    @TableField(exist = false)
    private String num;
}
