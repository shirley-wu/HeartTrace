package com.example.dell.auth;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.example.dell.passwd.PasswdWorker;

/**
 * Created by wu-pc on 2018/7/12.
 */

public class MyAccountTest extends InstrumentationTestCase {

    final static private String TAG = "MyAccountTest";

    public void testGet() {
        Context context = InstrumentationRegistry.getContext();
        MyAccount myAccount = MyAccount.get(context);
        if(myAccount == null) {
            Log.d(TAG, "testGet: myAccount is null");
        }
        else{
            Log.d(TAG, "testGet: myAccount name = " + myAccount.getName());
            Log.d(TAG, "testGet: myAccount token = " + myAccount.getToken());
        }
    }

    public void testSet() {
        Context context = InstrumentationRegistry.getContext();
        MyAccount myAccount = MyAccount.get(context);
        myAccount.setName("name");
        myAccount.setToken("token");
        Log.d(TAG, "testSet: save = " + myAccount.save());
    }
}
