package com.lmrj.fab.eqp.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
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
import com.lmrj.core.sys.entity.Organization;
import com.lmrj.fab.eqp.entity.FabEquipmentModel;
import com.lmrj.fab.eqp.entity.FabModelTemplate;
import com.lmrj.fab.eqp.entity.FabSensor;
import com.lmrj.fab.eqp.service.IFabSensorService;
import com.lmrj.fab.eqp.service.IFabEquipmentBindService;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wdj
 * @date 2021-06-01 8:59
 */
@RestController
@RequestMapping("fab/fabSensor")
@ViewPrefix("fab/fabSensor")
@RequiresPathPermission("fab:fabSensor")
public class FabSensorController extends BaseCRUDController<FabSensor> {

    @Autowired
    private IFabSensorService FabSensorService;
    @Autowired
    private IFabEquipmentBindService iIotEquipmentBindService;
    String title = "设备信息";


    /**
     * 设备列表下拉框
     * @param model
     * @param request
     * @param response
     * @return
     * Queryable queryable,
     * QueryableConvertUtils.convertQueryValueToEntityValue(queryable, this.entityClass);
     *         List<FabSensor>  eqps = FabSensorService.listWithNoPage(queryable);
     */
    @RequestMapping(value = "/eqpIdlist", method = { RequestMethod.GET, RequestMethod.POST })
    public void eqpIdList(Model model, @RequestParam(required = false) String param, HttpServletRequest request,
                          HttpServletResponse response) {
        if("MS".equals(param)){
            eqpIdMsList2(model, request, response);
            return;
        }
        List<Map> list = FabSensorService.findEqpMap();
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    @RequestMapping(value = "/eqpIdMsList", method = { RequestMethod.GET, RequestMethod.POST })
    public void eqpIdMsList2(Model model, HttpServletRequest request,
                             HttpServletResponse response) {
        List<Map> eqpids = FabSensorService.findEqpMsMap();
        DateResponse listjson = new DateResponse(eqpids);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }

    /**
     * 传感器列表下拉框（设备绑定添加页面）
     * @param model
     * @param request
     * @param response
     * @return
     * Queryable queryable,
     * QueryableConvertUtils.convertQueryValueToEntityValue(queryable, this.entityClass);
     *         List<FabSensor>  eqps = FabSensorService.listWithNoPage(queryable);
     */
    @RequestMapping(value = "/sorIdlist/{classCode}", method = { RequestMethod.GET, RequestMethod.POST })
    public void sorIdlist(Model model, @PathVariable String classCode, HttpServletRequest request,
                          HttpServletResponse response) {

        List<Map> list = FabSensorService.findNoSorMap(classCode);
        DateResponse listjson = new DateResponse(list);
        String content = JSON.toJSONString(listjson);
        ServletUtils.printJson(response, content);
    }



    /**
     * 在返回数据之前编辑数据
     *
     * @param entity
     */
    @Override
    public void afterFind(FabSensor entity) {
        //组织机构
        Organization office = OfficeUtils.getOffice(entity.getOfficeId());
        if(office != null){
            entity.setOfficeIds(office.getParentIds().replaceAll("/",",")+entity.getOfficeId());
        }
        UserUtil.updateUserName(entity);
        //查询绑定信息并展示对应的传感器信息
      //  entity.setIotEquipmentBindList(iIotEquipmentBindService.getIotEquipmenetBindList(entity.getId())) ;
    }


    /**
     * 在返回list数据之前编辑数据
     *
     * @param pagejson
     */
    @Override
    public void afterPage(PageResponse<FabSensor> pagejson, HttpServletRequest request, HttpServletResponse response) {
        List<FabSensor> list = pagejson.getResults();
        for(FabSensor FabSensor: list){
            if(FabSensor.getOfficeId() != null){
                Organization office = OfficeUtils.getOffice(FabSensor.getOfficeId());
                if(office != null){
                    FabSensor.setOfficeName(office.getName());
                }
            }
        }
    }

    //@RequestMapping(value = "{id}/inactiveeqp", method = RequestMethod.POST)
    @PutMapping("/active_flag/{id}/{flag}")
    @ResponseBody
    public Response update(@PathVariable String id, @PathVariable String flag,
                           HttpServletRequest request, HttpServletResponse response) {
        FabSensorService.activeEqp(id, flag);
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
//            EntityWrapper<FabSensor> entityWrapper = new EntityWrapper(this.entityClass);
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
//                    title, title, ExcelType.XSSF), FabSensor.class, newList);
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
        List<FabSensor> list = Lists.newArrayList();
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
                FabSensor FabSensor = null;
                // 遍历每一行，注意：第 0 行为标题
                for (int j = 1; j < rows; j++) {
                    FabSensor = new FabSensor();
                    // 获得第 j 行
                    Row row = sheet.getRow(j);
                    // 调整类型
                    FabSensor.setSorId(String.valueOf(row.getCell(0)));
                    FabSensor.setOfficeId(String.valueOf(row.getCell(1)));
                    FabSensor.setStepId(String.valueOf(row.getCell(2)));
                    FabSensor.setStepCode(String.valueOf(row.getCell(3)));
                    FabSensor.setActiveFlag(String.valueOf(row.getCell(4)));
                    FabSensor.setBcCode(String.valueOf(row.getCell(5)));
                    FabSensor.setIp(String.valueOf(row.getCell(6)));
                    FabSensor.setPort(String.valueOf(row.getCell(7)));
                    FabSensor.setDeviceId(String.valueOf(row.getCell(8)));
                    FabSensor.setModelId(String.valueOf(row.getCell(9)));
                    FabSensor.setModelName(String.valueOf(row.getCell(10)));
                    FabSensor.setProtocolName(String.valueOf(row.getCell(11)));
                    FabSensor.setFab(String.valueOf(row.getCell(12)));
                    FabSensor.setLineNo(String.valueOf(row.getCell(13)));
                    FabSensor.setLocation(String.valueOf(row.getCell(14)));
                    FabSensor.setUpdateBy("4028ea815a3d2a8c015a3d2f8d2a0002");
                    FabSensor.setUpdateDate(new Date());
                    FabSensor.setCreateDate(new Date());
                    FabSensor.setCreateBy("4028ea815a3d2a8c015a3d2f8d2a0002");
                    list.add(FabSensor);
                }
            }
            FabSensorService.insertBatch(list,100);
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
        FabSensor entity = get(id);
        afterGet(entity);
        Response res;
        if(entity == null){
            res = Response.error("未查询到数据");
        }else{
            res = DateResponse.ok(entity);
        }
        return res;
    }



    @Override
    public void afterSave(FabSensor entity, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    @RequestMapping(
            value = {"{id}/delete"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public Response delete(@PathVariable("id") String id) {


        return super.delete(id);

    }

    @Override
    @RequestMapping(
            value = {"batch/delete"},
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    @ResponseBody
    public Response batchDelete(@RequestParam(value = "ids",required = false) String[] ids) {
        List idList = Arrays.asList(ids);

        return  super.batchDelete(ids);
    }

}
