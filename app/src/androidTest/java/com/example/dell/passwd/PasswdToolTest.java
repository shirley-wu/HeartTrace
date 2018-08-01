package com.example.dell.passwd;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wu-pc on 2018/8/1.
 */

public class PasswdToolTest {

    final static private String TAG = "PasswdToolTest";

    @Test
    public void testCryption() {
        Context context = InstrumentationRegistry.getTargetContext();
        String data = "Shirley";
        String passwd = PasswdWorker.getPasswd(context);
        Log.d(TAG, "testCryption: passwd = " + passwd);
        String result = PasswdTool.desEncrypt(data, passwd);
        Log.d(TAG, "main: result = " + result);
        String backData = PasswdTool.desDecrypt(result, passwd);
        Log.d(TAG, "testCryption: back data = " + backData);
        assertEquals(data, backData);
    }

}
