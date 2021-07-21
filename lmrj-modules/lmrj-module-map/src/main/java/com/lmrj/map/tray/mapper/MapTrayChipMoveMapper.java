package com.lmrj.map.tray.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.entity.MapTrayChipMove;
import com.lmrj.map.tray.vo.MapTrayChipMoveQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
public interface MapTrayChipMoveMapper extends BaseMapper<MapTrayChipMove> {
    public List<Map<String, Object>> queryChipMove(MapTrayChipMoveQueryVo query);

    public int countChipMove(MapTrayChipMoveQueryVo query);

    public List<Map<String, Object>> queryChip(String chipId);

    List<Map<String, Object>> dmDetail(String chipId);

    List<MapTrayChipMove> getAllTraceData(Map<String,Object> param);

    void editTraceRs();

    void emptyTraceTemp();

    void insertTraceTemp(@Param("data") List<MapTrayChipMove> data);

    void updateChipIdById(MapTrayChipMove data);

    List<MapTrayChipMove> getTraceDataByUpper(String eqpId);

    List<MapTrayChipMove> getTraceDataByDown(String eqpId);

    int cntTraceData();

    List<Map<String, Object>> getConfig();

    //可以异步的获得追溯打码机数据
    List<MapTrayChipMove> getStartData(@Param("startTime") String startTime);

    List<MapTrayChipMove> getStartErrorData(@Param("startTime") String startTime);

    List<MapTrayChipMove> getUpperData(MapTrayChipMove param);

    List<Map<String, Object>> chkRecordCnt();

//    Integer getChkAttach(Map<String, Object> param);

    void updateChipIdBatch(@Param("ids") List<MapTrayChipMove> ids);

    void emptyTemp();

    void updateChipIds();

    Integer chkProcessRunning(@Param("beginTime") String beginTime);

    //获得日志中最后一次开始时间
    String getLastStartTime(@Param("trayCode") String trayCode);

    List<MapTrayChipMove> getNGStart(@Param("startTime") String startTime);

    String findNGProParam(Map<String, Object> param);

    String findProParam(Map<String, Object> param);

    List<Map<String, Object>> findParamTitle(String eqpModelName);

    //凌欧设备的生产条件
    String findLOProParam(Map<String, Object> param);

    //伪码追溯相关
    List<MapTrayChipMove> getPseudoStart(String eqpId);

    List<MapTrayChipMove> getPseudoLine(MapTrayChipMove param);

    List<MapEquipmentConfig> getMapEqpConfig();

    List<MapTrayChipMove> findBeforeLineEnd(MapTrayChipMove param);
    List<MapTrayChipMove> findVI(MapTrayChipMove param);

    /**更新上一段的伪码数据的状态为8*/
    List<Map<String, Object>> queryByPseudoCode(String pseudoCode);
    void updateTempPseudoCode(Map<String, Object> param);//上一段的PseudoCode
    /**更新状态为8的数据的伪码是PseudoCode*/
    void updateBeforePseudoCode(String PseudoCode);//最新的PseudoCode

    List<MapTrayChipMove> getHB2Start();
    List<MapTrayChipMove> getBeforeData(MapTrayChipMove param);

    List<MapEquipmentConfig> getHB2EqpConfig();

    List<MapTrayChipMove> getHB2Line(MapTrayChipMove param);

    void HB2Finish(Map<String, Object> param);

    List<MapEquipmentConfig> getCfgEqpForLine(String eqpId);

    List<MapTrayChipMove> findNGStart();
}
