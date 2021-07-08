package com.lmrj.mes.measure.service.impl;

import com.lmrj.common.mybatis.mvc.service.impl.CommonServiceImpl;
import com.lmrj.mes.measure.entity.MeasureSx;
import com.lmrj.mes.measure.mapper.MeasureSxMapper;
import com.lmrj.mes.measure.service.MeasureSxService;
import com.lmrj.util.collection.MapUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service("MeasureSxService")
@Slf4j
public class MeasureSxServiceImpl extends CommonServiceImpl<MeasureSxMapper, MeasureSx> implements MeasureSxService {
    @Autowired
    private MeasureSxMapper measureSxMapper;

    private Map<String, String> giLineName = JsonUtil.from("{\"burr_f\":\"毛刺\",\"pin_f1\":\"1:1PIN\",\"pin_f2\":\"1:2PIN\",\"pin_f3\":\"1:3PIN\",\"pin_f4\":\"1:4PIN\",\"pin_f5\":\"1:5PIN\",\"pin_f6\":\"1:6PIN\",\"pin_f1_f2\":\"1:1PIN-2PIN\",\"pin_f2_f3\":\"1:2PIN-3PIN\",\"pin_f3_f4\":\"1:3PIN-4PIN\",\"pin_f4_f5\":\"1:4PIN-5PIN\",\"pin_f5_f6\":\"1:5PIN-6PIN\",\"pin_s1\":\"2:1PIN\",\"pin_s2\":\"2:2PIN\",\"pin_s3\":\"2:3PIN\",\"pin_s4\":\"2:4PIN\",\"pin_s5\":\"2:5PIN\",\"pin_s6\":\"2:6PIN\"}", HashMap.class);

    @Override
    public List<Map<String, String>> findProductionNo(String type, String lineNo) {
        if("SX".equals(lineNo)){
            return measureSxMapper.findProductionNo(type);
        } else if("SIM".equals(lineNo)){
            return measureSxMapper.findSimProductionNo(type);
        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("lineNo", lineNo);
            param.put("type", type);
            return measureSxMapper.findGiProductionNo(param);
        }
    }

    public List findSxNumber(String productionName, String number, String startDate, String endDate, String type, String local) {
        number = "1";
        List<Map<String, String>> result = measureSxMapper.findSxNumber(productionName, number, startDate, endDate, type);
        number = "2";
        List<Map<String, String>> result2 = measureSxMapper.findSxNumber(productionName, number, startDate, endDate, type);
        List patent = new LinkedList();
        List title = new LinkedList();
        List arr = new LinkedList();
        List totalValue = new LinkedList();
        List totalValue2 = new LinkedList();
        for (Map map : result) {
            title.add(map.get("lotNo"));
            totalValue.add(map.get(local + "1"));
            totalValue.add(map.get(local + "2"));
        }
        for (Map map : result2) {
            totalValue2.add(map.get(local + "1"));
            totalValue2.add(map.get(local + "2"));
        }

        for (int i = 0; i < 6; i++) {
            Map element = new HashMap();
            if (i == 2 || i == 3) {
                Map markLine = new HashMap();
                List dataB = new ArrayList();
                Map basic = new HashMap();
                basic.put("type", "max");
                basic.put("name", "最大数据");
                dataB.add(basic);
                markLine.put("data", dataB);
                element.put("markLine", markLine);
            }
            switch (i) {
                case 0:
                    element.put("name", "1-1:" + local.toUpperCase());
                    break;
                case 1:
                    element.put("name", "1-2:" + local.toUpperCase());
                    break;
                case 2:
                    element.put("name", "上限");
                    break;
                case 3:
                    element.put("name", "下限");
                    break;
                case 4:
                    element.put("name", "2-1:" + local.toUpperCase());
                    break;
                case 5:
                    element.put("name", "2-2:" + local.toUpperCase());
                    break;
            }
            element.put("type", "line");
            List elementArr = new LinkedList();
            int size = (result.size() > result2.size()) ? result2.size() : result.size();
            for (int j = 0; j < size; j++) {
                if (i == 2) {
                    if (local.equals("a")) {
                        elementArr.add(14.3);
                    } else if (local.equals("b")) {
                        elementArr.add(1.00);
                    } else if (local.equals("c")) {
                        elementArr.add(0.13);
                    } else if (local.equals("d")) {
                        elementArr.add(17);
                    }
                } else if (i == 3) {
                    if (local.equals("a")) {
                        elementArr.add(13.9);
                    } else if (local.equals("b")) {
                        elementArr.add(0.4);
                    } else if (local.equals("c")) {
                        elementArr.add(0.07);
                    } else if (local.equals("d")) {
                        elementArr.add(0);
                    }
                } else {
                    if (i < 2) {
                        elementArr.add(totalValue.get((j * 2) + i));
                    } else if (i > 3) {
                        if (i == 4) {
                            int q = 0;
                            elementArr.add(totalValue2.get((j * 2) + q));
                        }
                        if (i == 5) {
                            int q = 1;
                            elementArr.add(totalValue2.get((j * 2) + q));
                        }
                    }
                }
            }
            element.put("data", elementArr);
            arr.add(element);
        }
        patent.add(title);
        patent.add(arr);
        Map min = new HashMap();
        if (local.equals("a")) { //13.9
            min.put("min", 13.8);
        } else if (local.equals("b")) { //0.4
            min.put("min", 0.3);
        } else if (local.equals("c")) {   //0.07
            min.put("min", 0.06);
        } else if (local.equals("d")) {   //0
            min.put("min", 0);
        }
        patent.add(min);
        return patent;
    }

    /** wangdong 简单重构
     */
    public List findSimNumber(String productionName, String number, String startDate, String endDate, String type, String local) {
        List<Map<String, String>> dataList = measureSxMapper.findSimNumber(productionName, startDate, endDate, type);//所有线的原数据集合
        List<Map<String, String>> limitList = measureSxMapper.findSimLimit();
        Map<String, Double> data1_1Map = new HashMap<>();
        Map<String, Double> data1_2Map = new HashMap<>();
        Map<String, Double> data2_1Map = new HashMap<>();
        Map<String, Double> data2_2Map = new HashMap<>();
        List<Double> data1_1 = new ArrayList<>();
        List<Double> data1_2 = new ArrayList<>();
        List<Double> data2_1 = new ArrayList<>();
        List<Double> data2_2 = new ArrayList<>();
        List<String> xAsix = new ArrayList<>();//echart 横轴数据
        List<Double> minLimit = new ArrayList<>();//下限
        List<Double> maxLimit = new ArrayList<>();//上限
        Double echartMin = null;
        Double limitMin = null;

        List optionDatas = new ArrayList();//echart 需要的各个线
        String curLotNo = "";
        //将数据库中的原数据拆分出echart 需要的各个线
        for(Map<String, String> data : dataList){
            //拆分横轴数据
            if(!curLotNo.equals(MapUtil.getString(data, "lotNo"))){
                curLotNo = MapUtil.getString(data, "lotNo");
                xAsix.add(curLotNo);
                //补充上限和下限
                if(limitList.size()>1){//正常情况下, 该数组的长度为2,下标为0的是最小值,为1的是最大值
                    String min = MapUtil.getString(limitList.get(0), local);
                    String max = MapUtil.getString(limitList.get(1), local);
                    if(StringUtil.isNotEmpty(min)){
                        minLimit.add(Double.parseDouble(min));
                    }else{
                        minLimit.add(0d);
                    }
                    if(StringUtil.isNotEmpty(max)){
                        maxLimit.add(Double.parseDouble(max));
                    }else{
                        maxLimit.add(0d);
                    }
                    limitMin = Double.parseDouble(MapUtil.getString(limitList.get(0), local));
                }
            }

            String lineData = MapUtil.getString(data, local);

            //记录所有数据中最小的值,echart使用
            if(StringUtil.isNotEmpty(lineData)){
                Double dataD = Double.parseDouble(lineData);
                if(echartMin==null){
                    echartMin = dataD;
                } else {
                    echartMin = echartMin > dataD ? dataD : echartMin;
                }
            }
            //拆分1-1,1-2,2-1,2-2数据
            if ("1-1".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data1_1Map.put(curLotNo, Double.parseDouble(lineData));
                }
            }else if("1-2".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data1_2Map.put(curLotNo, Double.parseDouble(lineData));
                }
            }else if("2-1".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data2_1Map.put(curLotNo, Double.parseDouble(lineData));
                }
            }else if("2-2".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data2_2Map.put(curLotNo, Double.parseDouble(lineData));
                }
            }
        }
        //缺失数据让线断开
        for (String asix : xAsix) {
            data1_1.add(MapUtil.getDouble(data1_1Map, asix));
            data1_2.add(MapUtil.getDouble(data1_2Map, asix));
            data2_1.add(MapUtil.getDouble(data2_1Map, asix));
            data2_2.add(MapUtil.getDouble(data2_2Map, asix));
        }

        optionDatas.add(xAsix);
        //将数据的分配name并放入返回对象中
        List<Map<String, Object>> lines = new ArrayList<>();
        Map<String, Object> lineObj1_1 = new HashMap<>();
        lineObj1_1.put("name","1-1:"+local.toUpperCase());
        lineObj1_1.put("type","line");
        lineObj1_1.put("data", data1_1);
        lines.add(lineObj1_1);
        Map<String, Object> lineObj1_2 = new HashMap<>();
        lineObj1_2.put("name","1-2:"+local.toUpperCase());
        lineObj1_2.put("type","line");
        lineObj1_2.put("data", data1_2);
        lines.add(lineObj1_2);
        //由于前端页面echart线的颜色影响,,lines的第3和4需是上下限
        Map<String, Object> lineMinObj = new HashMap<>();
        lineMinObj.put("name","下限");
        lineMinObj.put("type","line");
        lineMinObj.put("data", minLimit);
        lines.add(lineMinObj);
        Map<String, Object> lineMaxObj = new HashMap<>();
        lineMaxObj.put("name","上限");
        lineMaxObj.put("type","line");
        lineMaxObj.put("data", maxLimit);
        lines.add(lineMaxObj);

        Map<String, Object> lineObj2_1 = new HashMap<>();
        lineObj2_1.put("name","2-1:"+local.toUpperCase());
        lineObj2_1.put("type","line");
        lineObj2_1.put("data", data2_1);
        lines.add(lineObj2_1);
        Map<String, Object> lineObj2_2 = new HashMap<>();
        lineObj2_2.put("name","2-2:"+local.toUpperCase());
        lineObj2_2.put("type","line");
        lineObj2_2.put("data", data2_2);
        lines.add(lineObj2_2);
        optionDatas.add(lines);

        //echart Y轴最小值
        Map min = new HashMap();
        echartMin = echartMin > limitMin ? limitMin : echartMin;//数据线与下限之间取最小值
        echartMin = echartMin * 0.98;//为了数据线不贴在x轴上
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        min.put("min", df.format(echartMin));
        optionDatas.add(min);

        return optionDatas;
    }

    public List findGiNumber(String productionName, String lineNo, String startDate, String endDate, String type, String local) {
        List<Map<String, String>> dataList = measureSxMapper.findGiNumber(productionName, startDate, endDate, type);//所有线的原数据集合
        List<Map<String, String>> limitList = measureSxMapper.findGiLimit(lineNo);
        Map<String, Double> data1Map = new HashMap<>();
        Map<String, Double> data2Map = new HashMap<>();
        Map<String, Double> data3Map = new HashMap<>();
        Map<String, Double> data4Map = new HashMap<>();
        Map<String, Double> data5Map = new HashMap<>();
        List<Double> data1 = new ArrayList<>();
        List<Double> data2 = new ArrayList<>();
        List<Double> data3 = new ArrayList<>();
        List<Double> data4 = new ArrayList<>();
        List<Double> data5 = new ArrayList<>();
        List<String> xAsix = new ArrayList<>();//echart 横轴数据
        List<Double> minLimit = new ArrayList<>();//下限
        List<Double> maxLimit = new ArrayList<>();//上限
        Double echartMin = null;
        Double limitMin = null;

        List optionDatas = new ArrayList();//echart 需要的各个线
        String curLotNo = "";
        //将数据库中的原数据拆分出echart 需要的各个线
        for(Map<String, String> data : dataList){
            //拆分横轴数据
            if(!curLotNo.equals(MapUtil.getString(data, "lotNo"))){
                curLotNo = MapUtil.getString(data, "lotNo");
                xAsix.add(curLotNo);
                //补充上限和下限
                if(limitList.size()>1){//正常情况下, 该数组的长度为2,下标为0的是最小值,为1的是最大值
                    minLimit.add(Double.parseDouble(MapUtil.getString(limitList.get(1), local)));
                    maxLimit.add(Double.parseDouble(MapUtil.getString(limitList.get(0), local)));
                    limitMin = Double.parseDouble(MapUtil.getString(limitList.get(0), local));
                }
            }

            String lineData = MapUtil.getString(data, local);
            //记录所有数据中最小的值,echart使用
            if(StringUtil.isNotEmpty(lineData)){
                Double dataD = Double.parseDouble(lineData);
                if(echartMin==null){
                    echartMin = dataD;
                } else {
                    echartMin = echartMin > dataD ? dataD : echartMin;
                }
            }
            //拆分1-5数据
            if ("1".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data1Map.put(curLotNo, Double.parseDouble(lineData));
                }
            } else if("2".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data2Map.put(curLotNo, Double.parseDouble(lineData));
                }
            } else if("3".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data3Map.put(curLotNo, Double.parseDouble(lineData));
                }
            } else if("4".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data4Map.put(curLotNo, Double.parseDouble(lineData));
                }
            } else if("5".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineData)){
                    data5Map.put(curLotNo, Double.parseDouble(lineData));
                }
            }
        }

        //缺失数据让线断开
        for (String asix : xAsix) {
            data1.add(MapUtil.getDouble(data1Map, asix));
            data2.add(MapUtil.getDouble(data2Map, asix));
            data3.add(MapUtil.getDouble(data3Map, asix));
            data4.add(MapUtil.getDouble(data4Map, asix));
            data5.add(MapUtil.getDouble(data5Map, asix));
        }

        optionDatas.add(xAsix);
        //将数据的分配name并放入返回对象中
        List<Map<String, Object>> lines = new ArrayList<>();
        Map<String, Object> lineObj1 = new HashMap<>();
        lineObj1.put("name","1-"+giLineName.get(local));
        lineObj1.put("type","line");
        lineObj1.put("data", data1);
        lines.add(lineObj1);
        Map<String, Object> lineObj2 = new HashMap<>();
        lineObj2.put("name","2-"+giLineName.get(local));
        lineObj2.put("type","line");
        lineObj2.put("data", data2);
        lines.add(lineObj2);

        Map<String, Object> minLimitLine = new HashMap<>();
        minLimitLine.put("name","下限");
        minLimitLine.put("type","line");
        minLimitLine.put("data", minLimit);
        lines.add(minLimitLine);
        Map<String, Object> maxLimitLine = new HashMap<>();
        maxLimitLine.put("name","上限");
        maxLimitLine.put("type","line");
        maxLimitLine.put("data", maxLimit);
        lines.add(maxLimitLine);

        Map<String, Object> lineObj3 = new HashMap<>();
        lineObj3.put("name","3-"+giLineName.get(local));
        lineObj3.put("type","line");
        lineObj3.put("data", data3);
        lines.add(lineObj3);
        Map<String, Object> lineObj4 = new HashMap<>();
        lineObj4.put("name","4-"+giLineName.get(local));
        lineObj4.put("type","line");
        lineObj4.put("data", data4);
        lines.add(lineObj4);

        Map<String, Object> lineObj5 = new HashMap<>();
        lineObj5.put("name","5-"+giLineName.get(local));
        lineObj5.put("type","line");
        lineObj5.put("data", data5);
        lines.add(lineObj5);
        optionDatas.add(lines);

        //echart Y轴最小值
        Map min = new HashMap();
        echartMin = echartMin > limitMin ? limitMin : echartMin;//数据线与下限之间取最小值
        if(0== echartMin){
            echartMin = -0.1;
        } else {
            echartMin = echartMin * 0.98;//为了数据线不贴在x轴上
        }
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        min.put("min", df.format(echartMin));
        optionDatas.add(min);

        return optionDatas;
    }

    /**56GI总的*/
    public List findGiNumberAll(String productionName, String lineNo, String startDate, String endDate, String type) {
        List<Map<String, String>> dataList = measureSxMapper.findGiNumber(productionName, startDate, endDate, type);//所有线的原数据集合
        Map<String, Double> data1Map = new HashMap<>();
        Map<String, Double> data2Map = new HashMap<>();
        Map<String, Double> data3Map = new HashMap<>();
        Map<String, Double> data4Map = new HashMap<>();
        List<Double> data1 = new ArrayList<>();
        List<Double> data2 = new ArrayList<>();
        List<Double> data3 = new ArrayList<>();
        List<Double> data4 = new ArrayList<>();
        List<String> xAsix = new ArrayList<>();//echart 横轴数据
        Double echartMin = null;

        List optionDatas = new ArrayList();//echart 需要的各个线
        String curLotNo = "";
        //将数据库中的原数据拆分出echart 需要的各个线
        for(Map<String, String> data : dataList){
            //拆分横轴数据
            if(!curLotNo.equals(MapUtil.getString(data, "lotNo"))){
                curLotNo = MapUtil.getString(data, "lotNo");
                xAsix.add(curLotNo);
            }

            String lineDataBurr = MapUtil.getString(data, "burr_f");
            String lineDataPin = MapUtil.getString(data, "pin_f1");
            String lineDataPinf2f = MapUtil.getString(data, "pin_f1_f2");
            String lineDataPins = MapUtil.getString(data, "pin_s1");
            //记录所有数据中最小的值,echart使用
            echartMin = getMin(echartMin, lineDataBurr);
            echartMin = getMin(echartMin, lineDataPin);
            echartMin = getMin(echartMin, lineDataPinf2f);
            echartMin = getMin(echartMin, lineDataPins);

            //拆分1-5数据
            if ("1".equals(MapUtil.getString(data, "serialCounter"))) {
                if(StringUtil.isNotEmpty(lineDataBurr)){
                    data1Map.put(curLotNo, Double.parseDouble(lineDataBurr));
                }
                if(StringUtil.isNotEmpty(lineDataPin)){
                    data2Map.put(curLotNo, Double.parseDouble(lineDataPin));
                }
                if(StringUtil.isNotEmpty(lineDataPinf2f)){
                    data3Map.put(curLotNo, Double.parseDouble(lineDataPinf2f));
                }
                if(StringUtil.isNotEmpty(lineDataPins)){
                    data4Map.put(curLotNo, Double.parseDouble(lineDataPins));
                }
            }
        }

        //缺失数据让线断开
        for (String asix : xAsix) {
            data1.add(MapUtil.getDouble(data1Map, asix));
            data2.add(MapUtil.getDouble(data2Map, asix));
            data3.add(MapUtil.getDouble(data3Map, asix));
            data4.add(MapUtil.getDouble(data4Map, asix));
        }

        optionDatas.add(xAsix);
        //将数据的分配name并放入返回对象中
        List<Map<String, Object>> lines = new ArrayList<>();
        Map<String, Object> lineObj1 = new HashMap<>();
        lineObj1.put("name","1-毛刺");
        lineObj1.put("type","line");
        lineObj1.put("data", data1);
        lines.add(lineObj1);
        Map<String, Object> lineObj2 = new HashMap<>();
        lineObj2.put("name","1-1:1PIN");
        lineObj2.put("type","line");
        lineObj2.put("data", data2);
        lines.add(lineObj2);

        Map<String, Object> minLimitLine = new HashMap<>();
//        minLimitLine.put("name","下限");
        minLimitLine.put("type","line");
        minLimitLine.put("data", new ArrayList<>());
        lines.add(minLimitLine);
        Map<String, Object> maxLimitLine = new HashMap<>();
//        maxLimitLine.put("name","上限");
        maxLimitLine.put("type","line");
        maxLimitLine.put("data", new ArrayList<>());
        lines.add(maxLimitLine);

        Map<String, Object> lineObj3 = new HashMap<>();
        lineObj3.put("name","1-1:1PIN-2PIN");
        lineObj3.put("type","line");
        lineObj3.put("data", data3);
        lines.add(lineObj3);
        Map<String, Object> lineObj4 = new HashMap<>();
        lineObj4.put("name","1-2:1PIN");
        lineObj4.put("type","line");
        lineObj4.put("data", data4);
        lines.add(lineObj4);

        optionDatas.add(lines);

        //echart Y轴最小值
        Map min = new HashMap();
        if(0== echartMin){
            echartMin = -0.1;
        } else {
            echartMin = echartMin * 0.98;//为了数据线不贴在x轴上
        }
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        min.put("min", df.format(echartMin));
        optionDatas.add(min);

        return optionDatas;
    }

    private Double getMin(Double echartMin, String dataDouble){
        if(StringUtil.isNotEmpty(dataDouble)){
            Double dataD = Double.parseDouble(dataDouble);
            if(echartMin==null){
                echartMin = dataD;
            } else {
                echartMin = echartMin > dataD ? dataD : echartMin;
            }
            return echartMin;
        } else {
            return echartMin;
        }
    }
}
