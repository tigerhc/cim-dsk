package com.lmrj.mes.kongdong.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.kongdong.mapper.MsMeasureKongdongMapper;
import com.lmrj.mes.kongdong.service.IMsMeasureKongdongService;
import com.lmrj.util.FileUtils;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("msMeasureKongdongService")
@Slf4j
public class MsMeasureKongdongServiceImpl extends CommonServiceImpl<MsMeasureKongdongMapper, MsMeasureKongdong> implements IMsMeasureKongdongService {
    static public Map<String,String> typeMap= new HashMap<>();
    static {
        typeMap.put("DI-9","DI-1");
        typeMap.put("DI-10","DI-2");
        typeMap.put("MOS-11","MOS-3");
        typeMap.put("MOS-12","MOS-4");
        typeMap.put("MOS-13","MOS-5");
        typeMap.put("JP-14","JP-6");
        typeMap.put("MIC-D-15","MIC-D-7");
        typeMap.put("MIC-16","MIC-8");
    }
    @Override
    public int findKongdongData(String lineNo, String productionName, String lotNo){
        Integer rs = baseMapper.findKongdongData(lineNo,productionName,lotNo);
        return rs==null?0:rs;
    }
    @Override
    public Integer findKongdongExist(String lineNo, String productionName, String lotNo, String type){
        return baseMapper.findKongdongExist(lineNo,productionName,lotNo,type);
    }

    @Override
    public List<String> getPositionSelect(String eqpId) {
        return baseMapper.getPositionSelect(eqpId);
    }

    @Override
    public Map<String, Object> chkDataDefect(Map<String, Object> chkParam) {
        Map<String, Object> chkRs = new HashMap<>();
        List<Map<String, Object>> dataList = baseMapper.chkKongdongData(chkParam);
        if(dataList.size()>0){
            try {
                String productionName = MapUtils.getString(dataList.get(0), "productionName");
                String type = "";
                String time = MapUtils.getString(dataList.get(0), "crtDt");
                String lotNo = MapUtils.getString(dataList.get(0), "lotNo");
                StringBuffer msg = new StringBuffer();
                for(long index =0; index<dataList.size();index++){
                    Map<String, Object> dataItem = dataList.get((int)index);
                    String dataItemProductionName = MapUtils.getString(dataItem, "productionName");
                    String dataItemLotNo = MapUtils.getString(dataItem, "lotNo");
                    String dataItemType = MapUtils.getString(dataItem, "lineType");
                    String dataItemTime = MapUtils.getString(dataItem, "crtDt");
                    if(index == dataList.size()-1 && !dataItemProductionName.contains("5GI-") && !dataItemProductionName.contains("6GI-")){//最后一个批次
                        type = type + "," + dataItemType ;
                        String errType = getUnhaveData(productionName, type);
                        if (!StringUtils.isEmpty(errType)) {
                            msg.append(","+ lotNo +"_"+ errType);
                            chkRs.put(productionName, msg.toString());
                        }
                    } else if(!dataItemProductionName.contains("5GI-") && !dataItemProductionName.contains("6GI-")){
                        if(productionName.equals(dataItemProductionName) && lotNo.equals(dataItemLotNo)){
                            if (time.equals(dataItemTime)) {
                                type = type + "," + dataItemType ;
                            } else {
                                msg.append(productionName + "_" + lotNo + "的时间不一致,");
                            }
                        }else{
                            String errType = getUnhaveData(productionName, type);
                            if (!StringUtils.isEmpty(errType)) {
                                msg.append("," + lotNo +"_"+ errType);
                            }
                            if(!productionName.equals(dataItemProductionName) ){
                                if(!StringUtils.isEmpty(msg.toString())){
                                    chkRs.put(productionName + "", msg.toString());
                                    msg = new StringBuffer();
                                }
                                productionName = dataItemProductionName;
                            }
                            lotNo = dataItemLotNo;
                            time = dataItemTime;
                            type = dataItemType;//放置第一个元素
                        }
                    }
                }
            }catch (Exception e){
                log.error("空洞自定义检测接口报错",e);
            }
        }
        return chkRs;
    }

    /**获得缺少的数据的type，空字符串视为没有问题*/
    private String getUnhaveData(String productionName, String datas){
        String simTitle = "DI-1,DI-2,MIC-7,MIC-D-6,MOS-3,MOS-4,MOS-5";
        String smaTitle = "DI-1,MIC-5,MIC-D-3,MOS-2,MOS-3-4";
        String sx680Title = "DI-1,DI-2,MIC-8,MIC-D-7,MOS-3,MOS-4,MOS-5";
        String sx681Title = "DI-1,DI-2,JP-6,MIC-8,MIC-D-7,MOS-3,MOS-4,MOS-5";
        String[] types;
        if(productionName.contains("SIM")){
            types = simTitle.split(",");
        } else if(productionName.contains("SMA")){
            types = smaTitle.split(",");
        } else if(productionName.contains("SX680")){
            types = sx680Title.split(",");
        } else if(productionName.contains("SX681")){
            types = sx681Title.split(",");
        } else if(productionName.contains("5GI-") || productionName.contains("6GI-")){
            return "";//不处理5、6GI
        }else {
            return "请配置新的先别:" + productionName;
        }
        String unhaveType = "";
        for(String type : types){
            if(!datas.contains(type)){
                unhaveType = type + ",";
            }
        }
        return unhaveType.equals("")?unhaveType:"缺少："+ unhaveType;
    }

    @Override
    public List<MsMeasureKongdong> saveBeforeFile(int index) {
        List<String> pathArr = new ArrayList<>();
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SIM\\2020年\\7月\\SIM6812M(E)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SIM\\2020年\\8月\\SIM6812M(E)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SIM\\2020年\\8月\\SIM6812M(E)D-URA F2971");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SIM\\2020年\\9月\\SIM6812M(E)D-URA F2971");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SIM\\2020年\\10月\\SIM6812M(E)D-URA F2971");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SIM\\2020年\\11月\\SIM6812M(E)D-URA F2971");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\8月\\SMA6821MX(P)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\8月\\SMA6822MX(E)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\8月\\SMA6860MH(S)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\8月\\SMA6863MH(S)D-B4D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\8月\\SMA6863MZ(Q)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\9月\\SMA6821MX(P)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\9月\\SMA6822MX(E)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\9月\\SMA6823MX(N)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\9月\\SMA6860MH(S)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\9月\\SMA6863MH(S)D-B4D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\10月\\SMA6821MX(P)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\10月\\SMA6822MX(E)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\10月\\SMA6860MH(S)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\10月\\SMA6863MH(S)D-B4D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\11月\\SMA6821MX(P)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\11月\\SMA6822MX(E)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\11月\\SMA6823MX(N)D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SMA\\2020年\\11月\\SMA6863MH(S)D-B4D");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\8月\\SX68111");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\8月\\SX68124");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\9月\\SX68111");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\9月\\SX68111MA (P)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\9月\\SX68121M (AX)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\9月\\SX68122M (AU)YGD-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\9月\\SX68124");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\9月\\SX68124M (AY)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\10月\\SX68002MX (N)YGD-RP LPS");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\10月\\SX68111MA (P)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\10月\\SX68121M (AX)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\10月\\SX68124M (AY)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\10月\\SX68125M (AZ)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\11月\\SX68002MX (N)YGD-RP LPS");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\11月\\SX68121M (AX)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\11月\\SX68124M (AY)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\SX\\2020年\\11月\\SX68125M (AZ)D-RP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\7月\\5GI-2860B");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\8月\\5GI-2860B");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\8月\\5GI-2860B(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\8月\\5GI-2865");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\9月\\5GI-2860B(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\9月\\5GI-2865(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\9月\\5GI-2866(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\10月\\5GI-2860B(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\10月\\5GI-2864(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\10月\\5GI-2865(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\10月\\5GI-2866(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\11月\\5GI-2860B(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\11月\\5GI-2864(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\5GI\\2020年\\11月\\5GI-2866(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\7月\\6GI-2875");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\8月\\6GI-2870");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\8月\\6GI-2870(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\8月\\6GI-2872(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\8月\\6GI-2873(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\8月\\6GI-2875");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\8月\\6GI-2875(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\9月\\6GI-2870(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\9月\\6GI-2872(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\9月\\6GI-2873(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\9月\\6GI-2875(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\10月\\6GI-2870(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\10月\\6GI-2872(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\10月\\6GI-2873(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\10月\\6GI-2874(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\10月\\6GI-2875(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\11月\\6GI-2870(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\11月\\6GI-2873(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\11月\\6GI-2874(G)D-AP");
        pathArr.add("D:\\DSK1\\IT化データ（一課）\\X線データ\\データ処理\\ボイド率\\6GI\\2020年\\11月\\6GI-2875(G)D-AP");
        for(int i=0; i<pathArr.size(); i++){
            if(i==index){
                List<Map<String, Object>> fileNames = FileUtils.getFileInfos(pathArr.get(i), "","");
                if(fileNames.size()>0){
                    List<MsMeasureKongdong> list = new ArrayList<>();
                    for(Map<String, Object> item : fileNames){
                        _getDataByFile(list, MapUtils.getString(item, "fileName"), MapUtils.getString(item, "createTime"), pathArr.get(i));
                    }
                    if(list.size()>0){
                        insertBatch(list, 1000);
                    }
                    log.info("-------------------------------------------------------------"+list.size());
                    return list;
                }
                break;
            }
        }
        return null;
    }

    @Override
    public List<MsMeasureKongdong> getKongdong(Map<String, Object> param) {
        return baseMapper.getKongdong(param);
    }

    @Override
    public Map<String, Object> kongdongChart(Map<String, Object> param) {
        String productionName = MapUtils.getString(param, "productionName");
        List<String> legends = baseMapper.getLegend(param);
        Map<String,Object> configParam = new HashMap<>();
        configParam.put("productionName", productionName);
        configParam.put("lineType", MapUtils.getString(param, "lineType"));
        List<Map<String, Double>> configLine = baseMapper.getConfig(configParam);
        List<String> xasix = baseMapper.getXasix(param);
        Map<String, List<Double>> LinesData = new HashMap<>();
        //初始化series 各个线的数组
        if(legends.size()>0){
            for(String legend : legends){
                LinesData.put(legend, new ArrayList<>());
            }
            List<Map<String, Object>> datas = baseMapper.getData(param);
            for(String asix : xasix){
                //根据横轴坐标补充该坐标上所有线对应的值，没有补空
                for(String line : legends){
                    boolean findFlag = true;
                    for(Map<String, Object>  data : datas){
                        if(productionName.contains("5GI")||productionName.contains("6GI")){
                            if(asix.equals(MapUtils.getString(data, "lotNo"))){
                                LinesData.get(line).add(MapUtils.getDouble(data, "voidRatio"));
                                findFlag = false;
                            }
                        }else{
                            if(line.equals(MapUtils.getString(data,"lineType")) && asix.equals(MapUtils.getString(data, "lotNo"))){
                                LinesData.get(line).add(MapUtils.getDouble(data, "voidRatio"));
                                findFlag = false;
                            }
                        }
                    }
                    if(findFlag){
                        LinesData.get(line).add(null);
                    }
                }
            }
        }
        //添加数据对应的折线
        List<Object> seriesArr = new ArrayList<>();
        for (String legend : legends){
            Map<String,  Object> lineItem = new HashMap<>();
            lineItem.put("name", legend);
            lineItem.put("type", "line");
            lineItem.put("data", LinesData.get(legend));
            seriesArr.add(lineItem);
        }
        //添加上限对应的折线
        if(configLine.size()>0){
            for(Map<String, Double> configItem : configLine){
                Map<String, Object> lineItem = new HashMap<>();
                lineItem.put("type", "line");
                lineItem.put("name", "limit-"+MapUtils.getString(configItem, "lineType"));
                legends.add("limit-"+MapUtils.getString(configItem, "lineType"));
                List<Double> data = new ArrayList<>();
                for(int i=0; i<xasix.size(); i++){
                    data.add(MapUtils.getDouble(configItem, "lmt"));
                }
                lineItem.put("data", data);
                lineItem.put("color", "red");
                //加虚线折线箭头
                lineItem.put("markLine",getMarkLine("最大数据"));
                seriesArr.add(lineItem);
            }
        }
        Map<String, Object> rs = new HashMap<>();
        rs.put("legend",legends);
        rs.put("series",seriesArr);
        rs.put("xAxis",xasix);
        return rs;
    }

    /**
     *markLine: {
     *               data: [
     *                 { type: 'max', name: '最大数据' }
     *               ]
     *             }
     */
    private Map<String, Object> getMarkLine(String name){
        Map<String, Object> dataItem = new HashMap<>();
        dataItem.put("type","max");
        dataItem.put("name",name);
        List<Map<String, Object>> arr = new ArrayList<>();
        arr.add(dataItem);

        Map<String, Object> rs = new HashMap<>();
        rs.put("data", arr);
        return rs;
    }

    @Override
    public Map<String, Object> kongDongBar(Map<String, Object> param) {
        Map<String, Object> rs = new HashMap<>();
        rs.put("barData", baseMapper.getBar(param));
        rs.put("configData", baseMapper.getConfig(param));
        return rs;
    }

    private boolean _chkFileNameLine(String fileName){
        if(!fileName.contains("5GI")&& !fileName.contains("6GI")){
            if(fileName.indexOf(" ")<=0){
                return false;
            }
            if(fileName.indexOf("%")<=0){
                return false;
            }
            if(fileName.indexOf("-")<=0){
                return false;
            }
            return true;
        } else {
            if(fileName.indexOf("%")<=0){
                return false;
            }else{
                return true;
            }
        }
    }

    private void _getDataByFile(List<MsMeasureKongdong> list, String fileName, String createDate, String filePath){
        log.error("fileName:"+fileName+",filePath"+filePath);
        if(_chkFileNameLine(filePath+"\\"+fileName)){
            String productionNo = filePath.substring(filePath.lastIndexOf("\\")+1);
            if(filePath.contains("5GI")||filePath.contains("6GI")){
                String lotNo = fileName.substring(0,fileName.indexOf("-"));
                String voidRatio = fileName.substring(fileName.indexOf("-")+1,fileName.indexOf("%"));
                MsMeasureKongdong data = new MsMeasureKongdong();
                data.setLotNo(lotNo);
                data.setVoidRatio(Double.parseDouble(voidRatio));
                if(filePath.contains("5GI")){
                    data.setLineNo("5GI");
                }else{
                    data.setLineNo("6GI");
                }
                data.setProductionName("J."+productionNo);
                data.setCreateDate(DateUtil.parseDate(createDate));
                list.add(data);
            }else{
                filePath = filePath.substring(0,filePath.lastIndexOf("\\2020年"));
                String lineNo = filePath.substring(filePath.lastIndexOf("\\")+1);
                String lotNo = fileName.substring(0,fileName.indexOf(" "));//0927A2.3%-DI-1.jpg,filePathD:\DSK1\IT化データ（一課）\X線データ\データ処理\ボイド率\SIM\2020年\10月\SIM6812M(E)D-URA F2971
                String voidRatio = fileName.substring(fileName.indexOf(" ")+1,fileName.indexOf("%"));
                String type = fileName.substring(fileName.indexOf("%")+2,fileName.indexOf(".jpg"));
                MsMeasureKongdong data = new MsMeasureKongdong();
                data.setLotNo(lotNo);
                data.setVoidRatio(Double.parseDouble(voidRatio));
                data.setLineNo(lineNo);
                data.setProductionName("J."+productionNo);
                data.setType(type);
                data.setCreateDate(DateUtil.parseDate(createDate));
                list.add(data);
            }
        }
    }
}
