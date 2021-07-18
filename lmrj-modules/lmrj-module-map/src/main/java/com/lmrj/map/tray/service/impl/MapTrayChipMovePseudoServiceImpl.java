package com.lmrj.map.tray.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.map.tray.entity.MapEquipmentConfig;
import com.lmrj.map.tray.entity.MapTrayChipMove;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import com.lmrj.map.tray.service.IMapTrayChipMovePseudoService;
import com.lmrj.map.tray.util.TraceDateUtil;
import com.lmrj.util.lang.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MapTrayChipMovePseudoServiceImpl  extends CommonServiceImpl<MapTrayChipMoveMapper, MapTrayChipMove> implements IMapTrayChipMovePseudoService {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String firstEqpId = "APJ-IGBT-SMT1,APJ-FRD-SMT1,APJ-DBCT-SORT1,APJ-DBCB-SORT1";//第一段结尾设备,即没有上一段的设备
    /**伪码追溯
     * 1.追溯本线段的数据
     * 2.追溯紧连着的上一段的数据
     * 只有1和2同时满足时才更新数据的状态为6
     * 追溯成功做了两件事:
     * (1)将本段的数据的map_flag由原来的0变为6,并填充数据的伪码
     * (2)将上一段数据的伪码更新为最新的伪码,即由上一段伪码改为本段的伪码
     */
    @Override
    public void tracePseudoData(MapEquipmentConfig eqp) {
        List<MapTrayChipMove> startDatas = baseMapper.getPseudoStart(eqp.getEqpId());//获得起始数据的列表(包括:APJ-IGBT-SORT1\APJ-FRD-SORT1\APJ-DBCT-SORT2\APJ-DBCB-SORT2\APJ-IGBT-SORT3\APJ-FRD-SORT3\APJ-HB1-SORT2\
        List<MapTrayChipMove> traceLogs = new ArrayList<>();
        if(startDatas!=null && startDatas.size()>0){
            for (MapTrayChipMove startData : startDatas) {
                String pseudoCode = eqp.getEqpId()+"_"+sdf.format(startData.getStartTime()); //TODO 伪码生成规则
                startData.setPseudoCode(pseudoCode);//追溯本段前,先将伪码更新好
                Map<String, Object> subLineMap = traceSubLine(startData);//  ******追溯本段******
                if(StringUtil.isNotEmpty(MapUtils.getString(subLineMap, "msg"))){
                    saveErrData(traceLogs, startData, "伪码追溯异常,本段数据没有全部找到,"+MapUtils.getString(subLineMap, "msg"), false);
                } else {
                    boolean chkErr = true;
                    if(chkErr){
                        List<MapTrayChipMove> subLineData = (List<MapTrayChipMove>)subLineMap.get("pseudoData");
                        //追溯前一段
                        if(!firstEqpId.contains(subLineData.get(subLineData.size()-1).getEqpId())){
                            String beforeLineCode = traceBeforeLine(subLineData.get(subLineData.size()-1)); //前一段伪码
                            if(StringUtil.isEmpty(beforeLineCode)){
                                saveErrData(traceLogs, startData, "伪码追溯异常,前一段数据没有找到sort2:"+subLineData.get(subLineData.size()-1).getId(), false);
                                continue;
                            } else{
                                if("Y".equals(startData.getJudgeResult())){
                                    Map<String, Object> param = new HashMap<>();
                                    param.put("pseudoCode", beforeLineCode);
                                    param.put("experimentRemark", pseudoCode);
                                    baseMapper.updateTempPseudoCode(param);//将最新的伪码更新到experimentRemark字段中暂存(同时map_flag由6变为8,使本段数据不会被再次找到,如果mapFlag不变会再次成为另一段的数据的上一段)
                                    baseMapper.updateBeforePseudoCode(pseudoCode);// 将暂存的最新的伪码更新到伪码字段中
                                }else{
                                    Map<String, Object> finishParam = new HashMap<>();
                                    finishParam.put("chipId", startData.getChipId());
                                    finishParam.put("pseudoCode", beforeLineCode);
                                    baseMapper.HB2Finish(finishParam);//此处有更新状态mapflag=2,更新chip_id
                                }
                            }
                        }
                        //更新本段数据
                        if("Y".equals(startData.getJudgeResult())){
                            startData.setMapFlag(6);//更新本段的伪码
                        } else{
                            startData.setMapFlag(2);//获得上段伪码
                        }
                        subLineData.add(startData);
                        updateBatchById(subLineData, 100);
                    }
                }
            }
        }
        saveErrData(traceLogs, null, null, true);
    }

    /** 获得每段线中的结束点的设备*/
    @Override
    public List<MapEquipmentConfig> getLineEndEqp() {
        return baseMapper.getMapEqpConfig();
    }

    /**追溯最后一段的数据,二次热压段的追溯(本段不同于上方伪码追溯,本段是制品码的追溯,不会改变伪码),本段就是组立机的良品和不良品追溯
     * 1,追溯2次热压段
     * 2,追溯上基板和下基板,也就是DBC的两个段
     * 3,追溯VI
     * 4,通过VI 追HB1段(1次热压段)
     */
    @Override
    public void traceHB2(){
        List<MapTrayChipMove> startData = baseMapper.getHB2Start();//获得起始数据的列表
        List<MapTrayChipMove> traceLogs = new ArrayList<>();
        List<MapEquipmentConfig> eqpCfg = baseMapper.getHB2EqpConfig();
        if(startData!=null && startData.size()>0){
            for (MapTrayChipMove assemblyData : startData) {//APJ-HB2-ASSEMBLY1
                Date endDate = assemblyData.getStartTime();
                boolean unErrFlag = true;
                //获得主线数据,即下基版线
                List<MapTrayChipMove> hb2LineDatas = baseMapper.getHB2Line(assemblyData);
                List<MapTrayChipMove> SMTDatas = new ArrayList<>();//smt + HB2-SORT1
                //检查数量和时间间隔
                MapTrayChipMove HB2SortData = new MapTrayChipMove();
                for(MapEquipmentConfig cfg : eqpCfg){
                    int cnt = 0;
                    boolean unfindFlag = true;
                    for(MapTrayChipMove beforeDateItem : hb2LineDatas){
                        if(cfg.getEqpId().equals(beforeDateItem.getEqpId())){
                            long chkSec = TraceDateUtil.getDiffSec(beforeDateItem.getStartTime(), endDate);
                            if(chkSec <= cfg.getIntervalTimeMax() && chkSec>=0){
                                if("4".equals(cfg.getEqpType())){//贴片
                                    cnt++;
                                    beforeDateItem.setMapFlag(2);
                                    beforeDateItem.setChipId(assemblyData.getChipId()); //TODO 此处最后关注
                                    SMTDatas.add(beforeDateItem);
                                    if(cnt == beforeDateItem.getSmtCount()){
                                        unfindFlag = false;
                                        break;
                                    }
                                } else {//移栽机
                                    beforeDateItem.setMapFlag(2);
                                    beforeDateItem.setChipId(assemblyData.getChipId()); //TODO 此处最后关注
                                    HB2SortData = beforeDateItem;
                                    unfindFlag = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(unfindFlag){
                        unErrFlag = false;
                        saveErrData(traceLogs, assemblyData, "伪码追溯异常,HB2数据数量不对", false);
                        break;
                    }
                }
                //追溯上基板和VI
                if(unErrFlag){
                    List<String> hbsortPseudo = new ArrayList<>();//保存所有要更新的伪码,包括,上基板,下基板,1次热压SORT
                    //追溯DBC段
                    String dbcbPseudoCode = traceBeforeLine(HB2SortData);
                    if(StringUtil.isEmpty(dbcbPseudoCode)){
                        saveErrData(traceLogs, assemblyData, "伪码追溯异常,DBCB没有找到", false);
                        continue;
                    } else{
                        hbsortPseudo.add(dbcbPseudoCode);//******下基板******
                    }
                    String dbctPseudoCode = traceBeforeLine(assemblyData);
                    if(StringUtil.isEmpty(dbctPseudoCode)){
                        saveErrData(traceLogs, assemblyData, "伪码追溯异常,DBCT没有找到", false);
                        continue;
                    } else{
                        hbsortPseudo.add(dbctPseudoCode);//******上基板******
                    }
                    //追溯VI
                    List<MapTrayChipMove> VIdatas = new ArrayList<>();
                    for(MapTrayChipMove SMTData : SMTDatas){
                        List<MapTrayChipMove> VIMoveDatas = baseMapper.findVI(SMTData);
                        if(VIMoveDatas != null && VIMoveDatas.size()>0){
                            boolean viFindFlag = true;
                            for(MapTrayChipMove viMoveData : VIMoveDatas){
                                long chkTime = TraceDateUtil.getDiffSec(viMoveData.getStartTime(), SMTData.getStartTime());
                                if(chkTime < viMoveData.getIntervalTimeMax() && chkTime>=0) {
                                    viMoveData.setMapFlag(2);
                                    viMoveData.setChipId(assemblyData.getChipId());
                                    VIdatas.add(viMoveData);//****** vi ******
                                    viFindFlag = false;
                                    break;
                                }
                            }
                            if(viFindFlag){
                                saveErrData(traceLogs, assemblyData, "伪码追溯异常,VI有坐标对应但时间超限,数据SMT-Id:"+SMTData.getId(), false);
                                unErrFlag = false;
                                break;//跳出 SMT
                            }
                        } else{
                            saveErrData(traceLogs, assemblyData, "伪码追溯异常,VI没有坐标对应,数据SMT-Id:"+SMTData.getId(), false);
                            unErrFlag = false;
                            break;//跳出 SMT
                        }
                    }
                    if(!unErrFlag) {
                        continue;// 下一个 assembly
                    }
                    //追溯HB1-SORT
                    for(MapTrayChipMove VIdata : VIdatas){
                        String hb1PseudoCode = traceBeforeLine(VIdata);
                        if(StringUtil.isEmpty(hb1PseudoCode)){
                            saveErrData(traceLogs, assemblyData, "伪码追溯异常,HB1-SORT2没有找到,数据VI-Id:"+VIdata.getId(), false);
                            unErrFlag = false;
                            break;
                        } else {
                            hbsortPseudo.add(hb1PseudoCode); //****** HB1 段 ******
                        }
                    }
                    if(!unErrFlag) {
                        continue;// 下一个 assembly
                    }
                    //1.更新HB2的数据和VI的数据;2.更新追溯到的带有伪码的段尾数据(先1后2,原因是1耗时长)
                    assemblyData.setMapFlag(2);
                    SMTDatas.add(assemblyData);
                    SMTDatas.add(HB2SortData);
                    updateBatchById(SMTDatas, 100); //SMT1\SMT2\APJ-HB2-SORT1\assembly
                    updateBatchById(VIdatas, 100);//VI
                    Map<String, Object> finishParam = new HashMap<>();
                    finishParam.put("chipId", assemblyData.getChipId());
                    for(String pseudo: hbsortPseudo){//上基板,下基板,1次热压SORT  含有伪码的
                        finishParam.put("pseudoCode", pseudo);
                        baseMapper.HB2Finish(finishParam);
                    }
                }
            }
        }
        saveErrData(traceLogs, null, null, true);
    }

    private String traceBeforeLine(MapTrayChipMove nextStart){
        List<MapTrayChipMove> beforeLines = baseMapper.findBeforeLineEnd(nextStart);
        if(beforeLines != null && beforeLines.size()>0){
            for(MapTrayChipMove beforeLine : beforeLines){
                long chkTime = TraceDateUtil.getDiffSec(beforeLine.getStartTime(), nextStart.getStartTime());
                if(chkTime < beforeLine.getIntervalTimeMax() && chkTime>=0) {
                    return beforeLine.getPseudoCode();
                }
            }
        }
        return null;
    }

    /** 不良品追溯(特殊的数据,段首数据)
     * 仅这些设备(APJ-IGBT-SORT2\APJ-FRD-SORT2\APJ-HB1-SORT1\APJ-HB2-SORT1)+APJ-VI,的数据,特点是这些设备位于每段段首
     *   (注:APJ-IGBT-SMT1\APJ-FRD-SMT1\APJ-DBCT-SORT1\APJ-DBCB-SORT1,这些设备的不良品是直接扔掉的,不会进入数据库的)
     *   其余设备的不良品追溯在伪码追溯方法中;3DAOI等段中设备的不良不追溯,因为它一定会走移栽机;
     */
    @Override
    public void traceNGData(){
        List<MapTrayChipMove> traceLogs = new ArrayList<>();
        List<MapTrayChipMove> ngStartList = baseMapper.findNGStart();
        if(ngStartList!=null && ngStartList.size()>0){
            //先追溯本段数据,然后追上一段伪码成功的数据,VI不需要追本段数据,
            for(MapTrayChipMove ngStart: ngStartList){//(一)组立机的不良的追溯是需要跟良品追溯一样的,追溯制品码;(二)中间段的ng不追,因为会在对应的移栽机追溯到
                String beforeLinePseudoCode = traceBeforeLine(ngStart);//获得上段伪码
                if(StringUtil.isEmpty(beforeLinePseudoCode)){
                    saveErrData(traceLogs, ngStart, "追溯NG异常S,没有找到上一段数据", false);
                    continue;
                }
                //更新伪码段数据的chip_id
                Map<String, Object> finishParam = new HashMap<>();
                finishParam.put("chipId", ngStart.getChipId());
                finishParam.put("pseudoCode", beforeLinePseudoCode);
                baseMapper.HB2Finish(finishParam);//此处有更新状态mapflag=2,更新chip_id
                //更新ngStart 本身
                ngStart.setMapFlag(2);
                updateById(ngStart);
            }
        }
        saveErrData(traceLogs, null, null, true);
    }

    /** 追溯本段数据
     * startData : 本段数据尾点数据
     * ngFlag:true 是NG,false:是伪码追溯
     * 注意: VI 不会进来
     */
    private Map<String, Object> traceSubLine(MapTrayChipMove startData){
        List<MapTrayChipMove> pseudoData = new ArrayList<>();
        List<MapEquipmentConfig> lineCfgEqp = baseMapper.getCfgEqpForLine(startData.getEqpId());
        List<MapTrayChipMove> lineData = baseMapper.getPseudoLine(startData);
        Date curDate = startData.getStartTime();
        Map<String, Object> rs = new HashMap<>();
        for(MapEquipmentConfig cfgEqp : lineCfgEqp){
            boolean unfind = true;
            long logTime = 0;
            for(MapTrayChipMove item : lineData){
                if("Y".equals(cfgEqp.getSameLotFlag()) && !startData.getLotNo().equals(item.getLotNo())){//是否同批次好
                    continue;
                }
                //符合条件是时间间隔在cfgEqp中设置的间隔内(sql中以满足坐标相等)
                long timeChk = TraceDateUtil.getDiffSec(item.getStartTime(), curDate);
                if(timeChk<0){
                    break;
                }
                logTime = timeChk;
                if(cfgEqp.getEqpId().equals(item.getEqpId()) && timeChk < cfgEqp.getIntervalTimeMax() && timeChk >= 0){
                    if("Y".equals(startData.getJudgeResult()) && item.getMapFlag() == 0){
                        unfind = false;
                        curDate = item.getStartTime();
                        item.setPseudoCode(startData.getPseudoCode());
                        item.setMapFlag(6);
                    } else if(item.getMapFlag() == 0){//不能追良品追过的数据
                        unfind = false;
                        curDate = item.getStartTime();
                        item.setChipId(startData.getChipId());
                        item.setMapFlag(2);
                    }
                    pseudoData.add(item);
                    break;
                }
            }
            if(unfind){
                rs.put("msg", "缺"+cfgEqp.getEqpId()+",时间限制:"+cfgEqp.getIntervalTimeMax()+",最近的相差:"+logTime);
                return rs;
            }
        }
        rs.put("msg", "");
        rs.put("pseudoData", pseudoData);
        return rs;
    }

    /** 错误日志,记录在每个数据上;当累计99次时将mapflag也更新为99,使其不能再次被选为要追溯的数据,即不再追溯它了*/
    private void saveErrData(List<MapTrayChipMove> traceLogs, MapTrayChipMove data, String errMsg, boolean saveFlag){
        if(saveFlag && traceLogs.size()>0){
            updateBatchById(traceLogs, 1000);
        } else if (!saveFlag){
            if(data.getExperimentChk() == 99){//累计该条数据被追溯的次数
                data.setMapFlag(99);
            } else {
                int chk = data.getExperimentChk();
                data.setExperimentChk(chk + 1);
            }
            data.setExperimentRemark(errMsg);
            traceLogs.add(data);
        }
    }
}