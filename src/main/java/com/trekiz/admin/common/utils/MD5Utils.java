package com.trekiz.admin.common.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * MD5加密工具类
 * @author majiancheng
 * @Time 2015-5-13
 */
 
public class MD5Utils {
 
    /**
     * 
     * @param text
     *            明文
     * @return 32位密文
     */
    public static String generateMD5Code(String text) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte b[] = md.digest();
 
            int i;
 
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
 
            re_md5 = buf.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
    
}