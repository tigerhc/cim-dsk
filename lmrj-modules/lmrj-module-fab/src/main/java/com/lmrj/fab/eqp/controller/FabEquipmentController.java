package com.lmrj.fab.eqp.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lmrj.cim.utils.OfficeUtils;
import com.lmrj.cim.utils.UserUtil;
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
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.fab.eqp.entity.FabEquipment;
import com.lmrj.fab.eqp.entity.FabSensor;
import com.lmrj.fab.eqp.service.IFabEquipmentBindService;
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import com.lmrj.fab.eqp.service.IFabSensorService;
import com.lmrj.util.mapper.JsonUtil;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.fab.controller
 * @title: fab_equipment控制器
 * @description: fab_equipment控制器
 * @author: 张伟江/list
 * @date: 2019-06-04 15:42:26
 * @copyright: 2018 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("fab/fabequipment")
@ViewPrefix("modules/FabEquipment")
@RequiresPathPermission("FabEquipment")
@LogAspectj(title = "fab_equipment")
public class FabEquipmentController extends BaseCRUDController<FabEquipment> {

    @Autowired
    private IFabEquipmentService fabEquipmentService;
    @Autowired
    private IFabEquipmentBindService iIotEquipmentBindService;
    @Autowired
    private IFabSensorService sensorService;
    @Autowired
    private IFabEquipmentBindService fabEquipmentBindService;


    String title = "设备信息";
    @Value("${dsk.lineNo}")
    private String lineNo;
    /**
     * 设备列表下拉框
     * @param model
     * @param request
     * @param response
     * @return
     * Queryable queryable,
     * QueryableConvertUtils.convertQueryValueToEntityValue(queryable, this.entityClass);
     *         List<FabEquipment>  eqps = fabEquipmentService.listWithNoPage(queryable);
     */
    @RequestMapping(value = "/eqpIdlist", method = { RequestMethod.GET, RequestMethod.POST })
    public void eqpIdList(Model model, @RequestParam(required = false) String param, HttpServletRequest request,
                          HttpServletResponse response) {
        if("MS".equals(param)){
            eqpIdMsList2(model, request, response);
            return;
        }else if("OEE".equals(param)){
            List<Map> list = fabEquipmentService.findOeeEqpMap();
            DateResponse listjson = new DateResponse(list);
            String content = JSON.toJSONString(listjson);
            ServletUtils.printJson(response, content);
        }else {
            List<Map> list = fabEquipmentService.findEqpMap();
            DateResponse listjson = new DateResponse(list);
            String content = JSON.toJSONString(listjson);
            ServletUtils.printJson(response, content);
        }
    }

    @RequestMapping(value = "/SelectStarteqpIdlist", method = { RequestMethod.GET, RequestMethod.POST })
    public void SelectStarteqpIdlist(Model model, @RequestParam(required = false) String param, HttpServletRequest request,
                                     HttpServletResponse response) {
        if("MS".equals(param)){
            eqpIdMsList2(model, request, response);
            return;
        }
        String str ="";
        if(lineNo.equals("SIM")){//应苏科长要求,REFLOW1 显示REFLOW1,个别设备冰箱等显示描述
            str = "[{\"id\":\"SIM-PRINTER1\",\"name\":\"SIM-印刷机1\"},\n" +
                    "{\"id\":\"SIM-YGAZO1\",\"name\":\"SIM-印刷后画像1#\"},\n" +
                    "{\"id\":\"SIM-DM1\",\"name\":\"SIM-DM1#\"},\n" +
                    "{\"id\":\"SIM-DM2\",\"name\":\"SIM-DM2#\"},\n" +
                    "{\"id\":\"SIM-DM3\",\"name\":\"SIM-DM3#\"},\n" +
                    "{\"id\":\"SIM-DM4\",\"name\":\"SIM-DM4#\"},\n" +
                    "{\"id\":\"SIM-DM5\",\"name\":\"SIM-DM5#\"},\n" +
                    "{\"id\":\"SIM-DM6\",\"name\":\"SIM-DM6#\"},\n" +
                    "{\"id\":\"SIM-DM7\",\"name\":\"SIM-DM7#\"},\n" +
                    "{\"id\":\"SIM-REFLOW1\",\"name\":\"SIM-回流焊1#\"},\n" +
                    "{\"id\":\"SIM-HGAZO1\",\"name\":\"SIM-回流焊后画像1#\"},\n" +
                    "{\"id\":\"SIM-WB-1A\",\"name\":\"SIM-WB焊线机1A\"},\n" +
                    "{\"id\":\"SIM-WB-1B\",\"name\":\"SIM-WB焊线机1B\"},\n" +
                    "{\"id\":\"SIM-WB-2A\",\"name\":\"SIM-WB焊线机2A\"},\n" +
                    "{\"id\":\"SIM-WB-2B\",\"name\":\"SIM-WB焊线机2B\"},\n" +
                    "{\"id\":\"SIM-WB-3A\",\"name\":\"SIM-WB焊线机3A\"},\n" +
                    "{\"id\":\"SIM-WB-3B\",\"name\":\"SIM-WB焊线机3B\"},\n" +
                    "{\"id\":\"SIM-WB-4A\",\"name\":\"SIM-WB焊线机4A\"},\n" +
                    "{\"id\":\"SIM-WB-4B\",\"name\":\"SIM-WB焊线机4B\"},\n" +
                    "{\"id\":\"SIM-WB-5A\",\"name\":\"SIM-WB焊线机5A\"},\n" +
                    "{\"id\":\"SIM-WB-5B\",\"name\":\"SIM-WB焊线机5B\"},\n" +
                    "{\"id\":\"SIM-WB-6A\",\"name\":\"SIM-WB焊线机6A\"},\n" +
                    "{\"id\":\"SIM-WB-6B\",\"name\":\"SIM-WB焊线机6B\"},\n" +
                    "{\"id\":\"SIM-TRM1\",\"name\":\"SIM-TRM塑封机1\"},\n" +
                    "{\"id\":\"SIM-TRM2\",\"name\":\"SIM-TRM塑封机2\"},\n" +
                    "{\"id\":\"SIM-LF1\",\"name\":\"SIM-分离1\"},\n" +
                    "{\"id\":\"SIM-HTRT1\",\"name\":\"SIM-检查1\"},\n" +
                    "{\"id\":\"SIM-LF2\",\"name\":\"SIM-分离2\"},\n" +
                    "{\"id\":\"SIM-HTRT2\",\"name\":\"SIM-检查2\"}]";
        } else {
            str = "[{\"id\":\"DM-IGBT-SMT1\",\"name\":\"DM-IGBT印刷贴片机\"},\n" +
                    "{\"id\":\"DM-IGBT-2DAOI1\",\"name\":\"DM-IGBT印刷 2D画像检查机\"},\n" +
                    "{\"id\":\"DM-IGBT-REFLOW1\",\"name\":\"DM-IGBT印刷立式固化炉\"},\n" +
                    "{\"id\":\"DM-IGBT-3DAOI1\",\"name\":\"DM-IGBT印刷-3D画像检查机\"},\n" +
                    "{\"id\":\"DM-IGBT-SORT1\",\"name\":\"DM-IGBT印刷下料机\"},\n" +
                    "{\"id\":\"DM-IGBT-SORT2\",\"name\":\"DM-IGBT预结合上料机\"},\n" +
                    "{\"id\":\"DM-IGBT-DM1\",\"name\":\"DM-IGBT预结合晶圆贴片机\"},\n" +
                    "{\"id\":\"DM-IGBT-SORT3\",\"name\":\"DM-IGBT预结合下料机\"},\n" +
                    "{\"id\":\"DM-FRD-SMT1\",\"name\":\"DM-IGBT印刷贴片机\"},\n" +
                    "{\"id\":\"DM-FRD-2DAOI1\",\"name\":\"DM-FRD印刷 2D画像检查机\"},\n" +
                    "{\"id\":\"DM-FRD-REFLOW1\",\"name\":\"DM-FRD印刷立式固化炉\"},\n" +
                    "{\"id\":\"DM-FRD-3DAOI1\",\"name\":\"DM-FRD印刷-3D画像检查机\"},\n" +
                    "{\"id\":\"DM-FRD-SORT1\",\"name\":\"DM-FRD印刷下料机\"},\n" +
                    "{\"id\":\"DM-FRD-SORT2\",\"name\":\"DM-FRD预结合上料机\"},\n" +
                    "{\"id\":\"DM-FRD-DM1\",\"name\":\"DM-FRD预结合晶圆贴片机\"},\n" +
                    "{\"id\":\"DM-FRD-SORT3\",\"name\":\"DM-FRD预结合下料机\"},\n" +
                    "{\"id\":\"DM-HB1-SORT1\",\"name\":\"DM-一次热压上料机\"},\n" +
                    "{\"id\":\"DM-HB1-SINTERING1\",\"name\":\"DM-一次热压机\"},\n" +
                    "{\"id\":\"DM-HB1-3DAOI1\",\"name\":\"DM-一次热压3D画像检查机\"},\n" +
                    "{\"id\":\"DM-HB1-SORT2\",\"name\":\"DM-一次热压下料机\"},\n" +
                    "{\"id\":\"DM-VI1\",\"name\":\"DM-中间耐压检查一体机\"},\n" +
                    "{\"id\":\"DM-DBCT-SORT1\",\"name\":\"DM-上基板印刷上料机   \"},\n" +
                    "{\"id\":\"DM-DBCT-2DAOI1\",\"name\":\"DM-上基板印刷2D画像检查机\"},\n" +
                    "{\"id\":\"DM-DBCT-REFLOW1\",\"name\":\"DM-上基板印刷立式固化炉\"},\n" +
                    "{\"id\":\"DM-DBCT-3DAOI1\",\"name\":\"DM-上基板3D画像检查机\"},\n" +
                    "{\"id\":\"DM-DBCT-SORT2\",\"name\":\"DM-上基板印刷下料机\"},\n" +
                    "{\"id\":\"DM-DBCB-SORT1\",\"name\":\"DM-下基板印刷上料机   \"},\n" +
                    "{\"id\":\"DM-DBCB-2DAOI1\",\"name\":\"DM-下基板印刷2D画像检查机\"},\n" +
                    "{\"id\":\"DM-DBCB-REFLOW1\",\"name\":\"DM-下基板印刷立式固化炉\"},\n" +
                    "{\"id\":\"DM-DBCB-3DAOI1\",\"name\":\"DM-下基板3D画像检查机\"},\n" +
                    "{\"id\":\"DM-DBCB-SORT2\",\"name\":\"DM-下基板印刷下料机\"},\n" +
                    "{\"id\":\"DM-HB2-SORT1\",\"name\":\"DM-二次热压上料机\"},\n" +
                    "{\"id\":\"DM-HB2-DISPENSING1\",\"name\":\"DM-二次热压点胶机1\"},\n" +
                    "{\"id\":\"DM-HB2-DISPENSING2\",\"name\":\"DM-二次热压点胶机2\"},\n" +
                    "{\"id\":\"DM-HB2-DISPENSING3\",\"name\":\"DM-二次热压点胶机3\"},\n" +
                    "{\"id\":\"DM-HB2-2DAOI1\",\"name\":\"DM-二次热压2D画像检查机1\"},\n" +
                    "{\"id\":\"DM-HB2-SMT1\",\"name\":\"DM-二次热压贴片机1\"},\n" +
                    "{\"id\":\"DM-HB2-SMT2\",\"name\":\"DM-二次热压贴片机2\"},\n" +
                    "{\"id\":\"DM-HB2-2DAOI2\",\"name\":\"DM-二次热压2D画像检查机2\"},\n" +
                    "{\"id\":\"DM-HB2-ASSEMBLY1\",\"name\":\"DM-二次热压组立机\"},\n" +
                    "{\"id\":\"DM-HB2-SINTERING1\",\"name\":\"DM-二次热压机\"},\n" +
                    "{\"id\":\"DM-HB2-SORT2\",\"name\":\"DM-二次热压下料机\"},\n" +
                    "{\"id\":\"DM-XRAY1\",\"name\":\"DM-X射线检查机\"},\n" +
                    "{\"id\":\"DM-CLEAN-US1\",\"name\":\"DM-US1洗净机\"},\n" +
                    "{\"id\":\"DM-CLEAN-JET1\",\"name\":\"DM-JET洗净机\"},\n" +
                    "{\"id\":\"DM-TRM1\",\"name\":\"DM-TRM1\"},\n" +
                    "{\"id\":\"DM-LF1\",\"name\":\"DM-分离检查机\"},\n"+
                    "{\"id\":\"DM-AT1\",\"name\":\"DM-耐老化试验搬送机\"},\n" +
                    "{\"id\":\"DM-HTRT1\",\"name\":\"DM-高温室温检查机\"}]";
        }
        List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    /**
     * 设备列表下拉框
     * @param model
     * @param request
     * @param response
     * @return
     * Queryable queryable,
     * QueryableConvertUtils.convertQueryValueToEntityValue(queryable, this.entityClass);
     *         List<FabEquipment>  eqps = fabEquipmentService.listWithNoPage(queryable);
     */
    @RequestMapping(value = "/eqpIdlistByCode/{classcode}", method = { RequestMethod.GET, RequestMethod.POST })
    public void eqpIdlistByCode(Model model,@PathVariable String classcode, HttpServletRequest request,
                                HttpServletResponse response) {

        List<Map> list = fabEquipmentService.findEqpMapByCode(classcode);
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/eqpIdMsList", method = { RequestMethod.GET, RequestMethod.POST })
    public void eqpIdMsList2(Model model, HttpServletRequest request,
                             HttpServletResponse response) {
        List<Map> eqpids = fabEquipmentService.findEqpMsMap();
        DateResponse listjson = new DateResponse(eqpids);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }



    /**
     * 在返回数据之前编辑数据
     *
     * @param entity
     */
    @Override
    public void afterFind(FabEquipment entity) {
        //组织机构
        Organization office = OfficeUtils.getOffice(entity.getOfficeId());
        if(office != null){
            entity.setOfficeIds(office.getParentIds().replaceAll("/",",")+entity.getOfficeId());
        }
        UserUtil.updateUserName(entity);
        //查询绑定信息并展示对应的传感器信息
        entity.setIotEquipmentBindList(iIotEquipmentBindService.getIotEquipmenetBindList(entity.getEqpId())) ;
    }

    /**
     * 保存完主题表后处理
     *
     * @param entity
     * @param request
     * @param response
     */
    @Override
    public void afterSave(FabEquipment entity, HttpServletRequest request, HttpServletResponse response) {
        //1.保存绑定表
        //    iIotEquipmentBindService.saveBindList(entity.getIotEquipmentBindList());
        //2.保存传感器表

    }
    /**
     * 在返回list数据之前编辑数据
     *
     * @param pagejson
     */
    @Override
    public void afterPage(PageResponse<FabEquipment> pagejson, HttpServletRequest request, HttpServletResponse response) {
        List<FabEquipment> list = pagejson.getResults();
        for(FabEquipment fabEquipment: list){
            if(fabEquipment.getOfficeId() != null){
                Organization office = OfficeUtils.getOffice(fabEquipment.getOfficeId());
                if(office != null){
                    fabEquipment.setOfficeName(office.getName());
                }
            }
        }
    }

    //@RequestMapping(value = "{id}/inactiveeqp", method = RequestMethod.POST)
    @PutMapping("/active_flag/{id}/{flag}")
    @ResponseBody
    public Response update(@PathVariable String id, @PathVariable String flag,
                           HttpServletRequest request, HttpServletResponse response) {
        fabEquipmentService.activeEqp(id, flag);
        return Response.ok("修改成功");
    }




//    @GetMapping("export")
//    //@LogAspectj(logType = LogType.EXPORT)
////    @RequiresMethodPermissions("export")
//    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
//
//        super.export(queryable,  propertyPreFilterable,  request,  response);
//        Response response2 = Response.ok("导出成功");
//        try {
//            EntityWrapper<FabEquipment> entityWrapper = new EntityWrapper(this.entityClass);
//            this.preList(queryable, entityWrapper, request, response);
//            propertyPreFilterable.addQueryProperty(new String[]{"id"});
//            QueryableConvertUtils.convertQueryValueToEntityValue(queryable, this.entityClass);
//            SerializeFilter filter = propertyPreFilterable.constructFilter(this.entityClass);
//            Page pageBean = this.commonService.list(queryable, entityWrapper);
//            List newList = Lists.newArrayList();
//            newList.addAll(pageBean.getContent());
//
//
//            String title = "设备信息";
//            TemplateExportParams params = new TemplateExportParams("");
//            Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
//                    title, title, ExcelType.XSSF), FabEquipment.class, newList);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            book.write(bos);
//            byte[] bytes = bos.toByteArray();
//            String bytesRes = StringUtil.bytesToHexString2(bytes);
//            title = title+ "-" + DateUtil.getDateTime();
//            response2.put("bytes",bytesRes);
//            response2.put("title",title);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return response2.error(999998,"导出失败");
//        }
//        return response2;
//    }

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("设备信息", queryable,  propertyPreFilterable,  request,  response);
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public Response upload(MultipartFile file){
        Response response = Response.ok("导入成功");
        if (file == null) {
            return Response.error(999998,"文件不能为空，导出失败");
        }
        List<FabEquipment> list = Lists.newArrayList();
        try {
            InputStream inp = file.getInputStream();

            Workbook workbook=null;
            if(! inp.markSupported()) {
                inp = new PushbackInputStream(inp, 8);
            }
            if(POIFSFileSystem.hasPOIFSHeader(inp)) {
                workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            }
            if(POIXMLDocument.hasOOXMLHeader(inp)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
            // 有多少个sheet
            int sheets = workbook.getNumberOfSheets();
            for (int i = 0; i < sheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                // 获取多少行
                int rows = sheet.getPhysicalNumberOfRows();
                FabEquipment fabEquipment = null;
                // 遍历每一行，注意：第 0 行为标题
                for (int j = 1; j < rows; j++) {
                    fabEquipment = new FabEquipment();
                    // 获得第 j 行
                    Row row = sheet.getRow(j);
                    // 调整类型
                    fabEquipment.setEqpId(String.valueOf(row.getCell(0)));
                    fabEquipment.setOfficeId(String.valueOf(row.getCell(1)));
                    fabEquipment.setStepId(String.valueOf(row.getCell(2)));
                    fabEquipment.setStepCode(String.valueOf(row.getCell(3)));
                    fabEquipment.setActiveFlag(String.valueOf(row.getCell(4)));
                    fabEquipment.setBcCode(String.valueOf(row.getCell(5)));
                    fabEquipment.setIp(String.valueOf(row.getCell(6)));
                    fabEquipment.setPort(String.valueOf(row.getCell(7)));
                    fabEquipment.setDeviceId(String.valueOf(row.getCell(8)));
                    fabEquipment.setModelId(String.valueOf(row.getCell(9)));
                    fabEquipment.setModelName(String.valueOf(row.getCell(10)));
                    fabEquipment.setProtocolName(String.valueOf(row.getCell(11)));
                    fabEquipment.setFab(String.valueOf(row.getCell(12)));
                    fabEquipment.setLineNo(String.valueOf(row.getCell(13)));
                    fabEquipment.setLocation(String.valueOf(row.getCell(14)));
                    fabEquipment.setUpdateBy("4028ea815a3d2a8c015a3d2f8d2a0002");
                    fabEquipment.setUpdateDate(new Date());
                    fabEquipment.setCreateDate(new Date());
                    fabEquipment.setCreateBy("4028ea815a3d2a8c015a3d2f8d2a0002");
                    list.add(fabEquipment);
                }
            }
            fabEquipmentService.insertBatch(list,100);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(999998,"导出失败");
        }
        return response;
    }

    /**
     * 直接返回对象,交给springboot处理转换为json
     * 测试用
     * @param model
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/{id}/find2", method = { RequestMethod.GET, RequestMethod.POST })
    public Response find2(Model model, @PathVariable("id") String id, HttpServletRequest request,
                          HttpServletResponse response) {
        FabEquipment entity = get(id);
        afterGet(entity);
        Response res;
        if(entity == null){
            res = Response.error("未查询到数据");
        }else{
            res = DateResponse.ok(entity);
        }
        return res;
    }

    /**
     * 展示设备绑定的传感器
     * @param eqpId
     * @param request
     * @param response
     */
    @RequestMapping("/SensorList/{eqpId}")
    public Response selectFabSensorId(@PathVariable("eqpId") String eqpId, HttpServletRequest request,
                                      HttpServletResponse response){
        List<FabSensor> list = fabEquipmentService.selectFabSensorId( eqpId );
        if(list == null){
            return Response.error("无绑定传感器");
        }
        return Response.ok("查到传感器");
    }

    /**
     * 都是设备表的字段
     * 添加设备生成对应传感器
     */
    @RequestMapping("/{isBindCreated}/{eqpId}/AoutAddSensor")
    public void AoutAddSensor(  @PathVariable String isBindCreated,  @PathVariable String eqpId,
                                HttpServletRequest request, HttpServletResponse response){
        List<Map> list = sensorService.aoutAddSensor( isBindCreated , eqpId);
        if(list==null&&list.size()==0){
            Response.error("该设备无对应传感器");
        }
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    /*部门名称
     * */
    @RequestMapping("/selectOfficeName")
    public void selectOfficeName( HttpServletRequest request, HttpServletResponse response){
        List<FabEquipment> list = fabEquipmentService.selectOfficeName();
        for(FabEquipment fabEquipment: list){
            if(fabEquipment.getOfficeId() != null){
                Organization office = OfficeUtils.getOffice(fabEquipment.getOfficeId());
                if(office != null && fabEquipment.getOfficeId().split( "," )[fabEquipment.getOfficeId().split( "," ).length-1] == office.getId()){
                    fabEquipment.setOfficeName(office.getName());
                }
            }
        }
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);    }
}
