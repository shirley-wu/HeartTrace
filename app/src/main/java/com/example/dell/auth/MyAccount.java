package com.example.dell.auth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wu-pc on 2018/7/11.
 */

public class MyAccount {

    private static final String TAG = "MyAccount";

    private static final String PREFERENCE_NAME = "MyAccountPreference";

    private static MyAccount myAccount = null;

    private static SharedPreferences preferences = null;

    private String name;

    private String token;

    private MyAccount() {

    }

    public boolean setName(String name) {
        this.name = name;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        return editor.commit();
    }

    public String getName() {
        return name;
    }

    public boolean setToken(String token) {
        this.token = token;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        return editor.commit();
    }

    public String getToken() {
        return token;
    }

    static public MyAccount get(Context context) {
        if(preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

            myAccount   = new MyAccount();
            myAccount.name  = preferences.getString("", null);
            myAccount.token = preferences.getString("token", null);
        }
        return myAccount;
    }

}
