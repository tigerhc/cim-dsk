package com.lmrj.map.tray.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.google.common.collect.Maps;
import com.lmrj.common.http.DateResponse;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.entity.MesResult;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.map.tray.entity.MapTrayChipLog;
import com.lmrj.map.tray.mapper.MapTrayChipMoveMapper;
import com.lmrj.map.tray.service.IMapTrayChipLogService;
import com.lmrj.map.tray.service.IMapTrayChipMoveProcessService;
import com.lmrj.map.tray.service.IMapTrayChipMoveService;
import com.lmrj.map.tray.util.TraceDateUtil;
import com.lmrj.map.tray.vo.MapTrayChipMoveQueryVo;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.collection.MapUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.map.tray.controller
 * @title: map_tray_chip_move控制器
 * @description: map_tray_chip_move控制器
 * @author: stev
 * @date: 2020-08-02 15:31:58
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("map/maptraychipmove")
@ViewPrefix("map/maptraychipmove")
@RequiresPathPermission("map:maptraychipmove")
@LogAspectj(title = "map_tray_chip_move")

public class MapTrayChipMoveController {
    @Autowired
    private IMapTrayChipMoveProcessService mapTrayChipMoveProcessService;

    @Autowired
    private MapTrayChipMoveMapper mapper;

    @Autowired
    private IMapTrayChipLogService mpTrayChipLogService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private IMapTrayChipMoveService mapTrayChipMoveService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param query
     */
    @RequestMapping(value = "page", method = {RequestMethod.GET, RequestMethod.POST})
    public Response pageList(MapTrayChipMoveQueryVo query) {
        if (query == null) {
            query = new MapTrayChipMoveQueryVo();
        }
        if(query.getChipIds()!=null && query.getChipIds().size()>0){
            query.setChipId(query.getLotNo()+";%"+query.getChipIds().get(0));
        }

        int total = query.getTotal();
//        if (total <= 0) {
            total = mapper.countChipMove(query);
//        }
        List<Map<String, Object>> list = null;
        if (total > 0) {
            list = mapper.queryChipMove(query);
        }
        Response resp = DateResponse.ok(list);
        resp.put("total", total);
        return resp;
    }

    /**
     * 查询芯片轨迹
     *
     * @param chipId
     */
    @RequestMapping(value = "moveDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public Response pageList(@RequestParam String chipId) { //chipId 是 id 不是表中的chipId
        List<Map<String, Object>> list = mapper.queryChip(chipId);
        if(list!=null && list.size()>0){
            Map<String, Object> item = list.get(0);
            String dataChipId = MapUtil.getString(item, "chipId");
            if(StringUtil.isEmpty(dataChipId)){//查询伪码
                String pseudoCode = MapUtil.getString(item, "pseudoCode");
                if(StringUtil.isNotEmpty(pseudoCode)){
                    List<Map<String, Object>> pseudoCodeList = mapper.queryByPseudoCode(pseudoCode);
                    return DateResponse.ok(pseudoCodeList);
                }
            }
        }
        return DateResponse.ok(list);
    }

    /**
     * 查询晶圆轨迹
     *
     * @param id
     */
    @RequestMapping(value = "dmDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public Response dmDetail(@RequestParam String id) {
        List<Map<String, Object>> list = mapTrayChipMoveProcessService.dmDetail(id);
        return DateResponse.ok(list);
    }

    @RequestMapping(value = "traceData", method = {RequestMethod.GET, RequestMethod.POST})
    public Response pageList() {
        MapTrayChipLog traceLog = new MapTrayChipLog();
        try{
            //是否正在追溯中【正在追溯中的特点是，在log表中有一条记录只有开始时间，没有结束时间】
            Integer chkRunning = mapper.chkProcessRunning(TraceDateUtil.getChkTime(new Date(), -5));
            if(chkRunning == null || chkRunning==0){
                //设置为追溯中，即向log表中添加一个只有开始时间没有结束的时间的记录
                traceLog.setBeginTime(new Date());
                mpTrayChipLogService.insert(traceLog);
//                mapTrayChipMoveProcessService.traceData(traceLog, IMapTrayChipMoveProcessService.processErrDataFlag);
                mapTrayChipMoveProcessService.traceData(traceLog, IMapTrayChipMoveProcessService.processAsynchronous);
            }
        } catch (Exception e){
            e.printStackTrace();
            traceLog.setEndTime(new Date());
            mpTrayChipLogService.updateById(traceLog);
        }
        return Response.ok();
    }

    @RequestMapping(value = "getCommand",method = {RequestMethod.GET, RequestMethod.POST})
    public Response getCommand(@RequestParam String bcCode){
        Response rs = Response.ok();
        Map<String, String> map = Maps.newHashMap();
        map.put("METHOD", "TRAY_REUPLOAD");
        String replyMsg = (String) rabbitTemplate.convertSendAndReceive("S2C.T.CIM.COMMAND", bcCode, JsonUtil.toJsonString(map));
        if (replyMsg != null) {
            MesResult result = JsonUtil.from(replyMsg, MesResult.class);
            if ("Y".equals(result.getFlag())) {
                rs.put("cnt", result.getContent());
                return rs;
            }
        }
        return Response.error("没有得到返回结果");
    }

    @RequestMapping(value = "getProductionParam", method = {RequestMethod.GET, RequestMethod.POST})
    public Response getProductionParam(@RequestParam String id, @RequestParam String btn){
        Response rs = Response.ok();
        Map<String, Object> productionParam = mapTrayChipMoveProcessService.getProductionParam(Long.parseLong(id), btn);
        rs.put("paramObj", productionParam);
        return rs;
    }

    @RequestMapping(value = "/traceDataExport",method = {RequestMethod.GET, RequestMethod.POST})
    public Response traceDataExport(MapTrayChipMoveQueryVo query){
        try {
            String title = "追溯导出";
            Response res = Response.ok("导出成功");

            List<ExcelExportEntity> keyList = getExportKeyList();
            List<Map<String, String>> dataList = getExportDataList(query);

            Workbook book = MyExcelExportUtil.exportExcel(new ExportParams("追溯导出","追溯数据信息"),keyList,dataList, 2);
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

    /** 追溯查询物料信息 */
    @RequestMapping(value = "/findMaterial",method = {RequestMethod.GET, RequestMethod.POST})
    public Response findMaterial(@RequestParam String eqpId, @RequestParam String lotNo){
        Response res = Response.ok();
        res.put("meterial", mapTrayChipMoveService.findMaterial(eqpId, lotNo));
        return res;
    }

    /**
     * 文件打成压缩包下载
     */
//    @RequiresPermissions("system:appraising:detail")
//    @RequestMapping(value = "/downloadFile",method = {RequestMethod.GET, RequestMethod.POST})
//    public void downloadFile(String appraisingId, String appraisingName, String userName, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        List<UserAttachVo> userAttachVos;
//        if(null != appraisingId) {
//            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
//            try {
//                userAttachVos = iUserAttachService.findUserAttachByAppraisingId(Long.valueOf(appraisingId));
//                // 文件的名称
//                String downloadFilename = appraisingName + userName + ".zip";
//                // 转换中文否则可能会产生乱码
//                downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");
//                // 指明response的返回对象是文件流
//                response.setContentType("application/octet-stream");
//                // 设置在下载框默认显示的文件名
//                response.setHeader("Content-Disposition","attachment;filename=" + downloadFilename);
//
//                // 将文件打成压缩包并下载
//                for(UserAttachVo userAttach : userAttachVos) {
////                    String url = userAttach.getFilePath();
//                    String url = "";
//                    zos.putNextEntry(new ZipEntry(userAttach.getFileName()));
//                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//                    conn.setReadTimeout(5000);
//                    conn.setConnectTimeout(5000);
//                    conn.setRequestMethod("GET");
//                    BufferedInputStream fis = null;
//                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        fis = new BufferedInputStream(conn.getInputStream());
//                    }
//                    byte[] buffer = new byte[1024];
//                    int r = 0;
//                    if (null != fis) {
//                        while ((r = fis.read(buffer)) != -1) {
//                            zos.write(buffer, 0, r);
//                        }
//                        fis.close();
//                    }
//                }
//            } catch (Exception e) {
//                log.error("下载文件失败", e);
//            } finally {
//                zos.flush();
//                zos.close();
//            }
//        }
//    }

    private List<ExcelExportEntity> getExportKeyList(){
        List<ExcelExportEntity> keyList = new LinkedList<>();
        ExcelExportEntity key1 = new ExcelExportEntity("批次号","1");
        keyList.add(key1);
        ExcelExportEntity key2 = new ExcelExportEntity("制品号","2");
        keyList.add(key2);
        ExcelExportEntity key3 = new ExcelExportEntity("设备号","3");
        keyList.add(key3);
        ExcelExportEntity key4 = new ExcelExportEntity("品番号","4");
        keyList.add(key4);
        ExcelExportEntity key5 = new ExcelExportEntity("托盘ID","5");
        keyList.add(key5);
        ExcelExportEntity key6 = new ExcelExportEntity("X坐标","6");
        keyList.add(key6);
        ExcelExportEntity key7 = new ExcelExportEntity("Y坐标","7");
        keyList.add(key7);
        ExcelExportEntity key8 = new ExcelExportEntity("综合判定","8");
        keyList.add(key8);
        ExcelExportEntity key9 = new ExcelExportEntity("时间","9");
        keyList.add(key9);
        ExcelExportEntity key10 = new ExcelExportEntity("晶圆ID","10");
        keyList.add(key10);
        ExcelExportEntity key11 = new ExcelExportEntity("晶圆X","11");
        keyList.add(key11);
        ExcelExportEntity key12 = new ExcelExportEntity("晶圆Y","12");
        keyList.add(key12);
        ExcelExportEntity key13 = new ExcelExportEntity("物料信息","13");
        keyList.add(key13);
        return keyList;
    }

    private List<Map<String, String>> getExportDataList(MapTrayChipMoveQueryVo query){
        if (query == null) {
            query = new MapTrayChipMoveQueryVo();
        }
        if(query.getChipIds()!=null && query.getChipIds().size()>0){
            query.setChipId(query.getLotNo()+";%"+query.getChipIds().get(0));
        }

        List<Map<String, Object>> list = mapper.queryChipMove(query);
        List<Map<String, String>> dataList = new LinkedList<>();
        if (list.size() > 0) {
            for(Map<String, Object> item : list){
                Map<String, String> data = new HashMap<>();
                data.put("1", MapUtil.getString(item, "lotNo"));
                data.put("2", MapUtil.getString(item, "chipId"));
                data.put("3", MapUtil.getString(item, "eqpId"));
                data.put("4", MapUtil.getString(item, "productionNo"));
                data.put("5", MapUtil.getString(item, "toTrayId"));
                data.put("6", MapUtil.getString(item, "toX"));
                data.put("7", MapUtil.getString(item, "toY"));
                data.put("8", MapUtil.getString(item, "judgeResult"));
                data.put("9", MapUtil.getString(item, "startTime"));
                data.put("10", MapUtil.getString(item, "dmId"));
                data.put("11", MapUtil.getString(item, "dmX"));
                data.put("12", MapUtil.getString(item, "dmY"));
                data.put("13", MapUtil.getString(item, "materialInfo"));
                dataList.add(data);
            }
        }
        return dataList;
    }
}
