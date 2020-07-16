package com.lmrj.ms.record.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
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
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        //String recordId = "MS202006221329295447304157c29f24";
        String recordId = request.getParameter("recordId");
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
}
