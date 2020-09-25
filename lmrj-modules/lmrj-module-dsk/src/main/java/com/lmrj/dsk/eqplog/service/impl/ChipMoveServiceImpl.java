package com.lmrj.dsk.eqplog.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.dsk.eqplog.entity.ChipMove;
import com.lmrj.dsk.eqplog.mapper.ChipMoveMapper;
import com.lmrj.dsk.eqplog.service.IChipMoveService;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("mapTrayChipMoveService")
public class ChipMoveServiceImpl extends CommonServiceImpl<ChipMoveMapper, ChipMove> implements IChipMoveService {
    @Override
    public int insertData(List<Map<String, Object>> dataList) {
        List<ChipMove> mapperList = new ArrayList<ChipMove>();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Map<String, Object> item : dataList){
                ChipMove data = new ChipMove();
                data.setChipId("");//打码机在其他地方
                data.setEqpId(MapUtils.getString(item, "eqpNo"));
                data.setProductionNo(MapUtils.getString(item, "lotYield"));
                data.setLotNo(MapUtils.getString(item, "lotNo"));
                data.setFromTrayId(MapUtils.getString(item, "fromTrayId"));
                data.setFromX(MapUtils.getIntValue(item, "fromRow"));
                data.setFromY(MapUtils.getIntValue(item, "fromCol"));
                data.setToTrayId(MapUtils.getString(item, "toTrayId"));
                data.setToX(MapUtils.getIntValue(item, "toRow"));
                data.setToY(MapUtils.getIntValue(item, "toCol"));
                data.setJudgeResult(MapUtils.getString(item, "judgeResult"));
                data.setStartTime(sdf.parse(MapUtils.getString(item, "startTime")));
                mapperList.add(data);
            }
            return baseMapper.insertMoveLog(mapperList);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
