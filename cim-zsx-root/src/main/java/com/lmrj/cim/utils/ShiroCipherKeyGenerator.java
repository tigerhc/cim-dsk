package com.lmrj.cim.utils;

import org.apache.shiro.codec.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * All rights Reserved, Designed By www.lmrj.com
 *
 * @version V1.0
 * @package com.lmrj.web.utils
 * @title:
 * @description: 密钥生成java代码
 * @author: 张飞
 * @date: 2018/3/2 9:13
 * @copyright: 2017 www.lmrj.com Inc. All rights reserved.
 */
public class ShiroCipherKeyGenerator {

    public static void main(String[] args) {
        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKey deskey = keygen.generateKey();
        System.out.println(Base64.encodeToString(deskey.getEncoded()));
    }
}
