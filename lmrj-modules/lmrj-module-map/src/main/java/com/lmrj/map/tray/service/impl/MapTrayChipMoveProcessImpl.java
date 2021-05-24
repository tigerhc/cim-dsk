package com.lmrj.map.tray.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.entity.MapTrayChipLog;
import com.lmrj.map.tray.entity.MapTrayChipLogDetail;
import com.lmrj.map.tray.entity.MapTrayChipMove;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import com.lmrj.map.tray.service.IMapTrayChipLogDetailService;
import com.lmrj.map.tray.service.IMapTrayChipLogService;
import com.lmrj.map.tray.service.IMapTrayChipMoveProcessService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MapTrayChipMoveProcessImpl extends CommonServiceImpl<MapTrayChipMoveMapper,MapTrayChipMove> implements IMapTrayChipMoveProcessService {
    @Autowired
    private IMapTrayChipLogService mpTrayChipLogService;
    @Autowired
    private IMapTrayChipLogDetailService mapTrayChipLogDetailService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//    @Override
//    public void traceDataNeedSpace() {
//        List<MapTrayChipMove> traceDatas = new ArrayList<>();
//        //获得要追溯的数据
//        String startTimeParam = TraceDateUtil.getBeforeTime();
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("startTime",startTimeParam);
//        List<MapTrayChipMove> list = baseMapper.getAllTraceData(param);
//        if(list!=null){
//            for(int i=0; i<list.size(); i++){
//                MapTrayChipMove downData = list.get(i); //下游数据
//                if(!StringUtils.isEmpty(downData.getChipId())&& downData.getEqpType()!=1){
//                    for(int j=i+1; j<list.size(); j++){
//                        MapTrayChipMove upperData = list.get(j); //上游数据
//                        if(!StringUtils.isEmpty(downData.getChipId())){
//                            if(upperData.getDownEqpId().equals(downData.getEqpId()) &&
//                                upperData.getStartTime().compareTo(downData.getStartTime())<=0 &&
//                                upperData.getLotNo().equals(downData.getLotNo())
//                            ) {
//                                if(downData.getEqpType()==2){//检测类型
//                                    if(downData.getToTrayId().equals(upperData.getToTrayId())&&
//                                        downData.getToX()==upperData.getToX() &&
//                                        downData.getToY()==upperData.getToY()
//                                    ){
//                                        upperData.setChipId(downData.getChipId());
//                                        traceDatas.add(upperData);
//                                        if(upperData.getEqpType()!=4){
//                                            break;
//                                        }
//                                    }
//                                } else if (downData.getEqpType()==4){
//                                    if(
//                                            (downData.getFromTrayId().equals(upperData.getToTrayId())&&
//                                            downData.getFromX()==upperData.getToX() &&
//                                            downData.getFromY()==upperData.getToY())
//                                            ||(downData.getToTrayId().equals(upperData.getToTrayId())&&
//                                                downData.getToX()==upperData.getToX() &&
//                                                downData.getToY()==upperData.getToY())
//                                    ){
//                                        upperData.setChipId(downData.getChipId());
//                                        traceDatas.add(upperData);
//                                    }
//                                } else {
//                                    if(downData.getFromTrayId().equals(upperData.getToTrayId())&&
//                                            downData.getFromX()==upperData.getToX() &&
//                                            downData.getFromY()==upperData.getToY()
//                                    ){
//                                        upperData.setChipId(downData.getChipId());
//                                        traceDatas.add(upperData);
//                                        if(upperData.getEqpType()!=4){
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if(traceDatas.size()>0){
////                baseMapper.emptyTraceTemp();
////                baseMapper.insertTraceTemp(traceDatas);
////                baseMapper.editTraceRs();
//                for(MapTrayChipMove data : traceDatas){
//                    baseMapper.updateChipIdById(data);
//                }
//            }
//        }
//    }

//    @Override
//    public void traceDataNeedSpace2() {
//        int traceDatas = baseMapper.cntTraceData();
//        if(traceDatas>0){
//            List<Map<String, Object>> allEqp = baseMapper.getConfig();
//            if(allEqp!=null && allEqp.size()>0){
//                List<MapTrayChipMove> traceData = new ArrayList<>();
//                for(Map<String, Object> eqp : allEqp){
//                    List<MapTrayChipMove> downData = baseMapper.getTraceDataByDown(MapUtils.getString(eqp, "eqpId"));
//                    List<MapTrayChipMove> upperData = baseMapper.getTraceDataByUpper(MapUtils.getString(eqp, "eqpId"));
//                    if(downData.size()>0 && upperData.size()>0){//防止抛异常
//                        for(MapTrayChipMove downItem : downData){
//                            if(!StringUtils.isEmpty(downItem.getChipId()) && MapUtils.getIntValue(eqp,"eqpType")!=1){
////                                int contType4 = 0;
//                                for(MapTrayChipMove upperItem : upperData){
//                                    if(StringUtils.isEmpty(upperItem.getChipId())){
//                                        System.out.println("---------------ge------------------");
//                                        JSONObject upjson = JSONObject.fromObject(upperItem);
//                                        JSONObject downjson = JSONObject.fromObject(downItem);
//                                        System.out.println(upjson.toString() + "||"+downjson.toString());
//                                        if(upperItem.getLotNo().equals(downItem.getLotNo()) &&
//                                            upperItem.getStartTime().compareTo(downItem.getStartTime())<=0
//                                        ){
//                                            if(MapUtils.getIntValue(eqp,"eqpType")==0){
//                                                if(upperItem.getToTrayId().equals(downItem.getFromTrayId())&&
//                                                    upperItem.getToX()==downItem.getFromX()&&
//                                                    upperItem.getToY()==downItem.getFromY()
//                                                ){
//                                                    upperItem.setChipId(downItem.getChipId());
//                                                    traceData.add(upperItem);
//                                                    break;
//                                                }
//                                            }else if(MapUtils.getIntValue(eqp,"eqpType")==4 ){
//                                                if((upperItem.getToTrayId().equals(downItem.getFromTrayId())&&
//                                                        upperItem.getToX()==downItem.getFromX()&&
//                                                        upperItem.getToY()==downItem.getFromY())||
//                                                        ( upperItem.getToTrayId().equals(downItem.getToTrayId())&&
//                                                                upperItem.getToX()==downItem.getToX()&&
//                                                                upperItem.getToY()==downItem.getToY()
//                                                        )
//                                                ){
//                                                    upperItem.setChipId(downItem.getChipId());
//                                                    traceData.add(upperItem);
//                                                }
//                                            }else if(MapUtils.getIntValue(eqp,"eqpType")==8){
//                                                if((upperItem.getToTrayId().equals(downItem.getFromTrayId())&&
//                                                    upperItem.getToX()==downItem.getFromX()&&
//                                                    upperItem.getToY()==downItem.getFromY())||
//                                                    ( upperItem.getToTrayId().equals(downItem.getToTrayId())&&
//                                                        upperItem.getToX()==downItem.getToX()&&
//                                                        upperItem.getToY()==downItem.getToY()
//                                                    )
//                                                ){
//                                                    upperItem.setChipId(downItem.getChipId());
//                                                    traceData.add(upperItem);
////                                                if(contType4 ==1){
////                                                    updateDataCope.add(upperItem);
////                                                    break;
////                                                }
////                                                contType4++;
//                                                }
//                                            } else {
//                                                if(upperItem.getToTrayId().equals(downItem.getToTrayId())&&
//                                                    upperItem.getToX()==downItem.getToX()&&
//                                                    upperItem.getToY()==downItem.getToY()
//                                                ){
//                                                    upperItem.setChipId(downItem.getChipId());
//                                                    traceData.add(upperItem);
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        if(traceData.size()>0){
//                            insertOrUpdateBatch(traceData, 10000);
//                            traceData.clear();
//                        }
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    public void traceData() {
//        //获得所有打码机上报的记录作为起点
//        List<MapTrayChipMove> startData = baseMapper.getStartData();
//        if(startData!=null && startData.size()>0){
//            //存放追溯成功的记录
//            List<MapTrayChipMove> waitTrace = new ArrayList();
//            //每个芯片的完整记录，换芯片时会放到waitTrace中，然后清空
//            List<MapTrayChipMove> buff = new ArrayList();
//            //存放追溯不成功的记录
//            List<MapTrayChipMove> error = new ArrayList<>();
//            //换芯片的标识
//            String curChipId = "";
//            while(startData.size()>0){
//                MapTrayChipMove item = startData.get(0);
//                //换芯片编号时，将完整的追溯保存到成功的集合中
//                if(!curChipId.equals(item.getChipId())&& !"".equals(curChipId)){
//                    curChipId = item.getChipId();
//                    clearBuff(buff, waitTrace, false);//如果追溯的记录数量过多，先更新
//                    //保存打码机上报的
//                    item.setMapFlag(1);
//                    buff.add(item);
//                }else if("".equals(curChipId)){
//                    item.setMapFlag(1);
//                    buff.add(item);
//                }
//
//                if(item.getEqpType()==4){//贴合
//                    Map<String, Object> chkParam = new HashMap<>();
//                    chkParam.put("lotNo", item.getLotNo());
//                    chkParam.put("toTrayId", item.getToTrayId());
//                    chkParam.put("toX", item.getToX());
//                    chkParam.put("toY", item.getToY());
//                    chkParam.put("eqpId", item.getEqpId());
//                    chkParam.put("startTime",TraceDateUtil.getChkTime(item.getStartTime(), -2));
//                    chkParam.put("endTime", TraceDateUtil.getChkTime(item.getStartTime(), 2));
//                    Integer chkCnt = baseMapper.getChkAttach(chkParam);
//                    Integer chk = baseMapper.chkRecordCnt(item);//null 配置有误
//                    if(chk== null || chkCnt== null || chkCnt!=chk){
//                        for(MapTrayChipMove errorItem : buff){
//                            if(item.getChipId().equals(errorItem.getChipId())){
//                                errorItem.setMapFlag(2);
//                                error.add(errorItem);
//                                startData.remove(errorItem);
//                            }
//                        }
//                        buff.clear();
//                        break;
//                    }
//                }
//                List<MapTrayChipMove> upperData = baseMapper.getUpperData(item);
//                if(upperData!=null && upperData.size()>0){
//                    for(MapTrayChipMove upperItem : upperData){
//                        upperItem.setChipId(item.getChipId());
//                        startData.add(0,upperItem);
//                        upperItem.setMapFlag(1);
//                        buff.add(upperItem);
//                    }
//                }else if(item.getEqpType()!=1){//没有找到记录，并且非开始设备
//                    for(MapTrayChipMove errorItem : startData){
//                        if(item.getChipId().equals(errorItem.getChipId())){
//                            errorItem.setMapFlag(2);
//                            error.add(errorItem);
//                            startData.remove(errorItem);
//                        }
//                    }
//                }
//                startData.remove(item);
//                //最后一个芯片追溯到成功的集合中
//                if(startData.size()<=0){
//                    clearBuff(buff, waitTrace, true);
//                }
//            }
//            //更新追溯不成功的
//            if(error.size()>0){
//                clearBuff(error, waitTrace, true);
//            }
//        }
//    }
//
//    public void clearBuff(List<MapTrayChipMove> buff, List<MapTrayChipMove> waitTrace, boolean submitFlag){
//        if(buff!=null && buff.size()>0){
//            for(MapTrayChipMove buffItem : buff){
//                waitTrace.add(buffItem);
//            }
//            buff.clear();
//            if(waitTrace.size()>2000 || submitFlag){//如果追溯的记录数量过多，先更新
//                updateBatchById(waitTrace);
//                waitTrace.clear();
//            }
//        }
//    }

//    @Override
//    public void traceDataWhile() {
//        //获得所有打码机上报的记录作为起点
//        List<MapTrayChipMove> startData = baseMapper.getStartData();
//        if(startData!=null && startData.size()>0){
//            //每个芯片的完整记录，换芯片时会放到waitTrace中，然后清空
//            List<MapTrayChipMove> buff = new ArrayList();
//            //换芯片的标识
//            String curChipId = "";
//            while(startData.size()>0){
//                MapTrayChipMove item = startData.get(0);
//                //换芯片编号时，将完整的追溯保存到成功的集合中
//                if(!curChipId.equals(item.getChipId())&& !"".equals(curChipId)){
//                    curChipId = item.getChipId();
//                    clearBuff(buff, 1);//如果追溯的记录数量过多，先更新
//                    //每个芯片的打码机上报的
//                    buff.add(item);
//                }else if("".equals(curChipId)){
//                    buff.add(item);
//                }
//                if(item.getEqpType()==4){//贴合
//                    Map<String, Object> chkParam = new HashMap<>();
//                    chkParam.put("lotNo", item.getLotNo());
//                    chkParam.put("toTrayId", item.getToTrayId());
//                    chkParam.put("toX", item.getToX());
//                    chkParam.put("toY", item.getToY());
//                    chkParam.put("eqpId", item.getEqpId());
//                    chkParam.put("startTime",TraceDateUtil.getChkTime(item.getStartTime(), -2));
//                    chkParam.put("endTime", TraceDateUtil.getChkTime(item.getStartTime(), 2));
//                    Integer chkCnt = baseMapper.getChkAttach(chkParam);
//                    Integer chk = baseMapper.chkRecordCnt(item);//null 配置有误
//                    if(chk== null || chkCnt== null || chkCnt!=chk){
//                        clearBuff(buff, 2);
//                        for(MapTrayChipMove errorItem : buff){
//                            startData.remove(errorItem);
//                        }
//                        break;
//                    }
//                }
//                List<MapTrayChipMove> upperData = baseMapper.getUpperData(item);
//                if(upperData!=null && upperData.size()>0){
//                    for(MapTrayChipMove upperItem : upperData){
//                        upperItem.setChipId(item.getChipId());
//                        startData.add(0,upperItem);
//                        buff.add(upperItem);
//                    }
//                }else if(item.getEqpType()!=1){//没有找到记录，并且非开始设备
//                    clearBuff(buff, 2);
//                    for(MapTrayChipMove errorItem : buff){
//                        startData.remove(errorItem);
//                    }
//                }
//                startData.remove(item);
//            }
//            if(buff.size()>0){
//                clearBuff(buff, 1);
//            }
//        }
//    }

    /**追溯
     * @param traceLog ：作为参数的原因是为了避免被事务控制而影响 chkRunning 的判断
     * @param processFlag ： 是正常追溯，还是追溯异常的数据
     */
    @Override
    public void traceData(MapTrayChipLog traceLog, String processFlag) {
//        baseMapper.emptyTemp();
        //获得所有打码机上报的记录作为起点
//        List<Map<String, Object>> tieHe = baseMapper.chkRecordCnt();
        List<MapTrayChipMove> startData;

        //处理日志表
        String startTime = baseMapper.getLastStartTime(processFlag);
        if(StringUtil.isEmpty(startTime)){
            traceLog.setBeginTime(new Date());
            mpTrayChipLogService.insert(traceLog);
        }

        //根据processFlag取对应的开头数据
        if(processErrDataFlag.equals(processFlag)){
            startData = baseMapper.getStartErrorData(startTime);
            traceLog.setRemarks("追溯异常的数据"+startTime);
        }else if(processAsynchronous.equals(processFlag)){
            startData = baseMapper.getStartData(startTime);
            traceLog.setRemarks("追溯正常数据,startTime:"+startTime);
        }else if(processNgDataFlag.equals(processFlag)){
            startData = baseMapper.getNGStart(startTime);
            traceLog.setRemarks("追溯NG数据,startTime:"+startTime);
        }else{
            startData = baseMapper.getStartData("");
            traceLog.setRemarks("追溯正常的数据");
        }
        traceLog.setProcTotal(Long.valueOf(startData.size()));
//        List<MapTrayChipLogDetail> errDetailList = new ArrayList<>();
        long error = 0;
        long suc = 0;
        String errorLotNo = "";
        if(startData.size()>0){
            //每个芯片的完整记录，换芯片时会放到waitTrace中，然后清空
            List<MapTrayChipMove> buff = new ArrayList<>();
            for(MapTrayChipMove startItem : startData){
                List<MapTrayChipMove> traceList = new ArrayList<>();
                traceList.add(startItem);
                buff.add(startItem);
                if(!errorLotNo.equals(startItem.getLotNo())){
                    while(traceList.size()>0){
                        MapTrayChipMove item = traceList.get(0);
//                        if(item.getEqpType()==4){
//                            item.setLmtTime(TraceDateUtil.getChkTime(item.getStartTime(), -5));//贴合数据的前后不超过5分钟
//                        }
                        List<MapTrayChipMove> upperData = baseMapper.getUpperData(item);
//                        if(item.getEqpType()==4){
//                            int recordCnt = 0;
//                            for(Map<String, Object> thItem : tieHe){
//                                if(MapUtils.getString(thItem, "lotNo").equals(item.getLotNo())
//                                    && MapUtils.getString(thItem, "eqpId").equals(item.getEqpId())){
//                                    recordCnt = MapUtils.getIntValue(thItem, "recordCnt");
//                                    break;
//                                }
//                            }
//                            //检查配置是否不正确
//                            if(tieHe.size()<1 || recordCnt==0){
//                                buff.clear();
//                                traceList.clear();
//                                MapTrayChipLogDetail errDetail = new MapTrayChipLogDetail();
//                                errDetail.setWarnDtl("贴合的配置没有找到.eqpId:"+item.getEqpId()+",lotNo:"+item.getLotNo());
//                                errDetail.setWarnId(item.getId());
//                                errDetail.setCreateDate(new Date());
//                                errDetailList.add(errDetail);
//                                errorLotNo = item.getLotNo();
//                                break;
//                            }else if(upperData.size()!=recordCnt){
//                                clearBuff(buff, 2);
//                                error++;
//                                MapTrayChipLogDetail errDetail = new MapTrayChipLogDetail();
//                                errDetail.setWarnDtl("贴合的数量不正确，正确的数量："+recordCnt+",实际数量"+upperData.size()+",复查json："+ JsonUtil.toJsonString(item));
//                                errDetail.setWarnId(item.getId());
//                                errDetail.setCreateDate(new Date());
//                                errDetailList.add(errDetail);
//                                break;
//                            }
//                        }
                        if(upperData!=null && upperData.size()>0){
                            for(MapTrayChipMove upperItem : upperData){
                                if(StringUtils.isEmpty(upperItem.getChipId())){
                                    upperItem.setChipId(item.getChipId());
                                }
                                traceList.add(0,upperItem);
                                buff.add(upperItem);
                            }
                        }else if(item.getEqpType()!=1){//没有找到记录，并且非开始设备
                            clearBuff(buff, 2);
                            error++;
//                            MapTrayChipLogDetail errDetail = new MapTrayChipLogDetail();
//                            errDetail.setWarnDtl("缺少上游数据，当前的数据是:"+JsonUtil.toJsonString(item));
//                            errDetail.setWarnId(item.getId());
//                            errDetail.setCreateDate(new Date());
//                            errDetailList.add(errDetail);
                            for(MapTrayChipMove errorItem : buff){
                                traceList.remove(errorItem);
                            }
                        }
                        traceList.remove(item);
                    }
                }
                if(buff.size()>0){
                    suc++;
                    clearBuff(buff, 1);
                }
            }
        }
        traceLog.setProcWarn(error);
        traceLog.setProcSuc(suc);
        traceLog.setEndTime(new Date());
        //追溯补全log中只有开始时间没有结束时间的记录，以此标志着此次追溯完毕
        mpTrayChipLogService.updateById(traceLog);
//        if(errDetailList.size()>0){
//            mapTrayChipLogDetailService.insertBatch(errDetailList, 500);
//        }
//        baseMapper.updateChipIds();
    }

    @Override
    public List<Map<String, Object>> dmDetail(String id) {
        if(StringUtils.isEmpty(id)){
            return null;
        }
        MapTrayChipMove chipObj = baseMapper.selectById(Integer.parseInt(id));
        if(chipObj!=null && StringUtil.isNotEmpty(chipObj.getChipId())){
            List<Map<String, Object>> list = baseMapper.dmDetail(chipObj.getChipId());
            for(Map<String, Object> item : list){
                String[] xs = MapUtils.getString(item, "dmX").split(",");
                String[] ys = MapUtils.getString(item, "dmY").split(",");

                String xy = xs[0]+"_"+ys[0];
                List<Map<String, Integer>> dmXY = new ArrayList<>();
                for(int i=0; i<xs.length; i++){
                    xy = xy + ","+ xs[i]+"_"+ys[i];
                    Map<String, Integer> xyMap = new HashMap<>();
                    xyMap.put("dmX", Integer.parseInt(xs[i]));
                    xyMap.put("dmY", Integer.parseInt(ys[i]));
                    dmXY.add(xyMap);
                    if(i==0){
                        xy = xs[i]+"_"+ys[i];
                    }else{
                        xy = xy + ","+ xs[i]+"_"+ys[i];
                    }
                }
                item.put("lightPst", dmXY);//[{"dmX":1,"dmY":2},{"dmX":1,"dmY":2}]
            }
            return list;
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> listItem = new HashMap();

            //晶圆坐标信息
            List<Map<String, Object>> positionList = new ArrayList<>();
            Map<String, Object> positionItem = new HashMap();
            positionItem.put("dmX", chipObj.getDmX());
            positionItem.put("dmY", chipObj.getDmY());
            positionList.add(positionItem);
            listItem.put("lightPst", positionList);
            //其他信息
            listItem.put("eqpId", chipObj.getEqpId());
            listItem.put("dmId", chipObj.getDmId());
            listItem.put("startTime", DateUtil.formatDate(chipObj.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            list.add(listItem);
            return list;
        }
    }

    public void clearBuff(List<MapTrayChipMove> buff, int mapFlag){
        if(buff!=null && buff.size()>0){
            if(mapFlag>1){
                MapTrayChipMove module = null;
                for(MapTrayChipMove item : buff){
                    if(item.getEqpType()==8){
                        module=item;
                    }
                }
                if(module == null){//NG
                    for(MapTrayChipMove item : buff){
                        item.setMapFlag(mapFlag);
                    }
                    updateBatchById(buff,500);
                } else {//良品追溯
                    mapFlag = module.getMapFlag();
                    module.setMapFlag(++mapFlag);
                    updateById(module);
                }
            }else{
                for(MapTrayChipMove item : buff){
                    item.setMapFlag(mapFlag);
                }
                updateBatchById(buff,500);
//                baseMapper.updateChipIdBatch(buff);
            }
            buff.clear();
        }
    }

    @Override
    public Map<String, Object> getProductionParam(long id){
        Map<String, Object> rs = new HashMap<>();
        MapTrayChipMove data = baseMapper.selectById(id);
        if(data == null){
            return null;
        }
        if("APJ-HTRT1".equals(data.getEqpId())){//高温室温检查

        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("eqpId", data.getEqpId());
            String startTime = sdf.format(data.getStartTime());
            startTime = startTime.length()>23? startTime.substring(0,23):startTime;//2021-04-28 09:10:46.248
            param.put("startTime",startTime);
            if("N".equals(data.getJudgeResult())){//不良品
                rs.put("paramValue", baseMapper.findNGProParam(param));
            } else {
                rs.put("paramValue", baseMapper.findProParam(param));
            }
            rs.put("title", baseMapper.findParamTitle(data.getEqpModelName()));
        }
        return rs;
    }
}
