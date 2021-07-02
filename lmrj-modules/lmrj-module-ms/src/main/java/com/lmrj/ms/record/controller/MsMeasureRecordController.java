package com.lmrj.ms.record.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSON;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.ms.record.entity.MsMeasureRecord;
import com.lmrj.ms.record.entity.MsMeasureRecordDetail;
import com.lmrj.ms.record.service.IMsMeasureRecordService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.collection.MapUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.record.controller
 * @title: ms_measure_record控制器
 * @description: ms_measure_record控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:36:32
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasurerecord")
@ViewPrefix("ms/msmeasurerecord")
@RequiresPathPermission("ms:msmeasurerecord")
@LogAspectj(title = "ms_measure_record")
public class MsMeasureRecordController extends BaseCRUDController<MsMeasureRecord> {

    @Autowired
    IMsMeasureRecordService msMeasureRecordService;
    @Autowired
    private IFabEquipmentService fabEquipmentService;

    /**
     * 在返回list数据之前编辑数据
     *
     * @param pagejson
     */
    @Override
    public void afterPage(PageResponse<MsMeasureRecord> pagejson, HttpServletRequest request, HttpServletResponse response) {
        List<MsMeasureRecord> list = pagejson.getResults();
        for(MsMeasureRecord msMeasureRecord: list){

            if(msMeasureRecord.getEqpId() != null){
                FabEquipment fabEquipment = fabEquipmentService.findEqpByCode(msMeasureRecord.getEqpId());
                if(fabEquipment != null){
                    msMeasureRecord.setEqpName(fabEquipment.getEqpName());
                }
            }
        }
    }


    @RequestMapping(value = "/clientsave", method = { RequestMethod.GET, RequestMethod.POST })
    public Response insert( HttpServletRequest request,
                            HttpServletResponse response){
        Response res= new DateResponse();
        try {
            String record = request.getReader().readLine();
            MsMeasureRecord msMeasureRecord = JsonUtil.from(record,MsMeasureRecord.class);
            String recordId = StringUtil.randomTimeUUID("MS");
            msMeasureRecord.setRecordId(recordId);
            msMeasureRecord.setApproveResult("Y");
            if(StringUtil.isBlank(msMeasureRecord.getStatus())){
                msMeasureRecord.setStatus("1");
            }
            for (MsMeasureRecordDetail msMeasureRecordDetail : msMeasureRecord.getDetail()) {
                if(msMeasureRecordDetail.getItemResult().contains("N")){
                    msMeasureRecord.setApproveResult("N");
                    break;
                }
            }
            boolean flag = commonService.insert(msMeasureRecord);
            if(flag){
                res = DateResponse.ok(recordId);
                //res.put("record", msMeasureRecord);
                return res;
                //String content = JSON.toJSONString(msMeasureRecord);
                //ServletUtils.printJson(response, content);
            }else{
                return res.error("NG");
                //ServletUtils.printJson(response, "NG");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.error("无法解析");

    }

    @RequestMapping(value = "/rptmsrecordbytime/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void rptMsRecordByTime(@PathVariable String eqpId, @RequestParam String beginTime, @RequestParam String endTime,@RequestParam String productionNo,
                                  HttpServletRequest request, HttpServletResponse response){
        List<Map> maps =  msMeasureRecordService.findDetailBytimeAndPro(beginTime,endTime,eqpId, productionNo);
        String content = JSON.toJSONStringWithDateFormat(DateResponse.ok(maps), JSON.DEFFAULT_DATE_FORMAT);
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/rptmsrecordbytime2/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void rptMsRecordByTime2(@PathVariable String eqpId, @RequestParam String beginTime, @RequestParam String endTime,
                                   HttpServletRequest request, HttpServletResponse response){
        List<Map> maps =  msMeasureRecordService.findDetailBytime(beginTime,endTime,eqpId);
        String content = JSON.toJSONStringWithDateFormat(maps, JSON.DEFFAULT_DATE_FORMAT);
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/msrecordbyfirst/{eqpId}", method = { RequestMethod.GET, RequestMethod.POST })
    public void msrecordbyfirst(@PathVariable String eqpId, @RequestParam String processNo ,@RequestParam String productionNo, @RequestParam String lotNo ,@RequestParam String waferId,
                                  HttpServletRequest request, HttpServletResponse response){
        MsMeasureRecord record =  msMeasureRecordService.findMsrecordbyfirst(eqpId, processNo, productionNo, lotNo,waferId);
        String content = JSON.toJSONStringWithDateFormat(record, JSON.DEFFAULT_DATE_FORMAT);
        ServletUtils.printJson(response, content);
    }

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("量测配置信息", queryable,  propertyPreFilterable,  request,  response);
    }

//    @RequestMapping(value = "/exportDetail", method = { RequestMethod.GET, RequestMethod.POST })
    public Response OldExportDetail(@RequestParam String recordId,HttpServletRequest request, HttpServletResponse response){
        String title = "量测信息详情";
        String title1 = "量测信息";
        Response res = Response.ok("导出成功");

        try {
            List<Map<String, Object>> list = new LinkedList<>();
            List<MsMeasureRecord> records = msMeasureRecordService.findRecordByRecordId(recordId);
            Map<String, Object> map = new HashMap<>();
            map.put("title",new ExportParams(title1, title1, ExcelType.XSSF));
            map.put("entity",MsMeasureRecord.class);
            map.put("data",records);
            list.add(map);
            List<MsMeasureRecordDetail> details= new LinkedList<>();
            if(records.size() != 0){
                for (MsMeasureRecordDetail msMeasureRecordDetail: records.get(0).getDetail()) {
                    details.add(msMeasureRecordDetail);
                }
            }
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("title",new ExportParams(title, title, ExcelType.XSSF));
            detailMap.put("entity",MsMeasureRecordDetail.class);
            detailMap.put("data",details);
            list.add(detailMap);
            Workbook book = ExcelExportUtil.exportExcel(list,ExcelType.XSSF);
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

    @RequestMapping(value = "/exportDetail", method = { RequestMethod.GET, RequestMethod.POST })
    public Response exportDetail(@RequestParam String recordId,HttpServletRequest request, HttpServletResponse response){
        String title = "量测信息详情";
        Response res = Response.ok("导出成功");

        try {
            List<MsMeasureRecord> records = msMeasureRecordService.findRecordByRecordId(recordId);
            List<Map<String, String>> dataList = new ArrayList<>();
            Map<String, String> data = null;
            List<ExcelExportEntity> keyList= new LinkedList<>();
            if(records.size() != 0){
                List<MsMeasureRecordDetail> msMeasureRecordDetailList = records.get(0).getDetail();
                for (int i = 0; i  < msMeasureRecordDetailList.size(); i++) {
                    data = new HashMap<>();
                    ExcelExportEntity index = new ExcelExportEntity("#", "index");
                    if (!keyList.contains(index)){
                        keyList.add(index);
                    }
                    data.put("index", i + 1 + "");
                    String itemName = msMeasureRecordDetailList.get(i).getItemName();
                    String itemValue = msMeasureRecordDetailList.get(i).getItemValue();
                    String[] items = itemName.split(",");
                    String[] values = itemValue.split(",");
                    for (int j = 0; j < values.length; j++) {
                        ExcelExportEntity key = new ExcelExportEntity(items[j], items[j]);
                        key.setWidth(20.0D);
                        if (!keyList.contains(key)){
                            keyList.add(key);
                        }
                        data.put(items[j],values[j]);
                    }
                    dataList.add(data);
                }
            }
            Workbook book = ExcelExportUtil.exportExcel(new ExportParams(title, title, ExcelType.XSSF), keyList, dataList);
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

    /**
     * @return 可以直接在echart中展示的重量数据
     */
    @RequestMapping(value = "/weightChart", method = { RequestMethod.GET, RequestMethod.POST })
    public Response weightChart(@RequestParam String productionNo,@RequestParam String lotNo,@RequestParam String detailOption,
                                  @RequestParam String startTime,@RequestParam String endTime){
        if(StringUtil.isEmpty(startTime)&&!StringUtil.isEmpty(endTime)){
            return Response.error("请选择开始时间");
        }
        if(!StringUtil.isEmpty(startTime)&&StringUtil.isEmpty(endTime)){
            return Response.error("请选择结束时间");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("productionNo",productionNo);
        param.put("lotNo", lotNo);
        param.put("detailOption", detailOption);
        if(!StringUtil.isEmpty(startTime)){
            param.put("startTime",startTime);
            param.put("endTime",endTime);
        }
        Response res = Response.ok();
        res.putList("weightList", msMeasureRecordService.findWeight(param));
        return res;
    }

    @RequestMapping(value = "/weightChartExport", method = { RequestMethod.GET, RequestMethod.POST })
    public Response weightChartExport(@RequestParam String productionNo,@RequestParam String lotNo,@RequestParam String detailOption,
                                @RequestParam String startTime,@RequestParam String endTime){
        if(StringUtil.isEmpty(startTime)&&!StringUtil.isEmpty(endTime)){
            return Response.error("请选择开始时间");
        }
        if(!StringUtil.isEmpty(startTime)&&StringUtil.isEmpty(endTime)){
            return Response.error("请选择结束时间");
        }
        try {
            String title = "量测重量";
            Map<String, Object> param = new HashMap<>();
            param.put("productionNo",productionNo);
            param.put("lotNo", lotNo);
            param.put("detailOption", detailOption);
            if(!StringUtil.isEmpty(startTime)){
                param.put("startTime",startTime);
                param.put("endTime",endTime);
            }
            Response res = Response.ok();
            List<ExcelExportEntity> keyList= new LinkedList<>();
            List<Map<String,Object>> dataList = new LinkedList<>();
            List<Map> weight = msMeasureRecordService.findWeight(param);
            ExcelExportEntity key = new ExcelExportEntity(" ","1");
            keyList.add(key);
            ExcelExportEntity key1 = new ExcelExportEntity("批号","2");
            keyList.add(key1);
            ExcelExportEntity key2 = new ExcelExportEntity("时间","3");
            keyList.add(key2);
            ExcelExportEntity key3 = new ExcelExportEntity("平均重量","4");
            keyList.add(key3);
            ExcelExportEntity key4 = new ExcelExportEntity("最大重量","5");
            keyList.add(key4);
            ExcelExportEntity key5 = new ExcelExportEntity("最小重量","6");
            keyList.add(key5);
            //上下限
            Map<String, Object> limitData = new HashMap<>();
            limitData.put("1","上限");
            limitData.put("2","");
            limitData.put("3","");
            limitData.put("4", MapUtil.getString(weight.get(0), "limitMax"));
            limitData.put("5", MapUtil.getString(weight.get(0), "limitMax"));
            limitData.put("6", MapUtil.getString(weight.get(0), "limitMax"));
            dataList.add(limitData);
            limitData = new HashMap<>();
            limitData.put("1","下限");
            limitData.put("2","");
            limitData.put("3","");
            limitData.put("4", MapUtil.getString(weight.get(0), "limitMin"));
            limitData.put("5", MapUtil.getString(weight.get(0), "limitMin"));
            limitData.put("6", MapUtil.getString(weight.get(0), "limitMin"));
            dataList.add(limitData);

            weight.forEach(map -> {
                Map<String, Object> data = new HashMap<>() ;
                data.put("2",map.get("lotNo"));
                data.put("3",map.get("createDate"));
                data.put("4",map.get("avgWeight"));
                data.put("5",map.get("maxWeight"));
                data.put("6",map.get("minWeight"));
                dataList.add(data);
            });

            Workbook book = MyExcelExportUtil.exportExcel(new ExportParams("量测重量"+productionNo,"量测详细信息"),keyList,dataList, 4);
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

    @RequestMapping(value = "/getAllProductionNo", method = { RequestMethod.GET, RequestMethod.POST })
    public Response getAllProductionNo(@RequestParam String lineNo){
        Response res = Response.ok();
        res.putList("productionNoList", msMeasureRecordService.getAllProductionNo(lineNo));
        return res;
    }

    @RequestMapping(value = "/chkWeightData", method = { RequestMethod.GET, RequestMethod.POST })
    public Response chkWeightData(@RequestParam String startTime, @RequestParam String endTime){
        Response res = Response.ok();
        res.putList("weight", msMeasureRecordService.chkWeight(startTime, endTime));
        return res;
    }

    @RequestMapping(value = "/getEqpIdOptions", method = { RequestMethod.GET, RequestMethod.POST })
    public Response getEqpIdOptions(@RequestParam String lineNo){
        Response res = Response.ok();
        res.putList("eqpIdOptions",msMeasureRecordService.getEqpIdOptions(lineNo));
        return res;
    }

    @RequestMapping(value = "/getLineNoOptions", method = { RequestMethod.GET, RequestMethod.POST })
    public Response getLineNoOptions(){
        Response res = Response.ok();
        res.putList("lineNoOptions",msMeasureRecordService.getLineNoOptions());
        return res;
    }
}
