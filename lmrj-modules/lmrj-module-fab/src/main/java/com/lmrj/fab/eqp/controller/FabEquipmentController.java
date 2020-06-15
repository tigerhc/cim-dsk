package com.lmrj.fab.eqp.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import com.lmrj.fab.eqp.service.IFabEquipmentService;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
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
 * @author: 张伟江
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
    String title = "设备信息";

    /**
     * 设备列表下拉框
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/eqpIdlist", method = { RequestMethod.GET, RequestMethod.POST })
    public void eqpIdlist(Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        List<String> eqpids = fabEquipmentService.eqpIdlist();
        List<Map> list = Lists.newArrayList();
        for (String eqpid : eqpids) {
            Map map = Maps.newHashMap();
            map.put("id", eqpid);
            list.add(map);
        }
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
    public void afterFind(FabEquipment entity) {
        //组织机构
        Organization office = OfficeUtils.getOffice(entity.getOfficeId());
        if(office != null){
            entity.setOfficeIds(office.getParentIds().replaceAll("/",",")+entity.getOfficeId());
        }
        UserUtil.updateUserName(entity);
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
            fabEquipmentService.insertBatch(list);
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
}
