package com.lmrj.mes.kongdong.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.kongdong.mapper.MsMeasureKongdongMapper;
import com.lmrj.mes.kongdong.service.IMsMeasureKongdongService;
import com.lmrj.util.FileUtils;
import com.lmrj.util.calendar.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
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

    @Override
    public int findKongdongData(String lineNo, String productionName, String lotNo){
        Integer rs = baseMapper.findKongdongData(lineNo,productionName,lotNo);
        return rs==null?0:rs;
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
        List<String> legends = baseMapper.getLegend(param);
        List<Map<String, Double>> configLine = baseMapper.getConfig(MapUtils.getString(param, "productionName"));
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
                        if(line.equals(MapUtils.getString(data,"lineType")) && asix.equals(MapUtils.getString(data, "lotNo"))){
                            LinesData.get(line).add(MapUtils.getDouble(data, "voidRatio"));
                            findFlag = false;
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
                seriesArr.add(lineItem);
            }
        }
        Map<String, Object> rs = new HashMap<>();
        rs.put("legend",legends);
        rs.put("series",seriesArr);
        rs.put("xAxis",xasix);
        return rs;
    }

    @Override
    public Map<String, Object> kongDongBar(Map<String, Object> param) {
        Map<String, Object> rs = new HashMap<>();
        rs.put("barData", baseMapper.getBar(param));
        rs.put("configData", baseMapper.getConfig(MapUtils.getString(param, "productionName")));
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
