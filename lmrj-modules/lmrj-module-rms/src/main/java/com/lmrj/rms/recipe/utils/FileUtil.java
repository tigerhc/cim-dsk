package com.lmrj.rms.recipe.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FileUtil {

    public static Map<String, String> analysis(String filePath) throws Exception{
        Map<String, String> map = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),"UTF-16"));
        String str = null;
        StringBuffer stringBuffer = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            String[] strings = str.split("\t");
            if (strings.length > 3){
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = strings[i].trim();
                    if(!"".equals(strings[i]) && strings[i] != null){
                        stringBuffer.append(strings[i]);
                    }
                    if(i < strings.length - 1){
                        stringBuffer.append("@@");
                    }
                }
                map.put(strings[strings.length - 1], stringBuffer.toString());
                stringBuffer.setLength(0);
            }
        }
        return map;
    }
}
