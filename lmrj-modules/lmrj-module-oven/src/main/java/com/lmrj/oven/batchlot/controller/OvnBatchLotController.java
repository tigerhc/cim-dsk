package com.lmrj.oven.batchlot.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.lmrj.cim.utils.OfficeUtils;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.PageResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.common.utils.FastJsonUtils;
import com.lmrj.common.utils.ServletUtils;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.oven.batchlot.entity.FabEquipmentOvenStatus;
import com.lmrj.oven.batchlot.entity.OvnBatchLot;
import com.lmrj.oven.batchlot.service.IOvnBatchLotDayService;
import com.lmrj.oven.batchlot.service.IOvnBatchLotService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.*;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.controller
 * @title: ovn_batch_lot控制器
 * @description: ovn_batch_lot控制器
 * @author: zhangweijiang
 * @date: 2019-06-09 08:49:15
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("oven/ovnbatchlot")
@ViewPrefix("modules/OvnBatchLot")
@RequiresPathPermission("OvnBatchLot")
@LogAspectj(title = "ovn_batch_lot")
public class OvnBatchLotController extends BaseCRUDController<OvnBatchLot> {

    @Autowired
    private IOvnBatchLotService ovnBatchLotService;

    @Autowired
    private IFabEquipmentService eqpService;
    @Autowired
    private IOvnBatchLotDayService ovnBatchLotDayService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${dsk.lineNo}")
    private String lineNo;

    @GetMapping("testMsg")
    public void sendMsg(String msg) {
        //rabbitTemplate.convertAndSend("S2C.T.CURE.COMMAND","测试发送");
        OvnBatchLot ovnBatchLot = new OvnBatchLot();
        ovnBatchLot.setId("11111");
        FastJsonUtils.print(ovnBatchLot);
    }

    /**
     * 在异步获取数据之后
     *
     * @param pagejson
     */
    @Override
    public void afterAjaxList(PageResponse<OvnBatchLot> pagejson) {
        List<OvnBatchLot> list = pagejson.getResults();
        for (OvnBatchLot ovnBatchLot : list) {
            if (ovnBatchLot.getOfficeId() != null) {
                Organization office = OfficeUtils.getOffice(ovnBatchLot.getOfficeId());
                if (office != null) {
                    ovnBatchLot.setOfficeName(office.getName());
                }
            }
        }
    }

    /**
     * 在异步获取数据之前
     *
     * @param entity
     */
    public OvnBatchLot afterGet(OvnBatchLot entity) {
        Organization office = OfficeUtils.getOffice(entity.getOfficeId());
        if (office != null) {
            entity.setOfficeName(office.getName());
        }
        if ((entity.getOtherTempsTitle().length() - 1) == entity.getOtherTempsTitle().lastIndexOf(",")) {
            String other = entity.getOtherTempsTitle().substring(0, entity.getOtherTempsTitle().length() - 1);
            entity.setOtherTempsTitle(other);
        }
        if ((entity.getOtherTempsTitle().length() - 1) == entity.getOtherTempsTitle().lastIndexOf(",")) {
            String other = entity.getOtherTempsTitle().substring(0, entity.getOtherTempsTitle().length() - 1);
            entity.setOtherTempsTitle(other);
        }
        String otherTempsTitle = entity.getOtherTempsTitle().replaceAll("当前值", "PV");
        otherTempsTitle = otherTempsTitle.replaceAll("现在值", "PV");
        otherTempsTitle = otherTempsTitle.replaceAll("温区", "");
        entity.setOtherTempsTitle(otherTempsTitle);
        return entity;
    }

    @GetMapping("listEqp")
    public void list(HttpServletRequest request, HttpServletResponse response) {
        List<FabEquipmentOvenStatus> fabEquipmentOvenStatusList = ovnBatchLotService.selectFabStatus("");
        if (CollectionUtils.isEmpty(fabEquipmentOvenStatusList)) {
            FastJsonUtils.print(fabEquipmentOvenStatusList);
        }
        List<Map> fabStatusParams = ovnBatchLotService.selectFabStatusParam(fabEquipmentOvenStatusList);
        fabEquipmentOvenStatusList.forEach(fabEquipmentOvenStatus -> {
            for (Map map : fabStatusParams) {
                if (fabEquipmentOvenStatus.getEqpId().equals(String.valueOf(map.get("EQPID")))) {
                    if ("ptNo".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))) {
                        fabEquipmentOvenStatus.setPtNo(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if ("segNo".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))) {
                        fabEquipmentOvenStatus.setSegNo(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if ("rtime".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))) {
                        fabEquipmentOvenStatus.setRtime(String.valueOf(map.get("PARAMVALUE")));
                    }
                    if ("temp".equalsIgnoreCase(String.valueOf(map.get("PARAMCODE")))) {
                        fabEquipmentOvenStatus.setTemp(String.valueOf(map.get("PARAMVALUE")));
                    }
                }
            }
        });
        String content = JSON.toJSONString(fabEquipmentOvenStatusList);
        ServletUtils.printJson(response, content);
        //FastJsonUtils.print(fabEquipmentOvenStatusList);
    }

    @RequestMapping("tempEqpList")
    public Response tempEqpList(){
        Response rs = Response.ok();
        if(lineNo.equals("APJ")){//应苏科长要求,REFLOW1 显示REFLOW1,个别设备冰箱等显示描述
            String str = "[{\"eqpId\":\"APJ-IGBT-REFLOW1\",\"eqpName\":\"APJ-IGBT-REFLOW1\"},\n" +
                    "{\"eqpId\":\"APJ-FRD-REFLOW1\",\"eqpName\":\"APJ-FRD-REFLOW1\"},\n" +
                    "{\"eqpId\":\"APJ-DBCT-REFLOW1\",\"eqpName\":\"APJ-DBCT-REFLOW1\"},\n" +
                    "{\"eqpId\":\"APJ-DBCB-REFLOW1\",\"eqpName\":\"APJ-DBCB-REFLOW1\"},\n" +
                    "{\"eqpId\":\"APJ-AT1\",\"eqpName\":\"APJ-AT1\"},\n" +
                    "{\"eqpId\":\"APJ-CLEAN-US1\",\"eqpName\":\"APJ-CLEAN-US1温度仪\"},\n" +
                    "{\"eqpId\":\"APJ-TRM1\",\"eqpName\":\"APJ-TRM1\"},\n" +
                    "{\"eqpId\":\"APJ-HT\",\"eqpName\":\"高温一(温度仪)\"},\n" +
                    "{\"eqpId\":\"APJ-RT\",\"eqpName\":\"高温二(温度仪)\"},\n" +
                    "{\"eqpId\":\"APJ-HTRT1\",\"eqpName\":\"APJ-HTRT1\"},\n" +
                    "{\"eqpId\":\"APJ-OVEN1\",\"eqpName\":\"APJ-OVEN1温度仪(JET洗净后恒温槽)\"},\n" +
                    "{\"eqpId\":\"APJ-AT2\",\"eqpName\":\"APJ-AT2温度仪\"},\n" +
                    "{\"eqpId\":\"APJ-FREEZER3\",\"eqpName\":\"APJ-FREEZER3温度仪(树脂)\"},\n" +
                    "{\"eqpId\":\"APJ-OVEN2\",\"eqpName\":\"APJ-OVEN2温度仪(TRM恒温槽)\"},\n" +
                    "{\"eqpId\":\"APJ-FREEZER2\",\"eqpName\":\"APJ-FREEZER2温度仪(半田)\"},\n" +
                    "{\"eqpId\":\"APJ-FREEZER1\",\"eqpName\":\"APJ-FREEZER1温度仪(AgNps)\"}]";
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("eqpId", list);
        } else {
            rs.put("eqpId", eqpService.getTempEqpList());
        }
        return rs;
    }

    @GetMapping("chart")
    public void chart(HttpServletRequest request) {
        //方案模版
        List<Map> list = ovnBatchLotService.selectChart("");
        //排序
        list.forEach(map -> {
            if ("DOWN".equals(map.get("EQP_STATUS"))) {
                map.put("SORT_CODE", 1);
            } else if ("RUN".equals(map.get("EQP_STATUS"))) {
                map.put("SORT_CODE", 2);
            } else if ("ALARM".equals(map.get("EQP_STATUS"))) {
                map.put("SORT_CODE", 3);
            } else if ("IDLE".equals(map.get("EQP_STATUS"))) {
                map.put("SORT_CODE", 4);
            } else {
                map.put("SORT_CODE", 5);
            }
        });
        list.sort((o1, o2) -> {
            return (Integer) o1.get("SORT_CODE") - (Integer) o2.get("SORT_CODE");
        });
        FastJsonUtils.print(list);
    }

    @GetMapping("resolveAllTempFile")
    public String resolveAllTempFile(@RequestParam String eqpId, HttpServletRequest request) {
        //方案模版
        ovnBatchLotService.resolveAllTempFile(eqpId);
        return "resolve All OK";
    }

    @RequestMapping(value = "/tempbytime/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public void rptMsRecordByTime(@PathVariable String eqpId, @RequestParam String beginTime, @RequestParam String endTime,
                                  HttpServletRequest request, HttpServletResponse response) {

        List<Map> maps = ovnBatchLotService.findDetailBytime(beginTime, endTime, eqpId);
        String content = "";
        if (maps == null) {
            content = JSON.toJSONString(DateResponse.error("请缩短时间范围"));
        } else {
            OvnBatchLot ovnBatchLot = ovnBatchLotService.selectOne(new EntityWrapper<OvnBatchLot>().eq("eqp_id", eqpId));
            Response res = DateResponse.ok(maps);
            res.put("title", ovnBatchLot.getOtherTempsTitle());
            content = JSON.toJSONStringWithDateFormat(res, JSON.DEFFAULT_DATE_FORMAT);
        }
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/tempExport",method = {RequestMethod.GET, RequestMethod.POST})
    public Response tempExport(@RequestParam String eqpId,@RequestParam String startTime, @RequestParam String endTime){
        Response res = Response.ok("导出成功");
        String title = "温度导出";
        Map<String, Object> maps = ovnBatchLotService.tempExport(eqpId, startTime, endTime);
        OvnBatchLot ovnBatchLot = ovnBatchLotService.selectOne(new EntityWrapper<OvnBatchLot>().eq("eqp_id", eqpId));
        List<ExcelExportEntity> keyList = getTempKeyList(ovnBatchLot.getOtherTempsTitle().split(","));
        try {
            Workbook book = MyExcelExportUtil.exportExcel(new ExportParams("温度导出"+eqpId,"温度详细信息"),keyList,getTempDataList(maps), 5);//dataList
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
        } catch (Exception e){
            logger.error("温度导出遇到异常,参数是:"+eqpId+",startTime:"+startTime+",endTime:"+endTime, e);
        }
        return res;
    }
    private List<ExcelExportEntity> getTempKeyList(String[] titles){
        List<ExcelExportEntity> keyList = new LinkedList<>();
        if(titles!=null && titles.length > 0){
            ExcelExportEntity keyFirstCol = new ExcelExportEntity(" ","1");
            keyList.add(keyFirstCol);
            ExcelExportEntity keyTimeCol = new ExcelExportEntity("时间","2");
            keyList.add(keyTimeCol);
            for(int i=0; i<titles.length; i++){
                ExcelExportEntity key = new ExcelExportEntity(titles[i],String.valueOf(i+3));
                keyList.add(key);
            }
        }
        return keyList;
    }
    private List<Map<String, String>> getTempDataList(Map<String, Object> tempMap){
        try {
            List<String> maxList = (List) tempMap.get("maxLimit");
            List<String> minList = (List) tempMap.get("minLimit");
            List<String> setValue = (List) tempMap.get("setValue");
            List<Map<String, Object>> tempData = (List) tempMap.get("tempData");
            List<Map<String, String>> dataList = new LinkedList<>();
            if (maxList != null && maxList.size() > 0) {
                Map<String, String> maxLimit = new HashMap<>();// 上限
                Map<String, String> minLimit = new HashMap<>();// 下限
                Map<String, String> setLimit = new HashMap<>();// 设定值
                maxLimit.put("1", "上限");
                minLimit.put("1", "下限");
                setLimit.put("1", "设定值");
                for (int i = 0; i < maxList.size(); i++) {
                    maxLimit.put(String.valueOf(i + 3), maxList.get(i));
                    minLimit.put(String.valueOf(i + 3), minList.get(i));
                    setLimit.put(String.valueOf(i + 3), setValue.get(i));
                }
                dataList.add(maxLimit);
                dataList.add(minLimit);
                dataList.add(setLimit);
            }
            for (Map<String, Object> tempItem : tempData) {
                Map<String, String> data = new HashMap<>();
                data.put("2", MapUtils.getString(tempItem, "createTime"));
                List<String> allTempData = (List) tempItem.get("tempList");
                if (allTempData != null && allTempData.size() > 0) {
                    for (int i = 0; i < allTempData.size(); i++) {
                        data.put(String.valueOf(i + 3), allTempData.get(i));
                    }
                }
                dataList.add(data);
            }
            return dataList;
        } catch (Exception e){
            logger.error("温度导出时,获得数据列表时出错", e);
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "/tempbytimeOther", method = {RequestMethod.GET, RequestMethod.POST})
    public void rptMsRecordByTimeOther(@RequestParam String lotNo,
                                       HttpServletRequest request, HttpServletResponse response) {
        List<Map> maps = ovnBatchLotService.findDetailBytimeOther("SIM-YGAZO1",lotNo);
        String content = "";
        if (maps == null) {
            content = JSON.toJSONString(DateResponse.error("请缩短时间范围"));
        } else {
            OvnBatchLot ovnBatchLot = ovnBatchLotService.selectOne(new EntityWrapper<OvnBatchLot>().eq("eqp_id", "SIM-YGAZO1"));
            Response res = DateResponse.ok(maps);
            res.put("title", ovnBatchLot.getOtherTempsTitle());
            content = JSON.toJSONStringWithDateFormat(res, JSON.DEFFAULT_DATE_FORMAT);
        }
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/tempCharbytime/{eqpId}", method = {RequestMethod.GET, RequestMethod.POST})
    public Map tempMsRecordByTime(@PathVariable String eqpId, @RequestParam String beginTime, @RequestParam String endTime,
                                  HttpServletRequest request, HttpServletResponse response) {
        List<Map> os = ovnBatchLotService.findDetailBytime(beginTime, endTime, eqpId);
        OvnBatchLot ovnBatchLot = ovnBatchLotService.selectOne(new EntityWrapper<OvnBatchLot>().eq("eqp_id", eqpId));
        Map maps = new HashMap();
        String aaa[] = ovnBatchLot.getOtherTempsTitle().split(",");
        List list = new ArrayList();
        List list1 = new ArrayList();
        for (int j = 0; j < aaa.length / 4; j++) {
            for (int i = 0; i < os.size(); i++) {
                String f = aaa[j * 4];
                String str = (String) os.get(i).get("other_temps_value");
                String h[] = str.split(",");
                String str1 = String.valueOf(os.get(i).get("temp_pv"));
                String str2 = String.valueOf(os.get(i).get("temp_sp"));
                String str3 = String.valueOf(os.get(i).get("temp_min"));
                String str4 = String.valueOf(os.get(i).get("temp_max"));
                String sss[] = {str1, str2, str3, str4};
                String fff = f.replace("现在值", "");
                list1.add(sss);
                String b = h[j * 4];
                String c = h[j * 4 + 1];
                String d = h[j * 4 + 2];
                String e = h[j * 4 + 3];
                String s[] = {b, c, d, e};
                list.add(s);
                String ff = f.replace("当前值", "");
                maps.put(ff, list);
                maps.put("第一温区", list1);
            }

        }
        return maps;
    }
}








