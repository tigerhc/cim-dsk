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

    public static void main(String[] args) {
        String test = getBeforeTime();
        System.out.println(test);
    }
}
