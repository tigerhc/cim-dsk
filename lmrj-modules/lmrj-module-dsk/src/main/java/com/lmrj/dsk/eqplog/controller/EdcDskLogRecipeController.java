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
import com.lmrj.util.mapper.JsonUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
    @Value("${dsk.lineNo}")
    private String lineNo;
    @RequestMapping("selectEqpList")
    public Response tempEqpList(){
        Response rs = Response.ok();
        if(lineNo.equals("SIM")){//应苏科长要求,REFLOW1 显示REFLOW1,个别设备冰箱等显示描述
            String str = "[{\"id\":\"SIM-PRINTER1\",\"name\":\"SIM-印刷机1\"},\n" +
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
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("id", list);
        } else {
            String str = "[{\"id\":\"APJ-IGBT-SMT1\",\"name\":\"APJ-IGBT印刷贴片机\"},\n" +
                    "{\"id\":\"APJ-IGBT-2DAOI1\",\"name\":\"APJ-IGBT印刷 2D画像检查机\"},\n" +
                    "{\"id\":\"APJ-IGBT-REFLOW1\",\"name\":\"APJ-IGBT印刷立式固化炉\"},\n" +
                    "{\"id\":\"APJ-IGBT-3DAOI1\",\"name\":\"APJ-IGBT印刷-3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-IGBT-SORT1\",\"name\":\"APJ-IGBT印刷下料机\"},\n" +
                    "{\"id\":\"APJ-IGBT-SORT2\",\"name\":\"APJ-IGBT预结合上料机\"},\n" +
                    "{\"id\":\"APJ-IGBT-DM1\",\"name\":\"APJ-IGBT预结合晶圆贴片机\"},\n" +
                    "{\"id\":\"APJ-IGBT-SORT3\",\"name\":\"APJ-IGBT预结合下料机\"},\n" +
                    "{\"id\":\"APJ-FRD-SMT1\",\"name\":\"APJ-IGBT印刷贴片机\"},\n" +
                    "{\"id\":\"APJ-FRD-2DAOI1\",\"name\":\"APJ-FRD印刷 2D画像检查机\"},\n" +
                    "{\"id\":\"APJ-FRD-REFLOW1\",\"name\":\"APJ-FRD印刷立式固化炉\"},\n" +
                    "{\"id\":\"APJ-FRD-3DAOI1\",\"name\":\"APJ-FRD印刷-3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-FRD-SORT1\",\"name\":\"APJ-FRD印刷下料机\"},\n" +
                    "{\"id\":\"APJ-FRD-SORT2\",\"name\":\"APJ-FRD预结合上料机\"},\n" +
                    "{\"id\":\"APJ-FRD-DM1\",\"name\":\"APJ-FRD预结合晶圆贴片机\"},\n" +
                    "{\"id\":\"APJ-FRD-SORT3\",\"name\":\"APJ-FRD预结合下料机\"},\n" +
                    "{\"id\":\"APJ-HB1-SORT1\",\"name\":\"APJ-一次热压上料机\"},\n" +
                    "{\"id\":\"APJ-HB1-SINTERING1\",\"name\":\"APJ-一次热压机\"},\n" +
                    "{\"id\":\"APJ-HB1-3DAOI1\",\"name\":\"APJ-一次热压3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-HB1-SORT2\",\"name\":\"APJ-一次热压下料机\"},\n" +
                    "{\"id\":\"APJ-VI1\",\"name\":\"APJ-中间耐压检查一体机\"},\n" +
                    "{\"id\":\"APJ-DBCT-SORT1\",\"name\":\"APJ-上基板印刷上料机   \"},\n" +
                    "{\"id\":\"APJ-DBCT-2DAOI1\",\"name\":\"APJ-上基板印刷2D画像检查机\"},\n" +
                    "{\"id\":\"APJ-DBCT-REFLOW1\",\"name\":\"APJ-上基板印刷立式固化炉\"},\n" +
                    "{\"id\":\"APJ-DBCT-3DAOI1\",\"name\":\"APJ-上基板3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-DBCT-SORT2\",\"name\":\"APJ-上基板印刷下料机\"},\n" +
                    "{\"id\":\"APJ-DBCB-SORT1\",\"name\":\"APJ-下基板印刷上料机   \"},\n" +
                    "{\"id\":\"APJ-DBCB-2DAOI1\",\"name\":\"APJ-下基板印刷2D画像检查机\"},\n" +
                    "{\"id\":\"APJ-DBCB-REFLOW1\",\"name\":\"APJ-下基板印刷立式固化炉\"},\n" +
                    "{\"id\":\"APJ-DBCB-3DAOI1\",\"name\":\"APJ-下基板3D画像检查机\"},\n" +
                    "{\"id\":\"APJ-DBCB-SORT2\",\"name\":\"APJ-下基板印刷下料机\"},\n" +
                    "{\"id\":\"APJ-HB2-SORT1\",\"name\":\"APJ-二次热压上料机\"},\n" +
                    "{\"id\":\"APJ-HB2-DISPENSING1\",\"name\":\"APJ-二次热压点胶机1\"},\n" +
                    "{\"id\":\"APJ-HB2-DISPENSING2\",\"name\":\"APJ-二次热压点胶机2\"},\n" +
                    "{\"id\":\"APJ-HB2-DISPENSING3\",\"name\":\"APJ-二次热压点胶机3\"},\n" +
                    "{\"id\":\"APJ-HB2-2DAOI1\",\"name\":\"APJ-二次热压2D画像检查机1\"},\n" +
                    "{\"id\":\"APJ-HB2-SMT1\",\"name\":\"APJ-二次热压贴片机1\"},\n" +
                    "{\"id\":\"APJ-HB2-SMT2\",\"name\":\"APJ-二次热压贴片机2\"},\n" +
                    "{\"id\":\"APJ-HB2-2DAOI2\",\"name\":\"APJ-二次热压2D画像检查机2\"},\n" +
                    "{\"id\":\"APJ-HB2-ASSEMBLY1\",\"name\":\"APJ-二次热压组立机\"},\n" +
                    "{\"id\":\"APJ-HB2-SINTERING1\",\"name\":\"APJ-二次热压机\"},\n" +
                    "{\"id\":\"APJ-HB2-SORT2\",\"name\":\"APJ-二次热压下料机\"},\n" +
                    "{\"id\":\"APJ-XRAY1\",\"name\":\"APJ-X射线检查机\"},\n" +
                    "{\"id\":\"APJ-CLEAN-US1\",\"name\":\"APJ-US1洗净机\"},\n" +
                    "{\"id\":\"APJ-CLEAN-JET1\",\"name\":\"APJ-JET洗净机\"},\n" +
                    "{\"id\":\"APJ-TRM1\",\"name\":\"APJ-TRM1\"},\n" +
                    "{\"id\":\"APJ-AT1\",\"name\":\"APJ-耐老化试验搬送机\"},\n" +
                    "{\"id\":\"APJ-LF1\",\"name\":\"APJ-分离检查机\"},\n" +
                    "{\"id\":\"APJ-HTRT1\",\"name\":\"APJ-高温室温检查机\"}]";
            List<Map<String, Object>> list = JsonUtil.from(str, ArrayList.class);
            rs.put("eqpId", list);
        }
        return rs;
    }

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

