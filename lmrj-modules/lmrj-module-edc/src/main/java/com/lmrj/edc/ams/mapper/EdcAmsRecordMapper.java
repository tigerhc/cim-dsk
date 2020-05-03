package com.lmrj.edc.ams.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.edc.ams.entity.EdcAmsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.edc.ams.mapper
 * @title: edc_ams_record数据库控制层接口
 * @description: edc_ams_record数据库控制层接口
 * @author: 张伟江
 * @date: 2019-06-14 15:51:23
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface EdcAmsRecordMapper extends BaseMapper<EdcAmsRecord> {
 List<EdcAmsRecord> selectAmsRecord(@Param("officeId") String officeId, @Param("lineNo") String lineNo, @Param("department") String department, @Param("fab") String fab);
}
