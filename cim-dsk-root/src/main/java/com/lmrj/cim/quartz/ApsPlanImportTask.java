package com.lmrj.cim.quartz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldDetailService;
import com.lmrj.aps.plan.service.IApsPlanPdtYieldService;
import com.lmrj.util.ExcelUtil;
import com.lmrj.util.calendar.DateUtil;
import com.lmrj.util.file.FileUtil;
import com.lmrj.util.lang.StringUtil;
import com.lmrj.util.mapper.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ApsPlanImportTask {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private IApsPlanPdtYieldService apsPlanPdtYieldService;

    @Autowired
    private IApsPlanPdtYieldDetailService apsPlanPdtYieldDetailService;

    @Value("${aps.dir}")
    public String dir;

    public void rundskAps() {
        log.info("定时任务开始执行");
        String[] extensions = {"xls"};
        String year = DateUtil.getYear().substring(2,4);
        dir = dir+"日次計画"+year+"年\\";
        List<File> files = (List<File>) FileUtil.listFiles(new File(dir), extensions,false);
        if(files.size() == 0){
            return;
        }
        File lastFile = files.get(0);
        for (File file : files) {
            if(lastFile.lastModified() < file.lastModified()){
                lastFile = file;
            }
        }
        try {
            this.readApsPlan(lastFile.getAbsolutePath());
        } catch (Exception e){
            log.error("排程有异常",e);
        }
        log.info("定时任务开始执行结束");
    }

    //String dir  ="D:\\1项目\\大连三垦\\2需求分设\\APS\\";
    private void readApsPlan(String path){

        //ApsPlanPdtYield apsPlanPdtYield = new ApsPlanPdtYield();
        List<ApsPlanPdtYield> apsPlanPdtYieldList = Lists.newArrayList();
        List<ApsPlanPdtYieldDetail> apsPlanPdtYieldDetailList = Lists.newArrayList();
        Map<String, String> pdtMap = Maps.newHashMap();
        Map<Integer, String> dateMap = Maps.newHashMap();
        String period ="";
        try {
            List<List<String[]>> list = ExcelUtil.readExcel(path);
            List<String[]> list2 = list.get(0); //第一个sheet页数据
            Object[] line = list2.get(0);
            String year1 = line[26].toString();
            String year = line[26].toString().replace(".0","");
            String month = line[28].toString().replace(".0","");
            period = year+ (month.length()==1? "0"+month: month);
            //list2.remove(0);
            //list2.remove(0);
            int pdtStart = 2;
            int pdtEnd = 0;
            for (int i = pdtStart; i < list2.size(); i++) {
                Object[] strings = list2.get(i);
                if(StringUtil.isBlank(strings[22].toString())){
                    pdtEnd = i;
                    break;
                }
            }

            for (int i = pdtEnd; i < list2.size(); i++) {
                Object[] strings = list2.get(i);
                int startCol = 0;
                boolean dayStart = false;
                if("品番".equals(strings[2].toString())){
                    for (int i1 = 0; i1 < strings.length; i1++) {
                        Object s = strings[i1];
                        s = s.toString().replace(".0", "");
                        if(dayStart || "1".equals(s) ){
                            dayStart = true;
                            startCol = i1;
                            if(((String) s).length() == 1){
                                s = "0"+s;
                            }
                            dateMap.put(i1, period+s);
                        }

                    }
                    pdtEnd = i;
                    break;
                }
            }
            for (int i = pdtStart; i <= pdtEnd; i++) {
                Object[] strings = list2.get(i);
                if (StringUtil.isBlank(strings[0] + "") && StringUtil.isNotBlank(strings[22] + "")) {
                    System.out.println(strings[22] + "--" + strings[24] + "--" + strings[28] + "--" + strings[29]);
                    System.out.println(strings[30] + "--" + strings[32] + "--" + strings[36] + "--" + strings[37]);
                    String pdtno1 = strings[22].toString().replace(".0","");
                    if(StringUtil.isNotBlank(pdtno1)){
                        ApsPlanPdtYield apsPlanPdtYield = new ApsPlanPdtYield();
                        apsPlanPdtYield.setProductionNo(strings[22].toString().replace(".0",""));
                        apsPlanPdtYield.setProductionName(strings[24].toString());
                        apsPlanPdtYield.setLotQty((int) (Double.parseDouble(strings[28].toString())));
                        apsPlanPdtYield.setPlanQty((int) (Double.parseDouble(strings[29].toString())*1000));
                        apsPlanPdtYield.setPlanDate(period);
                        apsPlanPdtYieldList.add(apsPlanPdtYield);
                    }
                    String pdtno2 = strings[30].toString().replace(".0","");
                    if(StringUtil.isNotBlank(pdtno2)){
                        ApsPlanPdtYield apsPlanPdtYield2 = new ApsPlanPdtYield();
                        apsPlanPdtYield2.setProductionNo(strings[30].toString().replace(".0",""));
                        apsPlanPdtYield2.setProductionName(strings[32].toString());
                        apsPlanPdtYield2.setLotQty((int) (Double.parseDouble(strings[36].toString())));
                        apsPlanPdtYield2.setPlanQty((int) (Double.parseDouble(strings[37].toString())*1000));
                        apsPlanPdtYield2.setPlanDate(period);
                        apsPlanPdtYieldList.add(apsPlanPdtYield2);
                    }
                    pdtMap.put(pdtno1, strings[24].toString());
                    pdtMap.put(pdtno2, strings[32].toString());
                }
            }

            boolean startFlag = false;
            for (int i = pdtEnd; i < list2.size(); i++) {
                Object[] strings =list2.get(i);
                if(startFlag || "1.0".equals(strings[0]+"")){
                    startFlag = true;
                    boolean blankLine = true;
                    for (int i1 = 22; i1 < 53; i1++) {
                        String s = strings[i1].toString();
                        if(StringUtil.isNotBlank(s)){
                            blankLine = false;
                        }
                    }
                    if(!blankLine && !"ﾛｯﾄ数".equals(strings[6])&& !"投入数(k)".equals(strings[6]) ){
                        String productionNo = strings[2].toString();
                        for (int i1 = 22; i1 < 53; i1++) {
                            String s = strings[i1].toString();
                            if(StringUtil.isNotBlank(s) || s.length() == 5){
                                ApsPlanPdtYieldDetail detail = new ApsPlanPdtYieldDetail();
                                detail.setProductionNo(productionNo.replace(".0",""));
                                detail.setLotNo(s);
                                detail.setPlanDate(dateMap.get(i1));
                                apsPlanPdtYieldDetailList.add(detail);
                            }
                        }
                        System.out.println((i+2)+"---"+strings[2]);
                    }

                }else{
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //数据整理
        ApsPlanPdtYieldDetail lastPlan = apsPlanPdtYieldDetailList.get(0);
        lastPlan.setProductionName(pdtMap.get(lastPlan.getProductionNo()));
        for (int i = 1; i < apsPlanPdtYieldDetailList.size(); i++) {
            ApsPlanPdtYieldDetail temp = apsPlanPdtYieldDetailList.get(i);
            if(StringUtil.isBlank(temp.getProductionNo()) ||  temp.getProductionNo().contains("J.")){
                temp.setProductionNo(lastPlan.getProductionNo());
                temp.setProductionName(pdtMap.get(temp.getProductionNo()));
            }else{
                //新的批次
                if(temp.getProductionNo().length() == 7){
                    temp.setProductionName(pdtMap.get(temp.getProductionNo()));
                    lastPlan = temp;
                }

            }
        }

        Map<String, Integer> singleYieldMap = Maps.newHashMap();
        for (ApsPlanPdtYield apsPlanPdtYield : apsPlanPdtYieldList) {
            if(apsPlanPdtYield.getLotQty() != 0 ){
                singleYieldMap.put(apsPlanPdtYield.getProductionNo(), apsPlanPdtYield.getPlanQty()/apsPlanPdtYield.getLotQty());
            }
        }
        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : apsPlanPdtYieldDetailList) {
            apsPlanPdtYieldDetail.setPlanQty(singleYieldMap.get(apsPlanPdtYieldDetail.getProductionNo()));
        }
        System.out.println(apsPlanPdtYieldDetailList.size());
        Map<String,Integer> map = Maps.newHashMap();
        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : apsPlanPdtYieldDetailList) {
            String productionNo = apsPlanPdtYieldDetail.getProductionNo();
            String planDate = apsPlanPdtYieldDetail.getPlanDate();
            Integer count =  map.get(productionNo+planDate);
            if(count ==null){
                count = 0;
            }
            map.put(productionNo+planDate,++count);
        }
        Map<String, String> map1 = Maps.newHashMap();
        map1.put("A","1");
        map1.put("B","2");
        map1.put("C","3");
        map1.put("D","4");
        map1.put("E","5");
        map1.put("F","6");
        map1.put("G","7");
        map1.put("H","8");
        map1.put("I","9");
        map1.put("J","10");
        map1.put("K","11");
        map1.put("L","12");
        map1.put("M","13");
        map1.put("N","14");
        map1.put("O","15");
        map1.put("P","16");

        for (ApsPlanPdtYieldDetail apsPlanPdtYieldDetail : apsPlanPdtYieldDetailList) {
            String productionNo = apsPlanPdtYieldDetail.getProductionNo();
            String planDate = apsPlanPdtYieldDetail.getPlanDate();
            String lotNo = apsPlanPdtYieldDetail.getLotNo();
            String indexChar = lotNo.substring(4,5);
            String no = map1.get(indexChar);
            int count =  map.get(productionNo+planDate);
            apsPlanPdtYieldDetail.setDayNo(no+"/"+count);
        }
        apsPlanPdtYieldService.deleteByPeriod(period);
        apsPlanPdtYieldService.insertBatch(apsPlanPdtYieldList,100);
        apsPlanPdtYieldDetailService.deleteByPeriod(period);
        apsPlanPdtYieldDetailService.insertBatch(apsPlanPdtYieldDetailList,100);
        //将排程的数据上传到redis中
        log.info(JsonUtil.toJsonString(apsPlanPdtYieldDetailList));
        for(ApsPlanPdtYieldDetail item : apsPlanPdtYieldDetailList){
            String proNo = item.getProductionNo();
            String proName = item.getProductionName();
            if(null !=proNo && !"".equals(proNo) && null !=proName && !"".equals(proName)){
                redisTemplate.opsForValue().set(proNo, proName);
            }
        }
    }

}
