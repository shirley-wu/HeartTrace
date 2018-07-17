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

    private String nickname;

    private String gender;

    private String birthday;

    private String email;

    private String school;

    private String signature;

    private String headimage;

    private MyAccount() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return  birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {
        return school;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public void setHeadimage(String headimage){
        this.headimage = headimage;
    }

    public String getHeadimage(){
        return headimage;
    }

    public boolean save() {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("name", name);
        editor.putString("token", token);
        editor.putString("nickname", nickname);
        editor.putString("gender", gender);
        editor.putString("birthday", birthday);
        editor.putString("email", email);
        editor.putString("school", school);
        editor.putString("signature", signature);
        editor.putString("headimage",headimage);

        return editor.commit();
    }

    static public MyAccount get(Context context) {
        if(preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

            myAccount   = new MyAccount();
            myAccount.name = preferences.getString("name", null);
            myAccount.token = preferences.getString("token", null);
            myAccount.nickname = preferences.getString("nickname", null);
            myAccount.gender = preferences.getString("gender", null);
            myAccount.birthday = preferences.getString("birthday",null);
            myAccount.email = preferences.getString("email", null);
            myAccount.school = preferences.getString("school", null);
            myAccount.signature = preferences.getString("signature", null);
            myAccount.headimage = preferences.getString("headimage",null);
        }
        return myAccount;
    }

}
