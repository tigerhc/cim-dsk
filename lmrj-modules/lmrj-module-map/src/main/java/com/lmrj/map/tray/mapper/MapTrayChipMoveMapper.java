package com.lmrj.map.tray.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.lmrj.map.tray.vo.MapTrayChipMoveQueryVo;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.mapper
 * @title: map_tray_chip_move数据库控制层接口
 * @description: map_tray_chip_move数据库控制层接口
 * @author: stev
 * @date: 2020-08-02 15:31:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */
@Mapper
public interface MapTrayChipMoveMapper {

    public List<Map<String, Object>> queryChipMove(MapTrayChipMoveQueryVo query);
    
    public int countChipMove(MapTrayChipMoveQueryVo query);
    
    public List<Map<String, Object>> queryChip(String chipId);

}