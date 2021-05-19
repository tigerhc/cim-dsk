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
import com.lmrj.util.lang.StringUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, String>> productionName(@RequestParam String type){

        return  measureSxService.findProductionNo(type);

    }

    @RequestMapping(value = "/findSxNumber",method = {RequestMethod.GET, RequestMethod.POST})
    public List findSxNumber(@RequestParam String productionName,@RequestParam String number,@RequestParam String startDate, @RequestParam String endDate, @RequestParam String type, @RequestParam String local){

        return  measureSxService.findSxNumber(productionName,number, startDate,endDate,type,local);


    }

    @RequestMapping(value = "/findSxNumberExport",method = {RequestMethod.GET, RequestMethod.POST})
    public Response findSxNumberExport(@RequestParam String productionName,@RequestParam String number,@RequestParam String startDate, @RequestParam String endDate, @RequestParam String type, @RequestParam String local){
        try {
            number = "1";
            List<Map<String, String>> result = measureSxMapper.findSxNumber(productionName, number, startDate, endDate, type);
            number = "2";
            List<Map<String, String>> result2 = measureSxMapper.findSxNumber(productionName, number, startDate, endDate, type);
            int size = result.size()>result2.size() ? result2.size():result.size();

            String title = "量测分离";
            Response res = Response.ok("导出成功");


            List<ExcelExportEntity> keyList= new LinkedList<>();
            List<Map<String, String>> dataList = new LinkedList<>();
            ExcelExportEntity key = new ExcelExportEntity("类型","1");
            keyList.add(key);
            ExcelExportEntity key2 = new ExcelExportEntity("机种名","2");
            keyList.add(key2);
            ExcelExportEntity key3 = new ExcelExportEntity("批量号","3");
            keyList.add(key3);
            ExcelExportEntity key4 = new ExcelExportEntity("串行计数器","4");
            keyList.add(key4);
            ExcelExportEntity key5 = new ExcelExportEntity("时间","5");
            keyList.add(key5);
            ExcelExportEntity key6 = new ExcelExportEntity("1:A","6");
            keyList.add(key6);
            ExcelExportEntity key7 = new ExcelExportEntity("1:B","7");
            keyList.add(key7);
            ExcelExportEntity key8 = new ExcelExportEntity("1:C","8");
            keyList.add(key8);
            ExcelExportEntity key9 = new ExcelExportEntity("1:D","9");
            keyList.add(key9);
            ExcelExportEntity key10 = new ExcelExportEntity("2:A","10");
            keyList.add(key10);
            ExcelExportEntity key11 = new ExcelExportEntity("2:B","11");
            keyList.add(key11);
            ExcelExportEntity key12 = new ExcelExportEntity("2:C","12");
            keyList.add(key12);
            ExcelExportEntity key13 = new ExcelExportEntity("2:D","13");
            keyList.add(key13);
            for (int i = 0; i <size ; i++) {
                Map<String, String> data = new HashMap<>() ;
                data.put("1",type);
                data.put("2",productionName);
                data.put("3",result.get(i).get("lotNo"));
                data.put("4","1");
                data.put("5",result.get(i).get("meaDate"));
                data.put("6",result.get(i).get("a1"));
                data.put("7",result.get(i).get("b1"));
                data.put("8",result.get(i).get("c1"));
                data.put("9",result.get(i).get("d1"));
                data.put("10",result.get(i).get("a2"));
                data.put("11",result.get(i).get("b2"));
                data.put("12",result.get(i).get("c2"));
                data.put("13",result.get(i).get("d2"));
                dataList.add(data);
                data = new HashMap<>() ;
                data.put("1",type);
                data.put("2",productionName);
                data.put("3",result.get(i).get("lotNo"));
                data.put("4","2");
                data.put("5",result2.get(i).get("meaDate"));
                data.put("6",result2.get(i).get("a1"));
                data.put("7",result2.get(i).get("b1"));
                data.put("8",result2.get(i).get("c1"));
                data.put("9",result2.get(i).get("d1"));
                data.put("10",result2.get(i).get("a2"));
                data.put("11",result2.get(i).get("b2"));
                data.put("12",result2.get(i).get("c2"));
                data.put("13",result2.get(i).get("d2"));
                dataList.add(data);
                data = new HashMap<>() ;//%3行 下限
                data.put("1",type);
                data.put("2",productionName);
                data.put("3",result.get(i).get("lotNo"));
                data.put("4","下限");
                data.put("5","");//时间
                data.put("6","13.9");
                data.put("7","0.4");
                data.put("8","0.07");
                data.put("9","0");
                data.put("10","13.9");
                data.put("11","0.4");
                data.put("12","0.07");
                data.put("13","0");
                dataList.add(data);
                data = new HashMap<>() ;//%4行 上限
                data.put("1",type);
                data.put("2",productionName);
                data.put("3",result.get(i).get("lotNo"));
                data.put("4","上限");
                data.put("5","");//时间
                data.put("6","14.3");
                data.put("7","1");
                data.put("8","0.13");
                data.put("9","17");
                data.put("10","14.3");
                data.put("11","1");
                data.put("12","0.13");
                data.put("13","17");
                dataList.add(data);
            }

            Workbook book = ExcelExportUtil.exportExcel(new ExportParams("量测分离"+productionName,"量测详细信息"),keyList,dataList);
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
}
