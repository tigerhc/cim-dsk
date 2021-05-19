package com.lmrj.mes.kongdong.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.mes.kongdong.entity.MsMeasureKongdong;
import com.lmrj.mes.kongdong.service.IMsMeasureKongdongService;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.mes.kongdong.controller
 * @title: ms_measure_kogndong控制器
 * @description: ms_measure_kogndong控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasurekongdong")
@ViewPrefix("ms/msmeasurekongdong")
@RequiresPathPermission("ms:msmeasurekongdong")
@LogAspectj(title = "ms_measure_kongdong")
public class MsMeasureKongdongController extends BaseCRUDController<MsMeasureKongdong> {
    @Autowired
    private IMsMeasureKongdongService kongdongService;

//    @RequestMapping(value = "saveBeforeKongdong",method = {RequestMethod.GET, RequestMethod.POST})
//    public Response saveBeforeKongdong(@RequestParam("filePath") int index){
//        Response rs = Response.ok();
//        rs.putList("data",kongdongService.saveBeforeFile(index));
//        return rs;
//    }

    @RequestMapping(value = "kongdongChart",method = {RequestMethod.GET, RequestMethod.POST})
    public Response kongdongChart(@RequestParam String productionName, @RequestParam String startDate, @RequestParam String endDate, @RequestParam String lineType){
        Map<String, Object> param = new HashMap<>();
        param.put("productionName", productionName.replace("J.",""));
        param.put("startTime", startDate);
        param.put("endTime", endDate);
        param.put("lineType", lineType);
        Response rs = Response.ok();
        rs.put("data",kongdongService.kongdongChart(param));
        return rs;
    }

    @RequestMapping(value = "kongdongChartExport",method = {RequestMethod.GET, RequestMethod.POST})
    public Response kongdongChartExport(@RequestParam String productionName, @RequestParam String startDate, @RequestParam String endDate, @RequestParam String lineType){
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("productionName", productionName.replace("J.",""));
            param.put("startTime", startDate);
            param.put("endTime", endDate);
            param.put("lineType", lineType);
            Response rs = Response.ok();
            Map<String, Object> data= kongdongService.kongdongChart(param);
//            rs.put("data",kongdongService.kongdongChart(param)); ??为何多查service一次
            rs.put("data",data);
            String title = "量测空洞";
            Response res = Response.ok();
            List<ExcelExportEntity> keyList= new LinkedList<>();
            List<Map<Integer, Object>> dataList = new LinkedList<>();
            List<String> timeList = (List) data.get("timeList");

//            Map<String, Object> data = (Map) result.get("data");
            List<String> legend = (List<String>) data.get("legend");
            ExcelExportEntity key = new ExcelExportEntity("批号",1);
            keyList.add(key);
            //时间
            ExcelExportEntity timeTitle = new ExcelExportEntity("时间",2);
            keyList.add(timeTitle);
            for (int i = 0; i <legend.size() ; i++) {
                ExcelExportEntity p = new ExcelExportEntity(String.valueOf(legend.get(i)),i+3);
                keyList.add(p);
            }
            List xAxis = (List) data.get("xAxis");
            for (int i = 0; i <xAxis.size() ; i++) {
                Map<Integer, Object> data1 = new HashMap<>() ;
                data1.put(1,xAxis.get(i));
                List series = (List) data.get("series");
                for (int j = 0; j <series.size() ; j++) {
                    Map ele = (Map) series.get(j);
                    List value1 = (List) ele.get("data");
                    data1.put(j+3,value1.get(i));
                }
                //添加时间
                if(timeList.size()>i){
                    data1.put(2, timeList.get(i));
                } else {
                    data1.put(2, "");
                }
                dataList.add(data1);
            }

            Workbook book = ExcelExportUtil.exportExcel(new ExportParams("量测空洞"+productionName.replace("J.",""),"量测详细信息"),keyList,dataList);
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

    @RequestMapping(value = "/kongDongBar", method = {RequestMethod.GET, RequestMethod.POST})
    public Response kongDongBar(@RequestParam String productionName, @RequestParam String lotNo, @RequestParam String lineType) {
        Map<String, Object> param = new HashMap<>();
        param.put("productionName", productionName.replace("J.", ""));
        param.put("lotNo", lotNo);
        param.put("lineType", lineType);
        Response rs = Response.ok();
        rs.put("kongdong", kongdongService.kongDongBar(param));
        return rs;
    }

    @RequestMapping(value = "getPositionSelect", method = {RequestMethod.GET, RequestMethod.POST})
    public Response getPositionSelect(@RequestParam String productionName) {
        Response rs = Response.ok();
        rs.putList("positionList", kongdongService.getPositionSelect(productionName));
        return rs;
    }

    @RequestMapping(value = "chkDataDefect", method = {RequestMethod.GET, RequestMethod.POST})
    public Response chkDataDefect(@RequestParam String startTime, @RequestParam String endTime) {
        Map<String, Object> chkParam = new HashMap<>();
        chkParam.put("startTime",startTime);
        chkParam.put("endTime",endTime);
        Response rs = Response.ok();
        rs.put("chkMsg",kongdongService.chkDataDefect(chkParam));
        return rs;
    }
}
