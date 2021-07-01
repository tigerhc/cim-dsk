package com.lmrj.cim.quartz;

import com.lmrj.cim.CimBootApplication;
import com.lmrj.ms.thrust.entity.MsMeasureThrust;
import com.lmrj.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.List;

@SpringBootTest(classes = CimBootApplication.class)
@RunWith(SpringRunner.class)
public class ExcelWriteTest {

    @Test
    public void filetest(){

        String url = "C:\\Users\\86187_dar6n7g\\Desktop\\test\\excel模板.xlsx";
        FileInputStream fs;
        try {
            InputStream inp=ExcelUtil.class.getResourceAsStream(url);
            XSSFWorkbook wb = new XSSFWorkbook(inp);
            SXSSFWorkbook swb = new SXSSFWorkbook(wb,100);
            XSSFWorkbook xssfWorkbook = swb.getXSSFWorkbook();
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow row=sheet.getRow(54);
            //分别得到最后一行的行号，和一条记录的最后一个单元格
            FileOutputStream out=new FileOutputStream(url); //向d://test.xls中写数据
            row.getCell(2).setCellValue(9.9);
            out.flush();
            wb.write(out);
            out.close();
            System.out.println(row.getPhysicalNumberOfCells()+" "+row.getLastCellNum());
        } catch (IOException e) {
            e.printStackTrace();
        } //获取d://test.xls
    }


    public static void main(String[] args) throws IOException {
        String url = "C:\\Users\\86187_dar6n7g\\Desktop\\test\\excel模板.xlsx";
        File file = new File("C:\\Users\\86187_dar6n7g\\Desktop\\test\\excel模板.xlsx");
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
            System.out.println("您输入的excel格式不正确");
        }

        Sheet sheet = xssfWorkbook.getSheetAt(0);
        Row row1=sheet.getRow(53);
        Row row2=sheet.getRow(54);
        //分别得到最后一行的行号，和一条记录的最后一个单元格
        FileOutputStream out=new FileOutputStream(url); //向d://test.xls中写数据
        row2.getCell(2).setCellValue(9.9);
        out.flush();
        xssfWorkbook.write(out);
        out.close();
        System.out.println(row2.getPhysicalNumberOfCells()+" "+row2.getLastCellNum());
    }


    //拉力sheet页
    public Sheet pullDataParse(Sheet pullSheet, List<MsMeasureThrust> msMeasureThrustList){
        Row lotrow=pullSheet.getRow(52);//机种和批量行
        Row eqpIdrow=pullSheet.getRow(70);//设备名称行
        Row opIdrow=pullSheet.getRow(71);//操作员编号行
        for (int i = 0; i <= msMeasureThrustList.size(); i++) {
            MsMeasureThrust msMeasureThrust= msMeasureThrustList.get(i);
            String nameAndLot = msMeasureThrust.getProductionName().substring(0,8)+"\n"+msMeasureThrust.getLotNo();
            lotrow.getCell((i+1)).setCellValue(nameAndLot);
            eqpIdrow.getCell((i+1)).setCellValue(msMeasureThrust.getEqpId());
            opIdrow.getCell((i+1)).setCellValue(msMeasureThrust.getOpId());
        }
        //将拉力数据放入指定位置
        for (int i = 53; i <65 ; i++) {
            Row row=pullSheet.getRow(i);//数据填入起始行数
            datapPatse(row,msMeasureThrustList,i,1);
            System.out.println(row);
        }
        return pullSheet;
    }
    //推力力sheet页
    public Sheet thrustDataParse(Sheet thrustSheet,List<MsMeasureThrust> msMeasureThrustList){
        Row lotrow=thrustSheet.getRow(52);//机种和批量行
        Row eqpIdrow=thrustSheet.getRow(70);//设备名称行
        Row opIdrow=thrustSheet.getRow(71);//操作员编号行
        for (int i = 0; i <= msMeasureThrustList.size(); i++) {
            MsMeasureThrust msMeasureThrust= msMeasureThrustList.get(i);
            String nameAndLot = msMeasureThrust.getProductionName().substring(0,8)+"\n"+msMeasureThrust.getLotNo();
            lotrow.getCell((i+1)).setCellValue(nameAndLot);
            eqpIdrow.getCell((i+1)).setCellValue(msMeasureThrust.getEqpId());
            opIdrow.getCell((i+1)).setCellValue(msMeasureThrust.getOpId());
        }
        //将推力数据放入指定位置
        for (int i = 53; i <77 ; i++) {
            Row row=thrustSheet.getRow(i);//数据填入起始行数
            datapPatse(row,msMeasureThrustList,i,0);
            System.out.println(row);
        }
        return thrustSheet;
    }
    //将推力或拉力指定位置数据拼接为一行
    public void datapPatse(Row row, List<MsMeasureThrust> msMeasureThrustList, int rowSize, int flag){
        int dateGetIndex = rowSize-53;//获取推力数据的thrusts元素下表
        for (int i = 0; i < msMeasureThrustList.size(); i++) {
            MsMeasureThrust msMeasureThrust= msMeasureThrustList.get(i);
            String data = "";
            if(flag==0){//推力
                data = msMeasureThrust.getThrust().replace("~",",");
            }else if(flag == 1){//拉力
                data = msMeasureThrust.getPull().replace("~",",");
            }
            String datass[] = data.split(",");
            row.getCell((i+1)).setCellValue(datass[dateGetIndex]);
        }
    }
}
