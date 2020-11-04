package com.lmrj.dsk.eqplog.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.dsk.eqplog.entity.EdcDskLogRecipe;
import com.lmrj.dsk.eqplog.service.IEdcDskLogRecipeService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


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

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("配方日志记录", queryable,  propertyPreFilterable,  request,  response);
    }

    @RequestMapping(value = "/selectExport")
    public String selectExport(HttpServletRequest httpServletRequest, HttpServletResponse response)throws IOException {
//        EdcDskLogRecipe edcDskLogRecipe =iEdcDskLogRecipeService.findById("75537dfc1c544ddda51b1ca94dab2004");
//         return edcDskLogRecipe;
//        String idsw = httpServletRequest.getParameter("ids");
        //创建一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建一个sheet
        HSSFSheet sheet = wb.createSheet("用户信息");
        sheet.setColumnWidth(0, 5000); //设置第一列的宽度
        sheet.setColumnWidth(1, 5000); //设置第二列的宽度
        sheet.setColumnWidth(2, 5000); //设置第三列的宽度


        //创建单元格样式对象
        HSSFCellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFillBackgroundColor((short)8);

        //创建字体对象
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);  //字体宽度
        font.setFontName("宋体");  //设置字体类型
        font.setItalic(false); //这只是否为斜体
        //设置字体样式
        style.setFont(font);


        //创建一行
        HSSFRow row0 = sheet.createRow(0);
        //设置行样式
        row0.setRowStyle(style);

        HSSFCell cell0_0 = row0.createCell(0);
        cell0_0.setCellStyle(style); //设置单元格样式
        cell0_0.setCellValue("延误类型");
        HSSFCell cell0_1 = row0.createCell(1);
        cell0_1.setCellValue("航班号");
        HSSFCell cell0_2 = row0.createCell(2);
        cell0_2.setCellValue("航班日期");
        HSSFCell cell0_3 = row0.createCell(3);
        cell0_3.setCellValue("起飞地");
        HSSFCell cell0_4 = row0.createCell(4);
        cell0_4.setCellValue("目的地");
        HSSFCell cell0_5 = row0.createCell(5);
        cell0_5.setCellValue("机型/机号");
        HSSFCell cell0_6 = row0.createCell(6);
        cell0_6.setCellValue("计划起飞时间");
        HSSFCell cell0_7 = row0.createCell(7);
        cell0_7.setCellValue("实际起飞时间");
        HSSFCell cell0_8 = row0.createCell(8);
        cell0_8.setCellValue("实际到达时间");
        HSSFCell cell0_9 = row0.createCell(9);
        cell0_9.setCellValue("代班乘务长");
        HSSFCell cell0_10 = row0.createCell(10);
        cell0_10.setCellValue("所属部别");
        HSSFCell cell0_11 = row0.createCell(11);
        cell0_11.setCellValue("分舱位旅客人数");
        HSSFCell cell0_12 = row0.createCell(12);
        cell0_12.setCellValue("VIP/CIP数量");
        HSSFCell cell0_13 = row0.createCell(13);
        cell0_13.setCellValue("提交人工号");
        HSSFCell cell0_14 = row0.createCell(14);
        cell0_14.setCellValue("提交人");


//        PageObject pageObject = this.flightManageService.getListSelect(ids);
//        @SuppressWarnings("unchecked")
//        List<Flight>  list= pageObject.getRows();
//
//        for(int i = 0; i < list.size(); i ++){
//            //创建行
//            HSSFRow row = sheet.createRow(i+1);
//            for(int j = 0; j < 15; j++){
//                HSSFCell cell = row.createCell(j);
//                Flight flight =list.get(i);
//
//
//                switch(j){
//                    case 0 :
//                        String FlightDelayType = flight.getFlightDelayType();
//                        cell.setCellValue(FlightDelayType);
//                        break; //可选
//                    case 1 :
//                        String FlightNumber = flight.getFlightNumber();
//                        cell.setCellValue(FlightNumber);
//                        break; //可选
//                    case 2 :
//                        String FlightDate = flight.getFlightDate();
//                        cell.setCellValue(FlightDate);
//                        break; //可选
//                    case 3 :
//                        String FlightRange = flight.getFlightFromPlace();
//                        cell.setCellValue(FlightRange);
//                        break; //可选
//                    case 4 :
//                        String FlightToPlace = flight.getFlightToPlace();
//                        cell.setCellValue(FlightToPlace);
//                        break; //可选
//                    case 5 :
//                        String AircraftType = flight.getFlightAircraftType();
//                        cell.setCellValue(AircraftType);
//                        break; //可选
//                    case 6 :
//                        String FlightPlanTime = flight.getFlightPlanTime();
//                        cell.setCellValue(FlightPlanTime);
//                        break; //可选
//                    case 7 :
//                        String FlightFromTime = flight.getFlightFromTime();
//                        cell.setCellValue(FlightFromTime);
//                        break; //可选
//                    case 8 :
//                        String FlightToTime = flight.getFlightToTime();
//                        cell.setCellValue(FlightToTime);
//                        break; //可选
//                    case 9 :
//                        String FlightReplaceChief = flight.getFlightReplaceChief();
//                        cell.setCellValue(FlightReplaceChief);
//                        break; //可选
//                    case 10 :
//                        String FlightBelongtoPart = flight.getFlightBelongtoPart();
//                        cell.setCellValue(FlightBelongtoPart);
//                        break; //可选
//                    case 11 :
//                        String FlightCabinNumber = flight.getFlightCabinNumber();
//                        cell.setCellValue(FlightCabinNumber);
//                        break; //可选
//                    case 12 :
//                        String FlightVipNumber = flight.getFlightVipNumber();
//                        cell.setCellValue(FlightVipNumber);
//                        break; //可选
//                    case 13 :
//                        String FlightSubmitNumber = flight.getFlightSubmitNumber();
//                        cell.setCellValue(FlightSubmitNumber);
//                        break; //可选
//                    case 14 :
//                        String FlightSubmitName = flight.getFlightSubmitName();
//                        cell.setCellValue(FlightSubmitName);
//                        break; //可选
//
//                }
//
//
//
//
//
//
//            }
//        }

        ServletOutputStream  output =  response.getOutputStream();
//        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment;filename=students.xls");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        wb.write(output);
        response.flushBuffer();
        output.close();

//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");// 设置contentType为excel格式
//        response.setHeader("Content-disposition", "attachment;filename=aaaaaa.xls" );//默认Excel名称
//        response.flushBuffer();
//
//        wb.write(response.getOutputStream());
//        output.close();








      return "导出";
//        FileOutputStream fos  = null;
//        try {
//            fos = new FileOutputStream(new File("D:\\航班信息表.xls"));
//            wb.write(fos);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally{
//            if(fos != null){
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        JSONObject js =new JSONObject();
//        js.put("status", 1);
//        return js.toJSONString();
//

    }


//	public void selectExport(HttpServletRequest request, HttpServletResponse response)
//			throws RestException, IOException {
//		log.info("导出");
//
//        String id = request.getParameter("id");
//	    this.flightManageService.selectExport(id, response);
    }

