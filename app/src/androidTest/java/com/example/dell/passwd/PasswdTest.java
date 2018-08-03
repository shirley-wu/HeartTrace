package com.example.dell.passwd;

import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

/**
 * Created by wu-pc on 2018/8/1.
 */

public class PasswdTest {

    final static private String TAG = "PasswdTest";

    @Test
    public void testPasswd() {
        Log.d(TAG, "passwd = " + PasswdWorker.getPasswd(InstrumentationRegistry.getTargetContext()));
    }

}
