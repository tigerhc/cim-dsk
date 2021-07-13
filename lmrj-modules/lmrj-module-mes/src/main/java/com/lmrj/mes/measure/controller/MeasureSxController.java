package com.lmrj.mes.measure.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.mes.measure.mapper.MeasureSxMapper;
import com.lmrj.mes.measure.service.MeasureSxService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.collection.MapUtil;
import com.lmrj.util.lang.StringUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.*;

@RestController
@RequestMapping("ms/measuresx")
@ViewPrefix("ms/measuresx")
@RequiresPathPermission("ms/measuresx")
@LogAspectj(title = "ms/measuresx")
public class MeasureSxController {

    @Autowired
    private MeasureSxService measureSxService;
    @Autowired
    private MeasureSxMapper measureSxMapper;

    @RequestMapping(value = "/productionName",method = {RequestMethod.GET, RequestMethod.POST})
    public List<Map<String, String>> productionName(@RequestParam String type, @RequestParam String lineNo){
        if(StringUtil.isEmpty(lineNo)){
            return new ArrayList<>();
        }
        return  measureSxService.findProductionNo(type, lineNo);
    }

    /**
     * @param productionName:具体的如6812M
     * @param number
     * @param startDate:开始时间
     * @param endDate:结束时间
     * @param type: LF 检查
     * @param local: abcd
     * @return
     */
    @RequestMapping(value = "/findSxNumber",method = {RequestMethod.GET, RequestMethod.POST})
    public List findSxNumber(@RequestParam String productionName,@RequestParam String number,@RequestParam String startDate, @RequestParam String endDate, @RequestParam String type, @RequestParam String local, @RequestParam String lineNo){
        try {
            if (lineNo.contains("SX")) {
                return measureSxService.findSxNumber(productionName, number, startDate, endDate, type, local);
            } else if (lineNo.contains("SIM")) {
                return measureSxService.findSimNumber(productionName, number, startDate, endDate, type, local);
            } else {
                if(StringUtil.isEmpty(local)){
                    return measureSxService.findGiNumberAll(productionName, lineNo, startDate, endDate, type);
                } else {
                    return measureSxService.findGiNumber(productionName, lineNo, startDate, endDate, type, local);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/findSxNumberExport",method = {RequestMethod.GET, RequestMethod.POST})
    public Response findSxNumberExport(@RequestParam String productionName,@RequestParam String number,@RequestParam String startDate, @RequestParam String endDate, @RequestParam String type, @RequestParam String lineNo){
        try {
            String title = "量测分离";
            Response res = Response.ok("导出成功");

            List<ExcelExportEntity> keyList = new LinkedList<>();
            List<Map<String, String>> dataList = new LinkedList<>();
            if("SX".equals(lineNo)){
                keyList = getSxKeyList();
                dataList = getSxDataList(productionName, startDate, endDate, type);
            } else if ("SIM".equals(lineNo)){
                keyList = getSimKeyList();
                dataList = getSimDataList(productionName, startDate, endDate, type);
            } else if ("5GI".equals(lineNo) || "6GI".equals(lineNo)){
                 keyList = getGiKeyList();
                dataList = getGiDataList(productionName, startDate, endDate, type, lineNo);
            }

            Workbook book = MyExcelExportUtil.exportExcel(new ExportParams("量测分离"+productionName,"量测详细信息"),keyList,dataList, 4);
            FileOutputStream fos = new FileOutputStream("D:/ExcelExportForMap.xls");
            book.write(fos);
            fos.close();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            book.write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtil.bytesToHexString2(bytes);
            title = title + "-" + DateUtil.getDateTime();
            res.put("bytes", bytesRes);
            res.put("title", title);
            return res;
        } catch (Exception var16) {
            var16.printStackTrace();
            return Response.error(999998, "导出失败");
        }
    }

    private List<ExcelExportEntity> getSxKeyList(){
        List<ExcelExportEntity> keyList = new LinkedList<>();
        ExcelExportEntity key = new ExcelExportEntity(" ","1");
        keyList.add(key);
        ExcelExportEntity key1 = new ExcelExportEntity("类型","2");
        keyList.add(key1);
        ExcelExportEntity key2 = new ExcelExportEntity("机种名","3");
        keyList.add(key2);
        ExcelExportEntity key3 = new ExcelExportEntity("批量号","4");
        keyList.add(key3);
        ExcelExportEntity key4 = new ExcelExportEntity("串行计数器","5");
        keyList.add(key4);
        ExcelExportEntity key5 = new ExcelExportEntity("时间","6");
        keyList.add(key5);
        ExcelExportEntity key6 = new ExcelExportEntity("1:A","7");
        keyList.add(key6);
        ExcelExportEntity key7 = new ExcelExportEntity("1:B","8");
        keyList.add(key7);
        ExcelExportEntity key8 = new ExcelExportEntity("1:C","9");
        keyList.add(key8);
        ExcelExportEntity key9 = new ExcelExportEntity("1:D","10");
        keyList.add(key9);
        ExcelExportEntity key10 = new ExcelExportEntity("2:A","11");
        keyList.add(key10);
        ExcelExportEntity key11 = new ExcelExportEntity("2:B","12");
        keyList.add(key11);
        ExcelExportEntity key12 = new ExcelExportEntity("2:C","13");
        keyList.add(key12);
        ExcelExportEntity key13 = new ExcelExportEntity("2:D","14");
        keyList.add(key13);
        return keyList;
    }

    private List<Map<String, String>> getSxDataList(String productionName, String startDate, String endDate, String type){
        String cMin = "";
        if(!"LF".equals(type)){
            cMin = "0.02";
        } else {
            cMin = "0.07";
        }
        String number = "1";
        List<Map<String, String>> result = measureSxMapper.findSxNumber(productionName, number, startDate, endDate, type);
        number = "2";
        List<Map<String, String>> result2 = measureSxMapper.findSxNumber(productionName, number, startDate, endDate, type);
        int size = result.size()>result2.size() ? result2.size():result.size();

        List<Map<String, String>> dataList = new LinkedList<>();
        Map<String, String> LimitData = new HashMap<>() ;//%4行 上限
        LimitData.put("1","上限");
        LimitData.put("2",type);
        LimitData.put("3",productionName);
        LimitData.put("4","");
        LimitData.put("5","");
        LimitData.put("6","");//时间
        LimitData.put("7","14.3");
        LimitData.put("8","1");
        LimitData.put("9","0.13");
        LimitData.put("10","17");
        LimitData.put("11","14.3");
        LimitData.put("12","1");
        LimitData.put("13","0.13");
        LimitData.put("14","17");
        dataList.add(LimitData);
        LimitData = new HashMap<>() ;//%3行 下限
        LimitData.put("1","下限");
        LimitData.put("2",type);
        LimitData.put("3",productionName);
        LimitData.put("4","");
        LimitData.put("5","");
        LimitData.put("6","");//时间
        LimitData.put("7","13.9");
        LimitData.put("8","0.4");
        LimitData.put("9","0.02");
        LimitData.put("10","0");
        LimitData.put("11","13.9");
        LimitData.put("12","0.4");
        LimitData.put("13",cMin);
        LimitData.put("14","0");
        dataList.add(LimitData);
        for (int i = 0; i <size ; i++) {
            Map<String, String> data = new HashMap<>() ;
            data.put("2",type);
            data.put("3",productionName);
            data.put("4",result.get(i).get("lotNo"));
            data.put("5","1");
            data.put("6",result.get(i).get("meaDate"));
            data.put("7",result.get(i).get("a1"));
            data.put("8",result.get(i).get("b1"));
            data.put("9",result.get(i).get("c1"));
            data.put("10",result.get(i).get("d1"));
            data.put("11",result.get(i).get("a2"));
            data.put("12",result.get(i).get("b2"));
            data.put("13",result.get(i).get("c2"));
            data.put("14",result.get(i).get("d2"));
            dataList.add(data);
            data = new HashMap<>() ;
            data.put("2",type);
            data.put("3",productionName);
            data.put("4",result.get(i).get("lotNo"));
            data.put("5","2");
            data.put("6",result2.get(i).get("meaDate"));
            data.put("7",result2.get(i).get("a1"));
            data.put("8",result2.get(i).get("b1"));
            data.put("9",result2.get(i).get("c1"));
            data.put("10",result2.get(i).get("d1"));
            data.put("11",result2.get(i).get("a2"));
            data.put("12",result2.get(i).get("b2"));
            data.put("13",result2.get(i).get("c2"));
            data.put("14",result2.get(i).get("d2"));
            dataList.add(data);
        }
        return dataList;
    }

    private List<ExcelExportEntity> getSimKeyList(){
        List<ExcelExportEntity> keyList = new LinkedList<>();
        keyList.add(new ExcelExportEntity(" ","1"));
        keyList.add(new ExcelExportEntity("类型","2"));
        keyList.add(new ExcelExportEntity("机种名","3"));
        keyList.add(new ExcelExportEntity("批量号","4"));
        keyList.add(new ExcelExportEntity("串行计数器","5"));
        keyList.add(new ExcelExportEntity("时间","6"));
        keyList.add(new ExcelExportEntity("1:A","7"));
        keyList.add(new ExcelExportEntity("2:B","8"));
        keyList.add(new ExcelExportEntity("3:C1","9"));
        keyList.add(new ExcelExportEntity("3:C2","10"));
        keyList.add(new ExcelExportEntity("3:C3","11"));
        keyList.add(new ExcelExportEntity("3:C4","12"));
        keyList.add(new ExcelExportEntity("3:C5","13"));
        keyList.add(new ExcelExportEntity("3:C6","14"));
        keyList.add(new ExcelExportEntity("3:C7","15"));
        keyList.add(new ExcelExportEntity("3:C8","16"));
        keyList.add(new ExcelExportEntity("3:C9","17"));
        keyList.add(new ExcelExportEntity("3:C10","18"));
        keyList.add(new ExcelExportEntity("3:C11","19"));
        keyList.add(new ExcelExportEntity("3:C12","20"));
        keyList.add(new ExcelExportEntity("3:C13","21"));
        keyList.add(new ExcelExportEntity("3:C14","22"));
        keyList.add(new ExcelExportEntity("3:C15","23"));
        keyList.add(new ExcelExportEntity("3:C16","24"));
        keyList.add(new ExcelExportEntity("3:C17","25"));
        keyList.add(new ExcelExportEntity("3:C19","26"));
        keyList.add(new ExcelExportEntity("3:C20","27"));
        keyList.add(new ExcelExportEntity("3:C21","28"));
        keyList.add(new ExcelExportEntity("3:C23","29"));
        keyList.add(new ExcelExportEntity("3:C24","30"));
        keyList.add(new ExcelExportEntity("3:C26","31"));
        keyList.add(new ExcelExportEntity("3:C28","32"));
        keyList.add(new ExcelExportEntity("3:C30","33"));
        keyList.add(new ExcelExportEntity("3:C31","34"));
        keyList.add(new ExcelExportEntity("3:C35","35"));
        keyList.add(new ExcelExportEntity("3:C37","36"));
        keyList.add(new ExcelExportEntity("3:C40","37"));
        return keyList;
    }

    private List<Map<String, String>> getSimDataList(String productionName, String startDate, String endDate, String type){
        List<Map<String, String>> result = measureSxMapper.findSimNumber(productionName, startDate, endDate, type);
        List<Map<String, String>> limitData = measureSxMapper.findSimLimit();

        List<Map<String, String>> dataList = new LinkedList<>();
        //上下限
        if(limitData.size()>1){
            Map<String, String> LimitData = new HashMap<>() ;//%4行 上限
            LimitData.put("1","上限");
            LimitData.put("2",type);
            LimitData.put("3",productionName);
            LimitData.put("4","");
            LimitData.put("5","");
            LimitData.put("6","");//时间
            LimitData.put("7", MapUtil.getString(limitData.get(1), "a"));
            LimitData.put("8", MapUtil.getString(limitData.get(1), "b"));
            LimitData.put("9", MapUtil.getString(limitData.get(1), "c1"));
            LimitData.put("10", MapUtil.getString(limitData.get(1), "c2"));
            LimitData.put("11", MapUtil.getString(limitData.get(1), "c3"));
            LimitData.put("12", MapUtil.getString(limitData.get(1), "c4"));
            LimitData.put("13", MapUtil.getString(limitData.get(1), "c5"));
            LimitData.put("14", MapUtil.getString(limitData.get(1), "c6"));
            LimitData.put("15", MapUtil.getString(limitData.get(1), "c7"));
            LimitData.put("16", MapUtil.getString(limitData.get(1), "c8"));
            LimitData.put("17", MapUtil.getString(limitData.get(1), "c9"));
            LimitData.put("18", MapUtil.getString(limitData.get(1), "c10"));
            LimitData.put("19", MapUtil.getString(limitData.get(1), "c11"));
            LimitData.put("20", MapUtil.getString(limitData.get(1), "c12"));
            LimitData.put("21", MapUtil.getString(limitData.get(1), "c13"));
            LimitData.put("22", MapUtil.getString(limitData.get(1), "c14"));
            LimitData.put("23", MapUtil.getString(limitData.get(1), "c15"));
            LimitData.put("24", MapUtil.getString(limitData.get(1), "c16"));
            LimitData.put("25", MapUtil.getString(limitData.get(1), "c17"));
            LimitData.put("26", MapUtil.getString(limitData.get(1), "c19"));
            LimitData.put("27", MapUtil.getString(limitData.get(1), "c20"));
            LimitData.put("28", MapUtil.getString(limitData.get(1), "c21"));
            LimitData.put("29", MapUtil.getString(limitData.get(1), "c23"));
            LimitData.put("30", MapUtil.getString(limitData.get(1), "c24"));
            LimitData.put("31", MapUtil.getString(limitData.get(1), "c26"));
            LimitData.put("32", MapUtil.getString(limitData.get(1), "c28"));
            LimitData.put("33", MapUtil.getString(limitData.get(1), "c30"));
            LimitData.put("34", MapUtil.getString(limitData.get(1), "c31"));
            LimitData.put("35", MapUtil.getString(limitData.get(1), "c35"));
            LimitData.put("36", MapUtil.getString(limitData.get(1), "c37"));
            LimitData.put("37", MapUtil.getString(limitData.get(1), "c40"));
            dataList.add(LimitData);
            LimitData = new HashMap<>() ;//%3行 下限
            LimitData.put("1","下限");
            LimitData.put("2",type);
            LimitData.put("3",productionName);
            LimitData.put("4","");
            LimitData.put("5","");
            LimitData.put("6","");//时间
            LimitData.put("7", MapUtil.getString(limitData.get(0), "a"));
            LimitData.put("8", MapUtil.getString(limitData.get(0), "b"));
            LimitData.put("9", MapUtil.getString(limitData.get(0), "c1"));
            LimitData.put("10", MapUtil.getString(limitData.get(0), "c2"));
            LimitData.put("11", MapUtil.getString(limitData.get(0), "c3"));
            LimitData.put("12", MapUtil.getString(limitData.get(0), "c4"));
            LimitData.put("13", MapUtil.getString(limitData.get(0), "c5"));
            LimitData.put("14", MapUtil.getString(limitData.get(0), "c6"));
            LimitData.put("15", MapUtil.getString(limitData.get(0), "c7"));
            LimitData.put("16", MapUtil.getString(limitData.get(0), "c8"));
            LimitData.put("17", MapUtil.getString(limitData.get(0), "c9"));
            LimitData.put("18", MapUtil.getString(limitData.get(0), "c10"));
            LimitData.put("19", MapUtil.getString(limitData.get(0), "c11"));
            LimitData.put("20", MapUtil.getString(limitData.get(0), "c12"));
            LimitData.put("21", MapUtil.getString(limitData.get(0), "c13"));
            LimitData.put("22", MapUtil.getString(limitData.get(0), "c14"));
            LimitData.put("23", MapUtil.getString(limitData.get(0), "c15"));
            LimitData.put("24", MapUtil.getString(limitData.get(0), "c16"));
            LimitData.put("25", MapUtil.getString(limitData.get(0), "c17"));
            LimitData.put("26", MapUtil.getString(limitData.get(0), "c19"));
            LimitData.put("27", MapUtil.getString(limitData.get(0), "c20"));
            LimitData.put("28", MapUtil.getString(limitData.get(0), "c21"));
            LimitData.put("29", MapUtil.getString(limitData.get(0), "c23"));
            LimitData.put("30", MapUtil.getString(limitData.get(0), "c24"));
            LimitData.put("31", MapUtil.getString(limitData.get(0), "c26"));
            LimitData.put("32", MapUtil.getString(limitData.get(0), "c28"));
            LimitData.put("33", MapUtil.getString(limitData.get(0), "c30"));
            LimitData.put("34", MapUtil.getString(limitData.get(0), "c31"));
            LimitData.put("35", MapUtil.getString(limitData.get(0), "c35"));
            LimitData.put("36", MapUtil.getString(limitData.get(0), "c37"));
            LimitData.put("37", MapUtil.getString(limitData.get(0), "c40"));
            dataList.add(LimitData);
        }

        for (Map<String, String> dataItem : result) {
            Map<String, String> data = new HashMap<>();
            data.put("2",type);
            data.put("3",productionName);
            data.put("4", MapUtil.getString(dataItem, "lotNo"));
            data.put("5", MapUtil.getString(dataItem, "serialCounter"));
            data.put("6", MapUtil.getString(dataItem, "meaDate"));
            data.put("7", MapUtil.getString(dataItem, "a"));
            data.put("8", MapUtil.getString(dataItem, "b"));
            data.put("9", MapUtil.getString(dataItem, "c1"));
            data.put("10", MapUtil.getString(dataItem, "c2"));
            data.put("11", MapUtil.getString(dataItem, "c3"));
            data.put("12", MapUtil.getString(dataItem, "c4"));
            data.put("13", MapUtil.getString(dataItem, "c5"));
            data.put("14", MapUtil.getString(dataItem, "c6"));
            data.put("15", MapUtil.getString(dataItem, "c7"));
            data.put("16", MapUtil.getString(dataItem, "c8"));
            data.put("17", MapUtil.getString(dataItem, "c9"));
            data.put("18", MapUtil.getString(dataItem, "c10"));
            data.put("19", MapUtil.getString(dataItem, "c11"));
            data.put("20", MapUtil.getString(dataItem, "c12"));
            data.put("21", MapUtil.getString(dataItem, "c13"));
            data.put("22", MapUtil.getString(dataItem, "c14"));
            data.put("23", MapUtil.getString(dataItem, "c15"));
            data.put("24", MapUtil.getString(dataItem, "c16"));
            data.put("25", MapUtil.getString(dataItem, "c17"));
            data.put("26", MapUtil.getString(dataItem, "c19"));
            data.put("27", MapUtil.getString(dataItem, "c20"));
            data.put("28", MapUtil.getString(dataItem, "c21"));
            data.put("29", MapUtil.getString(dataItem, "c23"));
            data.put("30", MapUtil.getString(dataItem, "c24"));
            data.put("31", MapUtil.getString(dataItem, "c26"));
            data.put("32", MapUtil.getString(dataItem, "c28"));
            data.put("33", MapUtil.getString(dataItem, "c30"));
            data.put("34", MapUtil.getString(dataItem, "c31"));
            data.put("35", MapUtil.getString(dataItem, "c35"));
            data.put("36", MapUtil.getString(dataItem, "c37"));
            data.put("37", MapUtil.getString(dataItem, "c40"));
            dataList.add(data);
        }
        return dataList;
    }

    private List<ExcelExportEntity> getGiKeyList(){
        List<ExcelExportEntity> keyList = new LinkedList<>();
        ExcelExportEntity key = new ExcelExportEntity(" ","1");
        keyList.add(key);
        ExcelExportEntity key1 = new ExcelExportEntity("类型","2");
        keyList.add(key1);
        ExcelExportEntity key2 = new ExcelExportEntity("机种名","3");
        keyList.add(key2);
        ExcelExportEntity key3 = new ExcelExportEntity("批量号","4");
        keyList.add(key3);
        ExcelExportEntity key4 = new ExcelExportEntity("串行计数器","5");
        keyList.add(key4);
        ExcelExportEntity key5 = new ExcelExportEntity("时间","6");
        keyList.add(key5);
        ExcelExportEntity key6 = new ExcelExportEntity("毛刺","7");
        keyList.add(key6);
        ExcelExportEntity key7 = new ExcelExportEntity("正面弯曲1(1:1PIN)","8");
        keyList.add(key7);
        ExcelExportEntity key8 = new ExcelExportEntity("正面弯曲2(1:2PIN)","9");
        keyList.add(key8);
        ExcelExportEntity key9 = new ExcelExportEntity("正面弯曲3(1:3PIN)","10");
        keyList.add(key9);
        ExcelExportEntity key10 = new ExcelExportEntity("正面弯曲4(1:4PIN)","11");
        keyList.add(key10);
        ExcelExportEntity key11 = new ExcelExportEntity("正面弯曲5(1:5PIN)","12");
        keyList.add(key11);
        ExcelExportEntity key12 = new ExcelExportEntity("正面弯曲6(1:6PIN)","13");
        keyList.add(key12);
        ExcelExportEntity key13 = new ExcelExportEntity("横筋间距1_2(1:1PIN-2PIN)","14");
        keyList.add(key13);
        ExcelExportEntity key14 = new ExcelExportEntity("横筋间距2_3(1:2PIN-3PIN)","15");
        keyList.add(key14);
        ExcelExportEntity key15 = new ExcelExportEntity("横筋间距3_4(1:3PIN-4PIN)","16");
        keyList.add(key15);
        ExcelExportEntity key16 = new ExcelExportEntity("横筋间距4_5(1:4PIN-5PIN)","17");
        keyList.add(key16);
        ExcelExportEntity key17 = new ExcelExportEntity("横筋间距5_6(1:5PIN-6PIN)","18");
        keyList.add(key17);
        ExcelExportEntity key18 = new ExcelExportEntity("侧面弯曲1(2:1PIN)","19");
        keyList.add(key18);
        ExcelExportEntity key19 = new ExcelExportEntity("侧面弯曲2(2:2PIN)","20");
        keyList.add(key19);
        ExcelExportEntity key20 = new ExcelExportEntity("侧面弯曲3(2:3PIN)","21");
        keyList.add(key20);
        ExcelExportEntity key21 = new ExcelExportEntity("侧面弯曲4(2:4PIN)","22");
        keyList.add(key21);
        ExcelExportEntity key22 = new ExcelExportEntity("侧面弯曲5(2:5PIN)","23");
        keyList.add(key22);
        ExcelExportEntity key23 = new ExcelExportEntity("侧面弯曲6(2:6PIN)","24");
        keyList.add(key23);
        return keyList;
    }

    private List<Map<String, String>> getGiDataList(String productionName, String startDate, String endDate, String type, String lineNo){
        List<Map<String, String>> result = measureSxMapper.findGiNumber(productionName, startDate, endDate, type);
        List<Map<String, String>> limitData = measureSxMapper.findGiLimit(lineNo);

        List<Map<String, String>> dataList = new LinkedList<>();
        //上下限
        if(limitData.size()>1){
            Map<String, String> LimitData = new HashMap<>() ;//%4行 上限
            LimitData.put("1","上限");
            LimitData.put("2",type);
            LimitData.put("3",productionName);
            LimitData.put("4","");
            LimitData.put("5","");
            LimitData.put("6","");//时间
            LimitData.put("7", MapUtil.getString(limitData.get(1), "burr_f"));
            LimitData.put("8", MapUtil.getString(limitData.get(1), "pin_f1"));
            LimitData.put("9", MapUtil.getString(limitData.get(1), "pin_f2"));
            LimitData.put("10", MapUtil.getString(limitData.get(1), "pin_f3"));
            LimitData.put("11", MapUtil.getString(limitData.get(1), "pin_f4"));
            LimitData.put("12", MapUtil.getString(limitData.get(1), "pin_f5"));
            LimitData.put("13", MapUtil.getString(limitData.get(1), "pin_f6"));
            LimitData.put("14", MapUtil.getString(limitData.get(1), "pin_f1_f2"));
            LimitData.put("15", MapUtil.getString(limitData.get(1), "pin_f2_f3"));
            LimitData.put("16", MapUtil.getString(limitData.get(1), "pin_f3_f4"));
            LimitData.put("17", MapUtil.getString(limitData.get(1), "pin_f4_f5"));
            LimitData.put("18", MapUtil.getString(limitData.get(1), "pin_f5_f6"));
            LimitData.put("19", MapUtil.getString(limitData.get(1), "pin_s1"));
            LimitData.put("20", MapUtil.getString(limitData.get(1), "pin_s2"));
            LimitData.put("21", MapUtil.getString(limitData.get(1), "pin_s3"));
            LimitData.put("22", MapUtil.getString(limitData.get(1), "pin_s4"));
            LimitData.put("23", MapUtil.getString(limitData.get(1), "pin_s5"));
            LimitData.put("24", MapUtil.getString(limitData.get(1), "pin_s6"));
            dataList.add(LimitData);
            LimitData = new HashMap<>() ;//%3行 下限
            LimitData.put("1","下限");
            LimitData.put("2",type);
            LimitData.put("3",productionName);
            LimitData.put("4","");
            LimitData.put("5","");
            LimitData.put("6","");//时间
            LimitData.put("7", MapUtil.getString(limitData.get(0), "burr_f"));
            LimitData.put("8", MapUtil.getString(limitData.get(0), "pin_f1"));
            LimitData.put("9", MapUtil.getString(limitData.get(0), "pin_f2"));
            LimitData.put("10", MapUtil.getString(limitData.get(0), "pin_f3"));
            LimitData.put("11", MapUtil.getString(limitData.get(0), "pin_f4"));
            LimitData.put("12", MapUtil.getString(limitData.get(0), "pin_f5"));
            LimitData.put("13", MapUtil.getString(limitData.get(0), "pin_f6"));
            LimitData.put("14", MapUtil.getString(limitData.get(0), "pin_f1_f2"));
            LimitData.put("15", MapUtil.getString(limitData.get(0), "pin_f2_f3"));
            LimitData.put("16", MapUtil.getString(limitData.get(0), "pin_f3_f4"));
            LimitData.put("17", MapUtil.getString(limitData.get(0), "pin_f4_f5"));
            LimitData.put("18", MapUtil.getString(limitData.get(0), "pin_f5_f6"));
            LimitData.put("19", MapUtil.getString(limitData.get(0), "pin_s1"));
            LimitData.put("20", MapUtil.getString(limitData.get(0), "pin_s2"));
            LimitData.put("21", MapUtil.getString(limitData.get(0), "pin_s3"));
            LimitData.put("22", MapUtil.getString(limitData.get(0), "pin_s4"));
            LimitData.put("23", MapUtil.getString(limitData.get(0), "pin_s5"));
            LimitData.put("24", MapUtil.getString(limitData.get(0), "pin_s6"));
            dataList.add(LimitData);
        }

        for (Map<String, String> dataItem : result) {
            Map<String, String> data = new HashMap<>();
            data.put("2",type);
            data.put("3",productionName);
            data.put("4", MapUtil.getString(dataItem, "lotNo"));
            data.put("5", MapUtil.getString(dataItem, "serialCounter"));
            data.put("6", MapUtil.getString(dataItem, "meaDate"));
            data.put("7", MapUtil.getString(dataItem, "burr_f"));
            data.put("8", MapUtil.getString(dataItem, "pin_f1"));
            data.put("9", MapUtil.getString(dataItem, "pin_f2"));
            data.put("10", MapUtil.getString(dataItem, "pin_f3"));
            data.put("11", MapUtil.getString(dataItem, "pin_f4"));
            data.put("12", MapUtil.getString(dataItem, "pin_f5"));
            data.put("13", MapUtil.getString(dataItem, "pin_f6"));
            data.put("14", MapUtil.getString(dataItem, "pin_f1_f2"));
            data.put("15", MapUtil.getString(dataItem, "pin_f2_f3"));
            data.put("16", MapUtil.getString(dataItem, "pin_f3_f4"));
            data.put("17", MapUtil.getString(dataItem, "pin_f4_f5"));
            data.put("18", MapUtil.getString(dataItem, "pin_f5_f6"));
            data.put("19", MapUtil.getString(dataItem, "pin_s1"));
            data.put("20", MapUtil.getString(dataItem, "pin_s2"));
            data.put("21", MapUtil.getString(dataItem, "pin_s3"));
            data.put("22", MapUtil.getString(dataItem, "pin_s4"));
            data.put("23", MapUtil.getString(dataItem, "pin_s5"));
            data.put("24", MapUtil.getString(dataItem, "pin_s6"));
            dataList.add(data);
        }
        return dataList;
    }
}
