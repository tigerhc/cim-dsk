package com.lmrj.ms.thrust.controller;

import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.ms.thrust.entity.MsMeasureThrust;
import com.lmrj.ms.thrust.service.IMsMeasureThrustService;
import com.lmrj.util.lang.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.ms.config.controller
 * @title: ms_measure_config控制器
 * @description: ms_measure_config控制器
 * @author: 张伟江
 * @date: 2020-06-06 18:32:57
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("ms/msmeasurethrust")
@ViewPrefix("ms/msmeasurethrust")
@RequiresPathPermission("ms:msmeasurethrust")
@LogAspectj(title = "ms_measure_thrust")
public class MsMeasureThrustController extends BaseCRUDController<MsMeasureThrust> {
    @Autowired
    IMsMeasureThrustService msMeasureThrustService;
    @RequestMapping(value = "/exportLogThrust", method = { RequestMethod.GET, RequestMethod.POST })
    public Response exportData(String productionName ,String startTime,String endTime, HttpServletRequest request, HttpServletResponse response){
        Response res = Response.ok("导出成功");
        try {
            List<MsMeasureThrust> dataList = msMeasureThrustService.findDataByTime(productionName,startTime,endTime);
            String url = "E:\\CIM\\cim-dsk-root\\logs\\推力拉力excel模板.xlsx";
            File file = new File(url);
            String fileName = file.getName();
            //获取文件类型
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            System.out.println(" **** fileType:" + fileType);
            //获取输入流
            InputStream stream = new FileInputStream(file);
            //获取工作薄
            Workbook xssfWorkbook = null;
            if (fileType.equals("xls")) {
                xssfWorkbook = new HSSFWorkbook(stream);
            } else if (fileType.equals("xlsx")) {
                xssfWorkbook = new XSSFWorkbook(stream);
            } else {
                return Response.error("模板文件格式不正确! 文件名称："+fileName);
            }
            Sheet thrustSheet = xssfWorkbook.getSheetAt(1);//推力页
            thrustDataParse(thrustSheet,dataList);
            Sheet pullSheet = xssfWorkbook.getSheetAt(0);//拉力页
            pullDataParse(pullSheet,dataList);
            //分别得到最后一行的行号，和一条记录的最后一个单元格
           /* FileOutputStream out=new FileOutputStream(url); //向d://test.xls中写数据
            out.flush();
            xssfWorkbook.write(out);
            out.close();*/
            FileOutputStream fos = new FileOutputStream("D:/ExcelExportForMap.xlsx");
            xssfWorkbook.write(fos);
            fos.close();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            xssfWorkbook.write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtil.bytesToHexString2(bytes);
            res.put("bytes", bytesRes);
            return res;
        } catch (Exception var16) {
            var16.printStackTrace();
            return Response.error(999998, "导出失败");
        }
    }
    //拉力sheet页
    public Sheet pullDataParse(Sheet pullSheet,List<MsMeasureThrust> msMeasureThrustList){
        Row lotrow=pullSheet.getRow(52);//机种和批量行
        Row eqpIdrow=pullSheet.getRow(70);//设备名称行
        Row opIdrow=pullSheet.getRow(71);//操作员编号行
        for (int i = 0; i < msMeasureThrustList.size(); i++) {
            MsMeasureThrust msMeasureThrust= msMeasureThrustList.get(i);
            String nameAndLot = msMeasureThrust.getProductionName().substring(0,8)+"\n"+msMeasureThrust.getLotNo();
            lotrow.getCell((i+1)).setCellValue(nameAndLot);
            eqpIdrow.getCell((i+1)).setCellValue(msMeasureThrust.getEqpId());
            opIdrow.getCell((i+1)).setCellValue(Integer.parseInt(msMeasureThrust.getOpId()));
        }
        //将拉力数据放入指定位置
        for (int i = 53; i <65 ; i++) {
            Row row=pullSheet.getRow(i);//数据填入起始行数
            datapPatse(row,msMeasureThrustList,i,1);
        }
        //重新对公式单元格进行格式设置
        pullSheet.setForceFormulaRecalculation(true);
        return pullSheet;
    }
    //推力力sheet页
    public Sheet thrustDataParse(Sheet thrustSheet,List<MsMeasureThrust> msMeasureThrustList){
        Row lotrow=thrustSheet.getRow(52);//机种和批量行
        Row eqpIdrow=thrustSheet.getRow(82);//设备名称行
        Row opIdrow=thrustSheet.getRow(83);//操作员编号行
        for (int i = 0; i < msMeasureThrustList.size(); i++) {
            MsMeasureThrust msMeasureThrust= msMeasureThrustList.get(i);
            String nameAndLot = msMeasureThrust.getProductionName().substring(0,8)+"\n"+msMeasureThrust.getLotNo();
            lotrow.getCell((i+1)).setCellValue(nameAndLot);
            eqpIdrow.getCell((i+1)).setCellValue(msMeasureThrust.getEqpId());
            opIdrow.getCell((i+1)).setCellValue(Integer.parseInt(msMeasureThrust.getOpId()));
        }
        //将推力数据放入指定位置
        for (int i = 53; i <77 ; i++) {
            Row row=thrustSheet.getRow(i);//数据填入起始行数
            datapPatse(row,msMeasureThrustList,i,0);
        }
        thrustSheet.setForceFormulaRecalculation(true);
        return thrustSheet;
    }
    //将推力或拉力指定位置数据拼接为一行
    public void datapPatse(Row row,List<MsMeasureThrust> msMeasureThrustList,int rowSize,int flag){
        int dateGetIndex = rowSize-53;//获取推力数据的thrusts元素下表
        for (int i = 0; i < msMeasureThrustList.size(); i++) {
            MsMeasureThrust msMeasureThrust= msMeasureThrustList.get(i);
            String data = "";
            if(flag==1){//推力
                data = msMeasureThrust.getThrust().replace("~",",").replace(",-","");
            }else if(flag == 0){//拉力
                data = msMeasureThrust.getPull().replace("~",",").replace(",-","");
            }
            String datass[] = data.split(",");
            if(datass.length>dateGetIndex){
                if("-".equals(datass[dateGetIndex])){
                    row.getCell((i+1)).setCellValue(datass[dateGetIndex]);
                }else {
                    row.getCell((i+1)).setCellValue((double) Math.round(Double.valueOf(datass[dateGetIndex]) * 10) / 10);
                }
            }
        }
    }

}
