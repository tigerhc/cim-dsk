package com.lmrj.dsk.eqplog.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
//        File file = new File( "D:\\AAAAAAAAA_吴虎成大集合\\aa.csv" );
        try {
            File f1 = new File("D:/AAAAAAAAA_吴虎成大集合/aa.csv");
            if (f1.exists()==false){
                f1.getParentFile().mkdirs();
            }
            // 准备长度是2的字节数组，用88,89初始化，其对应的字符分别是X,Y
            byte data[] = {88,89};
            // 创建基于文件的输出流
            FileOutputStream fos = new FileOutputStream(f1);
            // 把数据写入到输出流
            fos.write(data);
            // 关闭输出流
            fos.close();
            System.out.println("输入完成");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("==============================");

        List list = new ArrayList( );
        list.add( "a" );
        list.add( "b" );
        String s = list.toString();
        System.out.println( s );

    }}
