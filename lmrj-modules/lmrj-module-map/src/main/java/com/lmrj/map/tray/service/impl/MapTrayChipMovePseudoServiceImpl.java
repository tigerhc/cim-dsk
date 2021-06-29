package com.lmrj.map.tray.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.entity.MapTrayChipLog;
import com.lmrj.map.tray.entity.MapTrayChipMove;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import com.lmrj.map.tray.service.IMapTrayChipLogService;
import com.lmrj.map.tray.service.IMapTrayChipMovePseudoService;
import com.lmrj.map.tray.util.TraceDateUtil;
import com.lmrj.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MapTrayChipMovePseudoServiceImpl  extends CommonServiceImpl<MapTrayChipMoveMapper, MapTrayChipMove> implements IMapTrayChipMovePseudoService {
    @Autowired
    private IMapTrayChipLogService mpTrayChipLogService;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    /**
     * 伪码追溯
     * 1.追溯本线段的数据
     * 2.追溯紧连着的上一段的数据
     * 只有1和2同时满足时才更新数据的状态为6
     * 追溯成功做了两件事:
     * (1)将本段的数据的map_flag由原来的0变为6,并填充数据的伪码
     * (2)将上一段数据的伪码更新为最新的伪码,即由上一段伪码改为本段的伪码
     */
    @Override
    public void tracePseudoData(MapEquipmentConfig eqp) {
        List<MapTrayChipMove> startDatas = baseMapper.getPseudoStart(eqp.getEqpId());//获得起始数据的列表
        List<MapEquipmentConfig> lineCfgEqp = baseMapper.getCfgEqpForLine(eqp.getEqpId());
        List<MapTrayChipLog> traceLogs = new ArrayList<>();
        if(startDatas!=null && startDatas.size()>0){
            for (MapTrayChipMove startData : startDatas) {
                List<MapTrayChipMove> lineData = baseMapper.getPseudoLine(startData);
                if(lineData==null){
                    //缺数据
                    addLog(traceLogs,"伪码追溯异常,数据没有找到,数据Id:"+startData.getId());
                    continue;
                } else {
                    String pseudoCode = eqp.getEqpId()+"_"+sdf.format(startData.getStartTime()); //TODO 伪码生成规则
                    List<MapTrayChipMove> pseudoData = new ArrayList<>();//伪码追溯成功的数据
                    Date curDate = startData.getStartTime();
                    boolean chkErr = true;
                    for(MapEquipmentConfig cfgEqp : lineCfgEqp){
                        boolean unfind = true;
                        for(MapTrayChipMove item : lineData){
                            //符合条件是时间间隔在cfgEqp中设置的间隔内(sql中以满足坐标相等)
                            long timeChk = TraceDateUtil.getDiffSec(curDate, item.getStartTime());
                            if(cfgEqp.getEqpId().equals(item.getEqpId()) && timeChk < cfgEqp.getIntervalTimeMax()){
                                unfind = false;
                                curDate = item.getStartTime();
                                item.setPseudoCode(pseudoCode);
                                item.setMapFlag(6);
                                pseudoData.add(item);
                                break;
                            }
                        }
                        if(unfind){
                            addLog(traceLogs,"伪码追溯异常,缺少数据,数据Id:"+startData.getId()+",缺少的eqpId:"+cfgEqp.getEqpId());
                            chkErr = false;
                            break;
                        }
                    }
                    if(chkErr){
                        //追溯前一段
                        if(!lineCfgEqp.get(0).getEqpType().equals("1")){
                            Map<String, Object> bfLineEndParam = new HashMap<>();
                            bfLineEndParam.put("fromTrayId", pseudoData.get(0).getFromTrayId());
                            bfLineEndParam.put("fromX", pseudoData.get(0).getFromX());
                            bfLineEndParam.put("fromY", pseudoData.get(0).getFromY());
                            bfLineEndParam.put("eqpId", pseudoData.get(0).getEqpId());
                            //前一段最后一个设备的数据
                            String beforeLineCode = traceBeforeLine(bfLineEndParam, pseudoData.get(0).getStartTime());
                            if(StringUtil.isEmpty(beforeLineCode)){
                                addLog(traceLogs,"伪码追溯异常,前一段数据没有找到,数据Id:"+startData.getId());
                                continue;
                            } else{
                                //更新上一段数据的伪码
                                baseMapper.updateBeforeTempMapFlag(beforeLineCode);
                                baseMapper.updateBeforePseudoCode(pseudoCode);
                                //更新本段的伪码
                                startData.setMapFlag(6);
                                startData.setPseudoCode(pseudoCode);
                                pseudoData.add(startData);
                                updateBatchById(pseudoData, 100);
                            }
                        }
                    }
                }
            }
        }
        saveTracLog(traceLogs);
    }

    /** 获得每段线中的结束点的设备
     * @return
     */
    @Override
    public List<MapEquipmentConfig> getLineEndEqp() {
        return baseMapper.getMapEqpConfig();
    }

    /**
     * 追溯最后一段的数据
     *
     */
    @Override
    public void traceHB2(){
        List<MapTrayChipMove> startData = baseMapper.getHB2Start();//获得起始数据的列表
        List<MapTrayChipLog> traceLogs = new ArrayList<>();
        List<MapEquipmentConfig> eqpCfg = baseMapper.getHB2EqpConfig();
        if(startData!=null && startData.size()>0){
            for (MapTrayChipMove assemblyData : startData) {//APJ-HB2-ASSEMBLY1
                Date endDate = assemblyData.getStartTime();
                boolean unErrFlag = true;
                List<MapTrayChipMove> dataLine = new ArrayList<>();
                //获得主线数据,即下基版线
                List<MapTrayChipMove> hb2LineDatas = baseMapper.getHB2Line(assemblyData);
                List<MapTrayChipMove> SMTDatas = new ArrayList<>();
                //检查数量和时间间隔
                for(MapEquipmentConfig cfg : eqpCfg){
                    int cnt = 0;
                    boolean unfindFlag = true;
                    for(MapTrayChipMove beforeDateItem : hb2LineDatas){
                        if(cfg.getEqpId().equals(beforeDateItem.getEqpId())){
                            long chkSec = TraceDateUtil.getDiffSec(endDate, beforeDateItem.getStartTime());
                            if(chkSec > cfg.getIntervalTimeMax()){
                                unErrFlag = false;
                                addLog(traceLogs,"伪码追溯异常,上下游数据时间不正确,数据Id:"+assemblyData.getId()+","+beforeDateItem.getId());
                                break;
                            }
                            dataLine.add(beforeDateItem);
                            if("4".equals(cfg.getEqpType())){//贴片
                                cnt++;
                                SMTDatas.add(beforeDateItem);
                                if(cnt == 6){
                                    unfindFlag = false;
                                    break;
                                }
                            } else {//移栽机
                                unfindFlag = false;
                                break;
                            }
                            beforeDateItem.setMapFlag(2);
                            beforeDateItem.setChipId(assemblyData.getChipId()); //TODO 此处最后关注
                        }
                    }
                    if(unfindFlag){
                        unErrFlag = false;
                        addLog(traceLogs,"伪码追溯异常,数据数量不对,数据Id:"+assemblyData.getId());
                        break;
                    }
                }
                //追溯上基板和VI
                if(unErrFlag){
                    dataLine.add(assemblyData);
                    List<String> hbsortPseudo = new ArrayList<>();//保存所有要更新的伪码,包括,上基板,下基板,1次热压SORT
                    //追溯DBC段
                    Map<String, Object> beforeLineParam = new HashMap();
                    beforeLineParam.put("eqpId", "APJ-DBCB-SORT2");//beforeLineParam 就是 DBCB
                    beforeLineParam.put("fromTrayId", dataLine.get(0).getFromEqpId());
                    beforeLineParam.put("fromX", dataLine.get(0).getFromX());
                    beforeLineParam.put("fromY", dataLine.get(0).getFromY());
                    String dbcbPseudoCode = traceBeforeLine(beforeLineParam, dataLine.get(0).getStartTime());
                    if(StringUtil.isEmpty(dbcbPseudoCode)){
                        addLog(traceLogs,"伪码追溯异常HB2段,DBCB没有找到,数据Id:"+assemblyData.getId());
                        continue;
                    } else{
                        hbsortPseudo.add(dbcbPseudoCode);//下基板  TODO 此处最后关注
                    }
                    beforeLineParam.put("eqpId", "APJ-DBCT-SORT2");//beforeLineParam 就是 DBCB
                    beforeLineParam.put("fromTrayId", assemblyData.getFromEqpId());
                    beforeLineParam.put("fromX", assemblyData.getFromX());
                    beforeLineParam.put("fromY", assemblyData.getFromY());
                    String dbctPseudoCode = traceBeforeLine(beforeLineParam, assemblyData.getStartTime());
                    if(StringUtil.isEmpty(dbctPseudoCode)){
                        addLog(traceLogs,"伪码追溯异常HB2段,DBCT没有找到,数据Id:"+assemblyData.getId());
                        continue;
                    } else{
                        hbsortPseudo.add(dbctPseudoCode);//上基板  TODO 此处最后关注
                    }

                    List<MapTrayChipMove> VIdatas = new ArrayList<>();
                    //追溯VI
                    for(MapTrayChipMove SMTData : SMTDatas){
                        beforeLineParam.put("eqpId", "APJ-VI1"); // beforeLineParam 就是 VIParam
                        beforeLineParam.put("fromTrayId", SMTData.getFromEqpId());
                        beforeLineParam.put("fromX", SMTData.getFromX());
                        beforeLineParam.put("fromY", SMTData.getFromY());
                        List<MapTrayChipMove> VIMoveDatas = baseMapper.findBeforeLineEnd(beforeLineParam);
                        if(VIMoveDatas != null && VIMoveDatas.size()>0){
                            boolean viFindFlag = true;
                            for(MapTrayChipMove viMoveData : VIMoveDatas){
                                long chkTime = TraceDateUtil.getDiffSec(SMTData.getStartTime(), viMoveData.getStartTime());
                                if(chkTime < viMoveData.getIntervalTimeMax()) {
                                    viMoveData.setMapFlag(2);
                                    viMoveData.setChipId(assemblyData.getChipId()); //TODO 此处最后关注
                                    VIdatas.add(viMoveData);
                                    viFindFlag = false;
                                    break;
                                }
                            }
                            if(viFindFlag){
                                addLog(traceLogs,"伪码追溯异常HB2段,VI没有找到,数据SMT-Id:"+SMTData.getId()+",assembly:"+assemblyData.getId());
                                unErrFlag = false;
                                break;//跳出 SMT
                            }
                        } else{
                            addLog(traceLogs,"伪码追溯异常HB2段,VI没有坐标对应,数据SMT-Id:"+SMTData.getId());
                            unErrFlag = false;
                            break;//跳出 SMT
                        }
                    }
                    if(!unErrFlag) {
                        continue;// 下一个 assembly
                    }
                    //追溯HB1-SORT
                    for(MapTrayChipMove VIdata : VIdatas){
                        beforeLineParam.put("eqpId", "APJ-HB1-SORT2");//beforeLineParam 就是 APJ-HB1-SORT2
                        beforeLineParam.put("fromTrayId", VIdata.getFromEqpId());
                        beforeLineParam.put("fromX", VIdata.getFromX());
                        beforeLineParam.put("fromY", VIdata.getFromY());
                        String viPseudoCode = traceBeforeLine(beforeLineParam, VIdata.getStartTime());
                        if(StringUtil.isEmpty(viPseudoCode)){
                            addLog(traceLogs,"伪码追溯异常HB2段,HB1-SORT2没有找到,数据VI-Id:"+VIdata.getId());
                            unErrFlag = false;
                            break;
                        } else {
                            hbsortPseudo.add(viPseudoCode); //TODO 此处最后关注
                        }
                    }
                    if(!unErrFlag) {
                        continue;// 下一个 assembly
                    }
                    //1.更新HB2的数据和VI的数据;2.更新追溯到的带有伪码的段尾数据(先1后2,原因是1耗时长)
                    updateBatchById(hb2LineDatas, 100); //SMT1\SMT2\APJ-HB2-SORT1;即下级板主线
                    updateBatchById(VIdatas, 100);//由SMT所产生的支线 VI
                    Map<String, Object> finishParam = new HashMap<>();
                    finishParam.put("chipId", assemblyData.getChipId());
                    for(String pseudo: hbsortPseudo){//上基板,下基板,1次热压SORT  一共3条伪码
                        finishParam.put("pseudoCode", pseudo);
                        baseMapper.HB2Finish(finishParam);
                    }
                }
            }
        }
        saveTracLog(traceLogs);
    }

    private String traceBeforeLine(Map<String, Object> beforeLineParam, Date date){
        List<MapTrayChipMove> beforeLines = baseMapper.findBeforeLineEnd(beforeLineParam);
        if(beforeLines != null && beforeLines.size()>0){
            for(MapTrayChipMove beforeLine : beforeLines){
                long chkTime = TraceDateUtil.getDiffSec(date, beforeLine.getStartTime());
                if(chkTime < beforeLine.getIntervalTimeMax()) {
                    return beforeLine.getPseudoCode();
                }
            }
        }
        return null;
    }

    private void addLog(List<MapTrayChipLog> list, String errMsg){
        MapTrayChipLog traceLog = new MapTrayChipLog();
        traceLog.setBeginTime(new Date());
        traceLog.setEndTime(new Date());
        traceLog.setRemarks(errMsg);
        list.add(traceLog);
    }

    @Override
    public void traceNGData(){
        List<MapTrayChipLog> traceLogs = new ArrayList<>();
        List<MapTrayChipMove> ngStartList = baseMapper.findNGStart();
        String spEqpId = "APJ-VI1,APJ-IGBT-SORT2,APJ-FRD-SORT2,APJ-HB1-SORT1,APJ-HB2-SORT1";//特殊设备,本段第一个点设备
        String firstEqpId = "APJ-IGBT-SORT1,APJ-FRD-SORT1,APJ-DBCT-SORT1,APJ-DBCB-SORT1";//第一段结尾设备,即没有上一段的设备
        if(ngStartList!=null && ngStartList.size()>0){
            //先追溯本段数据,然后追上一段伪码成功的数据,VI不需要追本段数据,
            for(MapTrayChipMove ngStart: ngStartList){//(一)组立机的不良的追溯是需要跟良品追溯一样的,追溯制品码;(二)中间段的ng不追,因为会在对应的移栽机追溯到
                if(spEqpId.contains(ngStart.getEqpId())){//段首位置上的设备
                    Map<String, Object> beforeLineParam = new HashMap<>();
                    beforeLineParam.put("eqpId", "APJ-HB1-SORT2");//beforeLineParam 就是 APJ-HB1-SORT2
                    beforeLineParam.put("fromTrayId", ngStart.getFromEqpId());
                    beforeLineParam.put("fromX", ngStart.getFromX());
                    beforeLineParam.put("fromY", ngStart.getFromY());
                    //获得上段伪码
                    String viPseudoCode = traceBeforeLine(beforeLineParam, ngStart.getStartTime());
                    //更新伪码段数据的chip_id
                    Map<String, Object> finishParam = new HashMap<>();
                    finishParam.put("chipId", ngStart.getChipId());
                    finishParam.put("pseudoCode", viPseudoCode);
                    baseMapper.HB2Finish(finishParam);
                } else if(firstEqpId.contains(ngStart.getEqpId())){//第一段数据
                    //追溯本段
                    List<MapTrayChipMove> sublineData = traceSubLine(ngStart,true);
                    if(sublineData==null){
                        addLog(traceLogs,"追溯NG异常,缺少数据,数据Id:"+ngStart.getId());
                        continue;
                    }
                    //更新本段数据
                    sublineData.add(ngStart);
                    ngStart.setMapFlag(2);
                    updateBatchById(sublineData, 100);
                } else if(ngStart.getEqpId().contains("SORT")){//中间段结尾数据
                    //追溯本段
                    List<MapTrayChipMove> sublineData = traceSubLine(ngStart,true);
                    if(sublineData==null){
                        addLog(traceLogs,"追溯NG异常,缺少数据,数据Id:"+ngStart.getId());
                        continue;
                    }
                    //获得上段伪码
                    Map<String, Object> beforeLineParam = new HashMap<>();
                    beforeLineParam.put("eqpId", "APJ-HB1-SORT2");//beforeLineParam 就是 APJ-HB1-SORT2
                    beforeLineParam.put("fromTrayId", ngStart.getFromEqpId());
                    beforeLineParam.put("fromX", ngStart.getFromX());
                    beforeLineParam.put("fromY", ngStart.getFromY());
                    String viPseudoCode = traceBeforeLine(beforeLineParam, ngStart.getStartTime());
                    if(StringUtil.isEmpty(viPseudoCode)){
                        addLog(traceLogs,"追溯NG异常,上段数据没有找到,数据Id:"+ngStart.getId());
                        continue;
                    }
                    //更新伪码段数据的chip_id
                    Map<String, Object> finishParam = new HashMap<>();
                    finishParam.put("chipId", ngStart.getChipId());
                    finishParam.put("pseudoCode", viPseudoCode);
                    baseMapper.HB2Finish(finishParam);
                    //更新本段数据
                    sublineData.add(ngStart);
                    ngStart.setMapFlag(2);
                    updateBatchById(sublineData, 100);
                }
            }
        }
        saveTracLog(traceLogs);
    }

    /** 追溯本段数据
     * startData : 本段数据尾点数据
     * ngFlag:true 是NG,false:是伪码追溯
     * 注意: VI 不会进来
     */
    private List<MapTrayChipMove> traceSubLine(MapTrayChipMove startData, boolean ngFlag){
        List<MapTrayChipMove> pseudoData = new ArrayList<>();
        List<MapEquipmentConfig> lineCfgEqp = baseMapper.getCfgEqpForLine(startData.getEqpId());
        List<MapTrayChipMove> lineData = baseMapper.getPseudoLine(startData);
        Date curDate = startData.getStartTime();
        for(MapEquipmentConfig cfgEqp : lineCfgEqp){
            boolean unfind = true;
            for(MapTrayChipMove item : lineData){
                //符合条件是时间间隔在cfgEqp中设置的间隔内(sql中以满足坐标相等)
                long timeChk = TraceDateUtil.getDiffSec(curDate, item.getStartTime());
                if(cfgEqp.getEqpId().equals(item.getEqpId()) && timeChk < cfgEqp.getIntervalTimeMax()){
                    unfind = false;
                    curDate = item.getStartTime();
                    if(ngFlag){
                        item.setChipId(startData.getChipId());
                        item.setMapFlag(2);
                    } else {
                        item.setPseudoCode(startData.getPseudoCode());
                        item.setMapFlag(6);
                    }
                    pseudoData.add(item);
                    break;
                }
            }
            if(unfind){
                return null;
            }
        }
        return pseudoData;
    }

    //保存追溯日志信息
    private void saveTracLog(List<MapTrayChipLog> traceLogs){
        if(traceLogs.size()>0){
            mpTrayChipLogService.insertBatch(traceLogs, 1000);
        }
    }
}
