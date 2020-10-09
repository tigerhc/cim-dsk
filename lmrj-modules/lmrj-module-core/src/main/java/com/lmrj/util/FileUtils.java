package com.lmrj.util;

import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.lang.StringUtil;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.util.*;

/**
 * @author wangdong
 *  2020-10-08
 */
public class FileUtils {
    /** 获得文件夹中所有的文件
     * @param directoryPath ：指定路径
     * @param isHasPath : 返回的文件名中是否含有目录，true：含有；false：不含有
     * @return  ：文件名的集合
     */
    public static List<String> getFileNames(String directoryPath, boolean isHasPath){
        List<String> list = new ArrayList<>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                if(isHasPath){
                    list.add(file.getAbsolutePath());
                } else{
                    list.add(file.getName());
                }
            }
        }
        return list;
    }

    /**获得文件夹中所有的文件信息
     * @param directoryPath ：指定路径
     * @return ：[{fileName:文件名,createTime:创建时间}]
     */
    public static List<Map<String, Object>> getFileInfos(String directoryPath, String startDate, String endDate){
        List<Map<String, Object>> list = new ArrayList<>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        long startLimit = 0;
        long endLimit = 0;
        if(!StringUtil.isEmpty(startDate)){
            startLimit = DateUtil.parseDate(startDate).getTime();
        }
        if(!StringUtil.isEmpty(endDate)){
            endLimit = DateUtil.parseDate(endDate).getTime();
        }

        for (File file : files) {
            if (!file.isDirectory()) {
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("fileName", file.getName());
                fileInfo.put("createTime", getFileCreateTime(file.getAbsolutePath()));
                long tm = getFileTime(file.getAbsolutePath());
                fileInfo.put("timeMillis", tm);
                if(startLimit != 0){
                    if(tm<startLimit){
                        continue;
                    }
                }
                if(endLimit !=0){
                    if(tm>endLimit){
                        continue;
                    }
                }
                if(list.size() == 0){
                    list.add(fileInfo);
                } else {
                    for(int i=0;i<list.size(); i++){
                        long item = MapUtils.getLongValue(list.get(i),"timeMillis");
                        if(item>tm){
                            list.add(i,fileInfo);
                            break;
                        }else if(i == list.size()-1){
                            list.add(fileInfo);
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

    /** 获取文件的创建时间
     * @param filePath ： 文件的全路径
     * @return ：文件的创建时间
     */
    public static String getFileCreateTime(String filePath){
        try {
            //Linux系统
//            Path path= Paths.get(filePath);
//            BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
//            BasicFileAttributes attr = basicview.readAttributes();
//            Date time = new Date(attr.creationTime().toMillis());

            //windows系统
            File file = new File(filePath);
            Date time = new Date(file.lastModified());
            return DateUtil.formatTime(time);
        } catch (Exception e) {
            e.printStackTrace();
//            File file = new File(filePath);
//            return file.lastModified();
            return "";
        }
    }

    public static Long getFileTime(String filePath){
        try {
            //Linux系统
//            Path path= Paths.get(filePath);
//            BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
//            BasicFileAttributes attr = basicview.readAttributes();
//            Date time = new Date(attr.creationTime().toMillis());

            //windows系统
            File file = new File(filePath);
            return file.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
//            File file = new File(filePath);
//            return file.lastModified();
            return 0L;
        }
    }

    /** 测试
     */
    public static void main(String[] args){
        String path = "d:\\backup\\SIM\\SIM6812M(E)D-URA F2971";//含有文件的路径
//        String path2 = "d:\\CIM";//不含有文件的路径
//        List<String> res = getFileNames(path, false);
//        System.out.println(res);

        List<Map<String, Object>> res2 = getFileInfos(path,"2020-10-07","2020-10-08");
        System.out.println(res2);
        System.out.println(res2.size());
    }
}
