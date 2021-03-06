package com.lmrj.cim.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lmrj.aps.plan.entity.ApsPlanPdtYield;
import com.lmrj.aps.plan.entity.ApsPlanPdtYieldDetail;
import com.lmrj.util.ExcelUtil;
import com.lmrj.util.lang.StringUtil;

import java.util.List;
import java.util.Map;

public class ExcelUtilTest {

    //@Test
    public void readExcel() {
        String dir  ="D:\\1项目\\大连三垦\\2需求分设\\APS\\";
        //ApsPlanPdtYield apsPlanPdtYield = new ApsPlanPdtYield();
        List<ApsPlanPdtYield> apsPlanPdtYieldList = Lists.newArrayList();
        List<ApsPlanPdtYieldDetail> apsPlanPdtYieldDetailList = Lists.newArrayList();
        Map<String, String> pdtMap = Maps.newHashMap();
        Map<Integer, String> dateMap = Maps.newHashMap();
        try {
            List<List<String[]>> list = ExcelUtil.readExcel(dir+"DSK投入計画表.xls");
            List<String[]> list2 = list.get(0); //第一个sheet页数据
            Object[] line = list2.get(0);
            String year1 = line[26].toString();
            String year = line[26].toString().replace(".0","");
            String month = line[28].toString().replace(".0","");
            String period = year+ (month.length()==1? "0"+month: month);
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
                        apsPlanPdtYieldList.add(apsPlanPdtYield);
                    }
                    String pdtno2 = strings[30].toString().replace(".0","");
                    if(StringUtil.isNotBlank(pdtno2)){
                        ApsPlanPdtYield apsPlanPdtYield2 = new ApsPlanPdtYield();
                        apsPlanPdtYield2.setProductionNo(strings[30].toString().replace(".0",""));
                        apsPlanPdtYield2.setProductionName(strings[32].toString());
                        apsPlanPdtYield2.setLotQty((int) (Double.parseDouble(strings[36].toString())));
                        apsPlanPdtYield2.setPlanQty((int) (Double.parseDouble(strings[37].toString())*1000));
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

    }

}
