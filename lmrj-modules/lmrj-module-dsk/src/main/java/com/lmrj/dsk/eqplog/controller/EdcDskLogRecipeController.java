package com.lmrj.dsk.eqplog.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipeBody;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeBodyService;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.controller
 * @title: edc_dsk_log_recipe控制器
 * @description: edc_dsk_log_recipe控制器
 * @author: 张伟江
 * @date: 2020-04-17 17:21:17
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("dsk/edcdsklogrecipe")
@ViewPrefix("dsk/edcdsklogrecipe")
@RequiresPathPermission("dsk:edcdsklogrecipe")
@LogAspectj(title = "edc_dsk_log_recipe")
public class EdcDskLogRecipeController extends BaseCRUDController<EdcDskLogRecipe> {
    @Autowired
    IEdcDskLogRecipeService iEdcDskLogRecipeService;
    @Autowired
    IEdcDskLogRecipeBodyService iEdcDskLogRecipeBodyService;

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("配方日志记录", queryable,  propertyPreFilterable,  request,  response);
    }



    @RequestMapping(value = "/exportLogRecipe", method = { RequestMethod.GET, RequestMethod.POST })
    public Response exportDetail(String id ,HttpServletRequest request, HttpServletResponse response){
        String title = "配方日志记录";
        Response res = Response.ok("导出成功");
        try {
            List<Map<String, String>> dataList = new LinkedList<>();
            Map<String, String> data = new HashMap<>() ;
              String[] ids =id.split(",");
            for (int i = 0; i <ids.length ; i++) {
                EdcDskLogRecipe edcDskLogRecipe=iEdcDskLogRecipeService.findById(ids[i]);
                data = new HashMap<>() ;
                String EqpId =edcDskLogRecipe.getEqpId();
                String RecipeCode =edcDskLogRecipe.getRecipeCode();
                Date date =edcDskLogRecipe.getStartTime();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String StartTime = ft.format(date);
                data.put("1",EqpId);
                data.put("2",RecipeCode);
                data.put("3",StartTime);
                dataList.add(data);
                List<EdcDskLogRecipeBody> detail = iEdcDskLogRecipeBodyService.selectParamList(ids[i]);
                for (EdcDskLogRecipeBody edcDskLogRecipeBody:detail){
                    Map<String, String> ele = new HashMap<>() ;
                    String  paraname=edcDskLogRecipeBody.getParaName();
                    String oldValue = iEdcDskLogRecipeService.findOldData(EqpId,StartTime,paraname);
                    ele.put("1",edcDskLogRecipeBody.getParaName());
                    ele.put("2",edcDskLogRecipeBody.getSetValue());
                    ele.put("3",oldValue);
                    if (!edcDskLogRecipeBody.getSetValue().equals(oldValue)){
                        ele.put("4","改变");
                    }
                    dataList.add(ele);
                }
                data = new HashMap<>() ;
                data.put("1", "");
                data.put("2", "");
                data.put("3", "");
                dataList.add(data);
            }
            List<ExcelExportEntity> keyList= new LinkedList<>();
            ExcelExportEntity key = new ExcelExportEntity("参数名称","1");
            keyList.add(key);
            ExcelExportEntity key2 = new ExcelExportEntity("New value","2");
            keyList.add(key2);
            ExcelExportEntity key3 = new ExcelExportEntity("Old value","3");
            keyList.add(key3);
            ExcelExportEntity key4 = new ExcelExportEntity("设定值是否改变","4");
            keyList.add(key4);
            Workbook book = ExcelExportUtil.exportExcel(new ExportParams("配方日志记录","量测详细信息"),keyList,dataList);
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

