package com.lmrj.cim.utils;

import java.util.UUID;

/**
 * @author wangdong
 * @create 2020-09-29
 */
public class UUIDUtil {

    /**生成一个uuid
     * @return
     */
    public static String createUUID(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
