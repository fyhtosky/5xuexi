package com.sj.yinjiaoyun.xuexi.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者：Administrator on 2018/4/25 17:38
 * <p>
 * 邮箱：xjs250@163.com
 */
public class CXAESUtil {
    private static final String CipherMode = "AES/ECB/PKCS5Padding";//使用CFB加密，需要设置IV

    /**
     * 对字符串加密
     *
     * @param key  密钥
     * @param data 源字符串
     * @return 加密后的字符串
     */
    public static String encryptString(String key, String data) {
        try {
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
            byte[] encrypted = cipher.doFinal(data.getBytes("utf-8"));
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对字符串解密
     *
     * @param key  密钥
     * @param data 已被加密的字符串
     * @return 解密得到的字符串
     */
    public static String decryptString(String key, String data)  {
        try {
            byte[] encrypted1 = Base64.decode(data.getBytes("utf-8"), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
