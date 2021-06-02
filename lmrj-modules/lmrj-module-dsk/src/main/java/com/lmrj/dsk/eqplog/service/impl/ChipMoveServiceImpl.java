package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.constant.EqpNameConstant;
import com.lmrj.dsk.eqplog.entity.ChipMove;
import com.lmrj.dsk.eqplog.mapper.ChipMoveMapper;
import com.lmrj.dsk.eqplog.service.IChipMoveService;
import com.lmrj.util.collection.MapUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("mapTrayChipMoveService")
@Slf4j
public class ChipMoveServiceImpl extends CommonServiceImpl<ChipMoveMapper, ChipMove> implements IChipMoveService {
    private SimpleDateFormat sdfSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private SimpleDateFormat sdfS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    @Override
    public int insertData(List<Map<String, Object>> dataList) {
        List<ChipMove> mapperList = new ArrayList<>();
        try{
            for(Map<String, Object> item : dataList){
                ChipMove data = new ChipMove();
                String chipId = MapUtils.getString(item, "chipId");
                if(StringUtil.isEmpty(chipId)||"null".equals(chipId)||"NULL".equals(chipId)){
                    chipId = null;
                }
                data.setEqpId(MapUtils.getString(item, "eqpId"));
                data.setEqpModelName(MapUtils.getString(item, "eqpName"));
                data.setProductionNo(MapUtils.getString(item, "lotYield"));
                data.setLotNo(MapUtils.getString(item, "lotNo"));
                data.setFromTrayId(MapUtils.getString(item, "fromTrayId"));
                String fromRow = MapUtils.getString(item, "fromRow");
                if(StringUtil.isEmpty(fromRow)){
                    data.setFromX(null);
                }else{
                    data.setFromX(Integer.parseInt(fromRow));
                }
                String fromCol = MapUtils.getString(item, "fromCol");
                if(StringUtil.isEmpty(fromCol)){
                    data.setFromY(null);
                }else{
                    data.setFromY(Integer.parseInt(fromCol));
                }
                data.setToTrayId(MapUtils.getString(item, "toTrayId"));
                if(EqpNameConstant.EQP_LAST_SORT.equals(data.getEqpId())){
                    data.setToX(1);
                    data.setToY(1);
                }else{
                    data.setToX(MapUtils.getIntValue(item, "toRow"));
                    data.setToY(MapUtils.getIntValue(item, "toCol"));
                }
                if(MapUtils.getString(item, "eqpId").contains("-DM")){//DM 坐标
                    data.setDmId(MapUtils.getString(item, "dmId"));
                    data.setDmX(MapUtils.getInteger(item, "dmX"));
                    data.setDmY(MapUtils.getInteger(item, "dmY"));
                }
                data.setJudgeResult(MapUtils.getString(item, "judgeResult"));
                String dataStartTime = MapUtils.getString(item, "startTime");
                if(dataStartTime.length() == 23){
                    data.setStartTime(sdfSSS.parse(dataStartTime));
                } else {
                    data.setStartTime(sdfS.parse(dataStartTime));
                }

                data.setChipId(chipId);
                data.setFileName(MapUtils.getString(item, "fileName"));
                data.setMapFlag(0);
                mapperList.add(data);
            }
            if(mapperList.size()>0){
                return baseMapper.insertMoveLog(mapperList);
            }
        } catch (Exception e){
            log.error("保存追溯的前段数据有误", e);
        }
        return 0;
    }

    @Override
    public int insertChipIdData(List<Map<String, Object>> dataList) {
        List<ChipMove> moveList = new ArrayList<>();
        try {
            for(Map<String, Object> item : dataList){
                String eqpId = MapUtils.getString(item, "eqpId");
                if(EqpNameConstant.EQP_CLEAN_US.equals(eqpId)||EqpNameConstant.EQP_JET.equals(eqpId)) {
                    List<String> xray = baseMapper.findXrayData(MapUtils.getString(item, "toTrayId"));
                    for (String chipId : xray) {
                        ChipMove data = new ChipMove();
                        data.setEqpId(eqpId);
                        data.setProductionNo(MapUtils.getString(item, "productionNo"));
                        data.setLotNo(MapUtils.getString(item, "lotNo"));
                        data.setToTrayId(MapUtils.getString(item, "toTrayId"));
                        data.setToX(1);
                        data.setToY(1);
                        data.setJudgeResult(MapUtils.getString(item, "judgeResult"));
                        String dataStartTime = MapUtils.getString(item, "startTime");
                        if(dataStartTime.length() == 23){
                            data.setStartTime(sdfSSS.parse(dataStartTime));
                        } else {
                            data.setStartTime(sdfS.parse(dataStartTime));
                        }
                        data.setFileName(MapUtils.getString(item, "fileName"));
                        data.setChipId(chipId);
                        data.setMapFlag(1);
                        moveList.add(data);
                    }
                    if (EqpNameConstant.EQP_JET.equals(eqpId)) {
                        //更新X射线的数据的追溯状态为追溯成功 1
                        baseMapper.finishXrayData(MapUtils.getString(item, "toTrayId"));
                    }
                }
            }
            if(moveList.size()>0){
                baseMapper.insertMoveLog(moveList);
            }
        } catch (Exception e){
            log.error("保存追溯的后半段数据有误", e);
        }
        return 0;
    }
}
