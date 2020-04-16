package com.lmrj.rms.recipefile.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lmrj.core.entity.BaseDataEntity;
import lombok.Data;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.rms.recipefile.entity
 * @title: rms_recipe_file实体
 * @description: rms_recipe_file实体
 * @author: zhangweijiang
 * @date: 2019-07-14 02:57:51
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@TableName("rms_recipe_file")
@SuppressWarnings("serial")
@Data
public class RmsRecipeFile extends BaseDataEntity {

    /**RECIPE_ID*/
    @TableField(value = "recipe_id")
    private String recipeId;
    /**附件名称*/
    @TableField(value = "file_name")
    private String fileName;
    /**文件扩展名*/
    @TableField(value = "file_extension")
    private String fileExtension;
    /**附件存放路径*/
    @TableField(value = "file_path")
    private String filePath;
    /**文件大小*/
    @TableField(value = "file_size")
    private Double fileSize;
    /**附件顺序号*/
    @TableField(value = "sort_no")
    private Double sortNo;

}
