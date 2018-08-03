package com.example.dell.passwd;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by wu-pc on 2018/8/1.
 */

public class PasswdTool {

    static final private String TAG = "PasswdTool";

    public static String sha1(String data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(data.getBytes("UTF-8"));
            return bytes2Hex(bytes);
        } catch (Exception e) {
            Log.e(TAG, "sha1: ", e);
            return null;
        }
    }

    static public String desEncrypt(String data, String key) {
        try {
            Log.d(TAG, "desEncrypt: data = " + data + " key = " + key);

            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);

            byte[] result = cipher.doFinal(data.getBytes("UTF-8"));
            String resultBase64 = Base64.encodeToString(result, Base64.DEFAULT);
            Log.d(TAG, "desEncrypt: result = " + resultBase64);
            return resultBase64;
        }
        catch (Exception e) {
            Log.e(TAG, "encrypt: ", e);
        }
        return null;
    }

    static public String desDecrypt(String result, String key) {
        try {
            Log.d(TAG, "desDecrypt: result = " + result + " key = " + key);

            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);

            byte[] data = cipher.doFinal(Base64.decode(result, Base64.DEFAULT));
            String dataUtf = new String(data, "UTF-8");
            Log.d(TAG, "desDecrypt: data = " + dataUtf);
            return dataUtf;
        } catch (Exception e) {
            Log.e(TAG, "desDescrypt: ", e);
        }
        return null;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
