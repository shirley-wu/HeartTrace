package com.example.dell.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

/**
 * Created by wu-pc on 2018/7/12.
 */

public class ServerAuthenticatorTest extends InstrumentationTestCase {

    private static final String TAG = "ServerAuthenticatorTest";

    public void testSignIn() {
        Bundle bundle = new Bundle();
        boolean status;

        status = ServerAuthenticator.signIn("", "", bundle);

        // assertTrue(status);
        Log.d(TAG, "testSignIn: status = " + status);
        // assertTrue(bundle.getBoolean("success"));
        // Log.d(TAG, "testSignIn: msg = " + bundle.getString("msg"));
        for(String key : bundle.keySet()) {
            Log.d(TAG, "testSignIn: key " + key + " value " + bundle.get(key));
        }
    }

    public void testSignUp() {
        Bundle bundle = new Bundle();
        boolean status;

        status = ServerAuthenticator.signUp("", "", bundle);

        Log.d(TAG, "testSignUp: status = " + status);
        for(String key : bundle.keySet()) {
            Log.d(TAG, "testSignUp: key " + key + " value " + bundle.get(key));
        }
    }

    public void testVerify() {
        Context context = InstrumentationRegistry.getTargetContext();
        MyAccount myAccount = MyAccount.get(context);
        boolean verified = ServerAuthenticator.veriy(myAccount.getName(), myAccount.getToken());
        Log.d(TAG, "testVerify: verified = " + verified);
    }

}
