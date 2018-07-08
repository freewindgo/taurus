package com.cf.taurus.common.util;

import java.security.MessageDigest;

/**
 * MD5Utils
 *
 * MD5加密
 *
 * @author 于文硕
 * @since 2018/5/15 11:31
 */
public class MD5Utils {

    /**
     * MD5加密
     * @param s
     * @return
     */
    public static String MD5Encryption(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验明文和密文是否相同
     *
     * @param password
     * @param encryptPas
     * @return
     */
    public static Boolean isPasswordRight(String password, String encryptPas){
        String encrypt = MD5Encryption(password);
        if(encrypt != null && encrypt.equals(encryptPas)){
            return true;
        }
        return false;
    }

}
