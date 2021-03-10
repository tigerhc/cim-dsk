package com.lmrj.rms.recipe.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FileUtil {

    public static Map<String, String> analysis(String filePath) throws Exception{
        Map<String, String> map = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), StandardCharsets.UTF_8));
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

    public static Map<String, String> analysisRecipeTemplate(String filePath) throws Exception{
        Map<String, String> map = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), StandardCharsets.UTF_8));
        String str = null;
        boolean flag = true;
        while ((str = bufferedReader.readLine()) != null) {
            String[] strings = str.trim().split("=");
            if (flag && strings.length == 1) {
                map.put("recipeType", strings[0]);
                flag = false;
            }
            if (strings.length == 2){
                map.put(strings[0], strings[1]);
            }
        }
        return map;
    }
}
