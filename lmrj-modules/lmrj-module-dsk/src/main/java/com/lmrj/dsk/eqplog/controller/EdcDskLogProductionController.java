package com.lmrj.dsk.eqplog.controller;

import com.google.common.collect.Lists;
import com.lmrj.common.http.Response;
import com.lmrj.common.mvc.annotation.ViewPrefix;
import com.lmrj.common.mybatis.mvc.controller.BaseCRUDController;
import com.lmrj.common.query.data.PropertyPreFilterable;
import com.lmrj.common.query.data.Queryable;
import com.lmrj.common.security.shiro.authz.annotation.RequiresPathPermission;
import com.lmrj.core.log.LogAspectj;
import com.lmrj.dsk.eqplog.entity.EdcDskLogProduction;
import com.lmrj.dsk.eqplog.service.IEdcDskLogProductionService;
import com.lmrj.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.dsk.eqplog.controller
 * @title: edc_dsk_log_production控制器
 * @description: edc_dsk_log_production控制器
 * @author: 张伟江
 * @date: 2020-04-14 10:10:00
 * @copyright: 2019 www.lmrj.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("dsk/edcdsklogproduction")
@ViewPrefix("dsk/edcdsklogproduction")
@RequiresPathPermission("dsk:edcdsklogproduction")
@LogAspectj(title = "edc_dsk_log_production")
@Slf4j
@EnableScheduling
public class EdcDskLogProductionController extends BaseCRUDController<EdcDskLogProduction> {

    @Override
    @GetMapping("export")
    //@LogAspectj(logType = LogType.EXPORT)
//    @RequiresMethodPermissions("export")
    public Response export(Queryable queryable, PropertyPreFilterable propertyPreFilterable, HttpServletRequest request, HttpServletResponse response) {
        return doExport("生产日志记录", queryable,  propertyPreFilterable,  request,  response);
    }
    public String filePath="E:\\FTP\\EQUIPMENT\\SIM\\2020\\PRINTER\\SIM-PRINTER1\\07";
    //获取文件
    public List<File> getFileList(String strPath) throws Exception {
        List<File> fileList = (List<File>) FileUtil.listFiles(new File(strPath), new String[]{"csv"}, false);
        List<File> filenewList = Lists.newArrayList();
        for (File file : fileList) {
            if (!file.getName().endsWith("-R.csv") && file.getName().contains("Productionlog")) {
                log.info("文件获取"+file.getName());
                filenewList.add(file);
            }
        }
        //对list中文件按文件名中的时间进行排序
        return filenewList;
    }
    //LotNo查询
    @Autowired
    IEdcDskLogProductionService edcDskLogProductionService;
    public EdcDskLogProduction findLotNo(String starTime, String eqpId){
        EdcDskLogProduction edcDskLogProduction=edcDskLogProductionService.findLotNo(starTime,eqpId);
        if(Objects.isNull(edcDskLogProductionService.findLotNo(starTime,eqpId))){
           log.info("edcDskLogProductionService.findLotNo(time,eqpid)为空");
        }
        return edcDskLogProduction;
    }

    //文件排序
    public void orderBytime(List<File> fileList){
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o2.lastModified() > o1.lastModified()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        log.info("文件排序结束");
    }

    //日期格式转换
    public Date chanangeDate(Date date) throws ParseException {
        String pattern="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(date);
        return new SimpleDateFormat(pattern).parse(dateString);
    }
    //内容修改 更改文件中批次
    public void fixfile(List<File> fileList) throws Exception {
        //记录每次循环的文件的名称及批次
        String lastlotNo=null;
        String lastfileName=null;
        int x = 0;
        log.info(" 开始解析");
        for (int i=0;i<=fileList.size()-1;i++){
            if(!Objects.isNull(lastlotNo)){
                log.info("上一文件lotNo:              "+lastlotNo);
                log.info("上一文件fileName:           "+lastfileName);
            }
            File nowFile = fileList.get(i);
            log.info("当前文件名：    "+nowFile.getName());
            List<String> lines = FileUtil.readLines(nowFile,"GBK");
            String data[]=lines.get(1).split(",");

            String eqpId=data[0];
            String startTime=data[4];
            EdcDskLogProduction edcDskLogProduction=this.findLotNo(startTime,eqpId);
            //如果数据库中没有满足条件的数据则不执行
            if(!Objects.isNull(edcDskLogProduction)){
                List<String> newlines = new ArrayList<>();
                newlines.add(lines.get(0));
                for (int j = 1; j <= lines.size()-1; j++) {
                    String pattern="yyyy-MM-dd HH:mm:ss";
                    String data1[] = lines.get(j).split(",");
                    if(j==lines.size()-1){
                        //判断当前行工作时间段在不在数据库时间段内                  最后一行开始时间                                                                                   最后一行结束时间
                        if(this.chanangeDate(edcDskLogProduction.getStartTime()).before(new SimpleDateFormat(pattern).parse(data1[4])) && this.chanangeDate(edcDskLogProduction.getEndTime()).after(new SimpleDateFormat(pattern).parse(data1[5]))){
                            if(!data1[14].equals(edcDskLogProduction.getLotNo())){
                                Arrays.fill(data1, 14, 15, edcDskLogProduction.getLotNo());
                                log.info("当前行解析完成");
                            }
                        }
                    }else{
                        String data2[] = lines.get(j+1).split(",");
                        //判断当前行工作时间段在不在数据库时间段内                  当前行开始时间                                                                                   下一行开始时间
                        if(this.chanangeDate(edcDskLogProduction.getStartTime()).before(new SimpleDateFormat(pattern).parse(data1[4])) && this.chanangeDate(edcDskLogProduction.getEndTime()).after(new SimpleDateFormat(pattern).parse(data2[4]))){
                            if(!data1[14].equals(edcDskLogProduction.getLotNo())){
                                Arrays.fill(data1, 14, 15, edcDskLogProduction.getLotNo());
                                log.info("当前行解析完成");
                            }
                        }
                    }
                    String str = "";
                    for (int l = 0; l < data1.length; l++) {
                        String str1 = data1[l];
                        if (l == 0) {
                            str = str1;
                        }else if(edcDskLogProduction.getLotNo().equals(lastlotNo) && l == 7){
                            str = str + "," + (Integer.parseInt(str1)+x);
                        }else{
                            str = str + "," + str1;
                        }
                        log.info(str);
                    }
                    newlines.add(str);
                }
                //备份
                FileUtil.move(filePath+"\\"+nowFile.getName(),"E:\\fileback_up"+"\\"+nowFile.getName(),false);
                //生成修改后的新文件并改名
                if(edcDskLogProduction.getLotNo().equals(lastlotNo)){
                    log.info("该批次文件已存在，续写至已创建文件");
                    newlines.remove(0);
                    FileUtil.writeLines(new File(lastfileName),"UTF-8",newlines,true);
                }else{
                    File newFile = new File(filePath +"\\"+ nowFile.getName());
                    FileUtil.writeLines(newFile,"GBK",newlines);
                    //在给文件改名时记录当前文件数据的批次和文件名，与下一文件批次对比
                    lastfileName=this.changename(newFile);
                    lastlotNo=edcDskLogProduction.getLotNo();
                    //取出当前文件最后一行批量内连番
                    String data2[]=lines.get(lines.size()-1).split(",");
                    x=Integer.parseInt(data2[7]);
                }
            }else {
                log.info("数据库查询无数据");
            }
        }
    }
    //修改文件名称 在名称尾部加"-R"
    public String changename(File newFile) {
        String fileName = newFile.getName();
        fileName = fileName.substring(0, fileName.length() -4);
        if (newFile.renameTo(new File( filePath+"\\"+ fileName + "-R.csv"))) {
            log.info("文件名修改成功");
        }
        return  filePath+"\\"+fileName + "-R.csv";
    }
    /*@Scheduled(cron = "0 30 8 * * ? ")*/
    public  void FileAnalysistest()  throws Exception{
        List<File> fileList = this.getFileList(filePath);
        this.orderBytime(fileList);
        this.fixfile(fileList);
    }
}
