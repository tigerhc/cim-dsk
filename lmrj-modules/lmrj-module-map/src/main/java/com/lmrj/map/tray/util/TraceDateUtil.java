package com.lmrj.map.tray.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TraceDateUtil {
    public static String getBeforeTime(){
        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前一天
        return defaultStartDate;
    }

    public static String getChkTime(Date date, int diff){
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.MINUTE, diff);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前一天
        return defaultStartDate;
    }

    public static String getDateStr(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        return sdf.format(date);
    }

    /** 获得两个时间之间差多少秒
     */
    public static long getDiffSec(Date startDate, Date endDate){
        long dateLong1 = startDate.getTime();
        long dateLong2 = endDate.getTime();
        long diff = dateLong2 - dateLong1;
        return diff / 1000;
    }

    public static void main(String[] args) {
//        String test = getBeforeTime();
//        String test = getChkTime(new Date(), -1);
//        System.out.println(test);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
            long test = getDiffSec(sdf.parse("2021-06-21 11:26:51"), sdf.parse("2021-06-21 11:21:50"));
            System.out.println(test);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
