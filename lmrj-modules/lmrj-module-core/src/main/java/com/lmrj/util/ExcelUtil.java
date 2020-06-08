package com.lmrj.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * poi解析excel工具
 *
 * @author yhh
 */
public class ExcelUtil {


    /**
     * 解析xls（2003版）
     *
     * @throws IOException
     */
    public static List<List<String[]>> readXls(File file) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        List<List<String[]>> rowlists = new ArrayList<List<String[]>>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            if (hssfWorkbook.isSheetHidden(numSheet)) {
                continue;
            }
            if ("hidden".equals(hssfSheet.getSheetName())) {
                continue;
            }
            List rowlist = new ArrayList<String[]>();
            // 循环行Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                List<String> celllist = new ArrayList<String>();
                // 循环列Cell
                for (int cellNum = 0; cellNum < hssfRow.getLastCellNum(); cellNum++) {
                    HSSFCell hssfCell = hssfRow.getCell(cellNum);
//	          if(hssfCell == null){
//	            continue;
//	          }
                    celllist.add(getValue(hssfCell));
                }
                rowlist.add(celllist.toArray());
            }
            rowlists.add(rowlist);
        }
        return rowlists;
    }


    public static List<List<String[]>> readXlsx(File file) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
        List<List<String[]>> rowlists = new ArrayList<List<String[]>>();

        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            if ("hidden".equals(xssfSheet.getSheetName())) {
                continue;
            }
            List rowlist = new ArrayList<String[]>();
            // 循环行Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow == null) {
                    continue;
                }
                List<String> celllist = new ArrayList<String>();
                // 循环列Cell
                for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                    XSSFCell xssfCell = xssfRow.getCell(cellNum);
//		          if(xssfCell == null){
//		            continue;
//		          }
                    celllist.add(getValue(xssfCell));
                }
                rowlist.add(celllist.toArray());
            }
            rowlists.add(rowlist);
        }
        return rowlists;
    }


    /**
     * 解析excel （支持2003及2007）
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static List<List<String[]>> readExcel(String path) throws Exception {
        File file = new File(path);
        if (path.endsWith("xlsx")) {
            return readXlsx(file);
        } else if (path.endsWith("xls")) {
            return readXls(file);
        }
        return null;
    }

    /**
     * 解析excel （支持2003及2007）
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static List<List<String[]>> readExcel(File file, String fileName) throws Exception {
        if (fileName.endsWith("xlsx")) {
            return readXlsx(file);
        } else if (fileName.endsWith("xls")) {
            return readXls(file);
        }
        return null;
    }

    /**
     * 公共方法
     *
     * @param list
     * @param titleMap
     * @return
     */
    public static InputStream getExcelByList(List<Map> list, Map<String, String> titleMap) {
        // 创建一个HSSFWorkbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // 由HSSFWorkbook创建一个HSSFSheet
        HSSFSheet sheet = wb.createSheet();
        // 由HSSFSheet创建HSSFRow
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell;
        Iterator it = titleMap.keySet().iterator();
        int k = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            cell = row.createCell((int) k);
            cell.setCellValue(titleMap.get(key));
            k++;
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow((int) (i + 1));
            int g = 0;
            Iterator it2 = titleMap.keySet().iterator();
            while (it2.hasNext()) {
                cell = row.createCell((int) g);
                String key = (String) it2.next();
                Map<String, Object> map = list.get(i);
                cell.setCellValue(map.get(key) + "");
                g++;
            }
        }

        // 使用apache的commons-lang.jar产生随机的字符串作为文件名

        String fileName = RandomStringUtils.randomAlphanumeric(10);

        // 生成xls文件名必须要是随机的，确保每个线程访问都产生不同的文件
        StringBuffer sb = new StringBuffer(fileName);
        final File file = new File(sb.append(".xls").toString());
        try {
            OutputStream os = new FileOutputStream(file);
            wb.write(os);
            os.close();
            return new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

        return null;//返回的是一个输入流
    }

    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
        if (null == hssfCell) {
            return "";
        }
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
            try {
                return String.valueOf(hssfCell.getNumericCellValue());
            } catch (IllegalStateException e) {
                return String.valueOf(hssfCell.getRichStringCellValue());
            }
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    @SuppressWarnings("static-access")
    private static String getValue(XSSFCell xssfCell) {
        if (null == xssfCell) {
            return "";
        }
        if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(xssfCell)) {
                Date date = xssfCell.getDateCellValue();
                SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd");
                return si.format(date);
            }
            return String.valueOf(xssfCell.getNumericCellValue());
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }

    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
}
