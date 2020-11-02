package com.lmrj.map.tray.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.entity.MapTrayChipMove;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import com.lmrj.map.tray.service.IMapTrayChipMoveProcessService;
import com.lmrj.map.tray.util.TraceDateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapTrayChipMoveProcessImpl extends CommonServiceImpl<MapTrayChipMoveMapper,MapTrayChipMove> implements IMapTrayChipMoveProcessService {

    @Override
    public void traceDataNeedSpace() {
        List<MapTrayChipMove> traceDatas = new ArrayList<>();
        //获得要追溯的数据
        String startTimeParam = TraceDateUtil.getBeforeTime();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("startTime",startTimeParam);
        List<MapTrayChipMove> list = baseMapper.getAllTraceData(param);
        if(list!=null){
            for(int i=0; i<list.size(); i++){
                MapTrayChipMove downData = list.get(i); //下游数据
                if(!StringUtils.isEmpty(downData.getChipId())&& downData.getEqpType()!=1){
                    for(int j=i+1; j<list.size(); j++){
                        MapTrayChipMove upperData = list.get(j); //上游数据
                        if(!StringUtils.isEmpty(downData.getChipId())){
                            if(upperData.getDownEqpId().equals(downData.getEqpId()) &&
                                upperData.getStartTime().compareTo(downData.getStartTime())<=0 &&
                                upperData.getLotNo().equals(downData.getLotNo())
                            ) {
                                if(downData.getEqpType()==2){//检测类型
                                    if(downData.getToTrayId().equals(upperData.getToTrayId())&&
                                        downData.getToX()==upperData.getToX() &&
                                        downData.getToY()==upperData.getToY()
                                    ){
                                        upperData.setChipId(downData.getChipId());
                                        traceDatas.add(upperData);
                                        if(upperData.getEqpType()!=4){
                                            break;
                                        }
                                    }
                                } else if (downData.getEqpType()==4){
                                    if(
                                            (downData.getFromTrayId().equals(upperData.getToTrayId())&&
                                            downData.getFromX()==upperData.getToX() &&
                                            downData.getFromY()==upperData.getToY())
                                            ||(downData.getToTrayId().equals(upperData.getToTrayId())&&
                                                downData.getToX()==upperData.getToX() &&
                                                downData.getToY()==upperData.getToY())
                                    ){
                                        upperData.setChipId(downData.getChipId());
                                        traceDatas.add(upperData);
                                    }
                                } else {
                                    if(downData.getFromTrayId().equals(upperData.getToTrayId())&&
                                            downData.getFromX()==upperData.getToX() &&
                                            downData.getFromY()==upperData.getToY()
                                    ){
                                        upperData.setChipId(downData.getChipId());
                                        traceDatas.add(upperData);
                                        if(upperData.getEqpType()!=4){
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(traceDatas.size()>0){
//                baseMapper.emptyTraceTemp();
//                baseMapper.insertTraceTemp(traceDatas);
//                baseMapper.editTraceRs();
                for(MapTrayChipMove data : traceDatas){
                    baseMapper.updateChipIdById(data);
                }
            }
        }
    }


}
