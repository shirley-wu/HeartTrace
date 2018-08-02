package com.example.dell.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.dell.passwd.PasswdTool;
import com.example.dell.passwd.PasswdWorker;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by wu-pc on 2018/7/11.
 */

public class MyAccount {

    private static final String TAG = "MyAccount";

    private static final String PREFERENCE_USERNAME = "MyAccountPreference";

    private static MyAccount myAccount = null;

    private static SharedPreferences preferences = null;

    private static String key = null;

    private static String packageName = null;

    private String username;

    private String token;

    private String nickname;

    private String gender;

    private String birthday;

    private String email;

    private String school;

    private String signature;

    private String headimage;

    private boolean isRemember;

    private boolean autoLogin;

    private String password;

    private long modified = 0;

    private MyAccount() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsRemember(boolean isRemember){
        this.isRemember = isRemember;
    }

    public boolean getIsRemember(){
        return isRemember;
    }

    public boolean getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public boolean save(boolean setModified) {
        SharedPreferences.Editor editor = preferences.edit();

        if (setModified) modified = System.currentTimeMillis();
        editor.putString("modified", PasswdTool.desEncrypt("" + modified, key));

        if (username != null) editor.putString("username", PasswdTool.desEncrypt(username, key));
        if (token != null) editor.putString("token", PasswdTool.desEncrypt(token, key));
        if (nickname != null) editor.putString("nickname", PasswdTool.desEncrypt(nickname, key));
        if (gender != null) editor.putString("gender", PasswdTool.desEncrypt(gender, key));
        if (birthday != null) editor.putString("birthday", PasswdTool.desEncrypt(birthday, key));
        if (email != null) editor.putString("email", PasswdTool.desEncrypt(email, key));
        if (school != null) editor.putString("school", PasswdTool.desEncrypt(school, key));
        if (signature != null) editor.putString("signature", PasswdTool.desEncrypt(signature, key));
        if (headimage != null) editor.putString("headimage", PasswdTool.desEncrypt(headimage, key));

        editor.putString("isRemember", PasswdTool.desEncrypt("" + isRemember, key));

        editor.putString("autoLogin", PasswdTool.desEncrypt("" + autoLogin, key));

        if(password != null) editor.putString("password", PasswdTool.desEncrypt(password, key));

        return editor.commit();
    }

    static public MyAccount get(Context context) {
        if(packageName == null) {
            packageName = context.getApplicationContext().getPackageName();

            key = PasswdWorker.getPasswd(context);

            preferences = context.getSharedPreferences(PREFERENCE_USERNAME, Context.MODE_PRIVATE);

            myAccount   = new MyAccount();

            String modified = preferences.getString("modified", null);
            if (modified == null) {
                myAccount.modified = -1;
            }
            else {
                String modifiedDecrypted = PasswdTool.desDecrypt(modified, key);
                myAccount.modified = Long.parseLong(modifiedDecrypted);
            }

            String username = preferences.getString("username", null);
            myAccount.username = (username == null) ? null : PasswdTool.desDecrypt(username, key);

            String token = preferences.getString("token", null);
            myAccount.token = (token == null) ? null : PasswdTool.desDecrypt(token, key);

            String nickname = preferences.getString("nickname", null);
            myAccount.nickname = (nickname == null) ? null : PasswdTool.desDecrypt(nickname, key);

            String gender = preferences.getString("gender", null);
            myAccount.gender = (gender == null) ? null : PasswdTool.desDecrypt(gender, key);

            String birthday = preferences.getString("birthday", null);
            myAccount.birthday = (birthday == null) ? null : PasswdTool.desDecrypt(birthday, key);

            String email = preferences.getString("email", null);
            myAccount.email = (email == null) ? null : PasswdTool.desDecrypt(email, key);

            String school = preferences.getString("school", null);
            myAccount.school = (school == null) ? null : PasswdTool.desDecrypt(school, key);

            String signature = preferences.getString("signature", null);
            myAccount.signature = (signature == null) ? null : PasswdTool.desDecrypt(signature, key);

            String headimage = preferences.getString("headimage", null);
            myAccount.headimage = (headimage == null) ? null : PasswdTool.desDecrypt(headimage, key);

            String password = preferences.getString("password", null);
            myAccount.password = (password == null) ? null : PasswdTool.desDecrypt(password, key);

            String isRemember = preferences.getString("isRemember", null);
            String isRememberDecrypted = (isRemember == null) ? null : PasswdTool.desDecrypt(isRemember, key);
            myAccount.isRemember = (isRememberDecrypted != null) && isRememberDecrypted.equals("true");

            String autoLogin = preferences.getString("autoLogin", null);
            String autoLoginDecrypted = (autoLogin == null) ? null : PasswdTool.desDecrypt(autoLogin, key);
            myAccount.autoLogin = (autoLoginDecrypted != null) && autoLoginDecrypted.equals("true");
        }
        else if(!packageName.equals(context.getApplicationContext().getPackageName())) {
            return null;
        }
        return myAccount;
    }

}
