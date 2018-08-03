package com.example.dell.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.User;
import com.example.dell.passwd.PasswdTool;
import com.example.dell.passwd.PasswdWorker;
import com.j256.ormlite.dao.Dao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wu-pc on 2018/7/11.
 */

public class MyAccount {

    private static final String TAG = "MyAccount";

    private static final String PREFERENCE_USERNAME = "MyAccountPreference";

    Dao<User, String> dao = null;

    private User user;

    private SharedPreferences preferences = null;

    private String key;

    private String password;

    private String token;

    private boolean isRemember;

    private boolean autoLogin;

    public MyAccount(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_USERNAME, Context.MODE_PRIVATE);

        key = PasswdWorker.getPasswd(context);

        String password = preferences.getString("password", null);
        this.password = (password == null) ? null : PasswdTool.desDecrypt(password, key);
        Log.d(TAG, "MyAccount: password = " + this.password);

        String token = preferences.getString("token", null);
        this.token = (token == null) ? null : PasswdTool.desDecrypt(token, key);
        Log.d(TAG, "MyAccount: token = " + this.token);

        String isRemember = preferences.getString("isRemember", null);
        String isRememberDecrypted = (isRemember == null) ? null : PasswdTool.desDecrypt(isRemember, key);
        this.isRemember = (isRememberDecrypted != null) && isRememberDecrypted.equals("true");
        Log.d(TAG, "MyAccount: isRemember = " + this.isRemember);

        String autoLogin = preferences.getString("autoLogin", null);
        String autoLoginDecrypted = (autoLogin == null) ? null : PasswdTool.desDecrypt(autoLogin, key);
        this.autoLogin = (autoLoginDecrypted != null) && autoLoginDecrypted.equals("true");
        Log.d(TAG, "MyAccount: autoLogin = " + this.autoLogin);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        try {
            dao = databaseHelper.getDaoAccess(User.class);
            List<User> list = dao.queryForAll();
            Log.d(TAG, "MyAccount: list size = " + list.size());
            if (list.size() == 0) user = new User();
            else user = list.get(0);
            // TODO: Dangerous!!!!! DatabaseHelper not closed.
        }
        catch (SQLException e) {
            Log.e(TAG, "MyAccount: ", e);
            dao = null;
            user = null;
        }
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setNickname(String nickname) {
        user.setNickname(nickname);
    }

    public String getNickname() {
        return user.getNickname();
    }

    public void setGender(String gender) {
        user.setGender(gender);
    }

    public String getGender() {
        return user.getGender();
    }

    public void setBirthday(String birthday) {
        user.setBirthday(birthday);
    }

    public String getBirthday() {
        return user.getBirthday();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setSchool(String school) {
        user.setSchool(school);
    }

    public String getSchool() {
        return user.getSchool();
    }

    public void setSignature(String signature) {
        user.setSignature(signature);
    }

    public String getSignature() {
        return user.getSignature();
    }

    public void setHeadimage(String headimage){
        try {
            Log.d(TAG, "setHeadimage: length = " + headimage.getBytes("UTF-8").length);
        }
        catch (UnsupportedEncodingException e) {
            Log.e(TAG, "setHeadimage: ", e);
        }
        user.setHeadimage(headimage);
    }

    public String getHeadimage(){
        return user.getHeadimage();
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
        return user.getModified();
    }

    public void setModified(long modified) {
        user.setModified(modified);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean saveUser() {
        return saveUser(true);
    }

    public boolean saveUser(boolean setModified) {
        try {
            Log.d(TAG, "save: setModified = " + setModified);

            if (setModified) user.setModified(System.currentTimeMillis());
            Log.d(TAG, "saveUser: modified = " + getModified());

            Dao.CreateOrUpdateStatus status = dao.createOrUpdate(user);
            Log.d(TAG, "saveUser: 新建 = " + status.isCreated());
            Log.d(TAG, "saveUser: 更新 = " + status.isUpdated());

            return true;
        }
        catch (SQLException e) {
            Log.e(TAG, "saveUser: ", e);
            return false;
        }
    }

    public boolean saveSetting() {
        SharedPreferences.Editor editor = preferences.edit();

        if (token != null) editor.putString("token", PasswdTool.desEncrypt(token, key));
        Log.d(TAG, "saveSetting: token = " + token);

        if (password != null) editor.putString("password", PasswdTool.desEncrypt(password, key));
        Log.d(TAG, "saveSetting: password = " + password);

        editor.putString("isRemember", PasswdTool.desEncrypt("" + isRemember, key));
        editor.putString("autoLogin", PasswdTool.desEncrypt("" + autoLogin, key));
        boolean status = editor.commit();
        Log.d(TAG, "saveSetting: status = " + status);

        return status;
    }

    public boolean clearUser() {
        try {
            List<User> list = dao.queryForAll();
            for (User u : list) {
                int code = dao.delete(u);
                Log.d(TAG, "clearUser: 删除返回值 = " + code);
            }
            dao = null;
            user = null;

            return true;
        }
        catch (SQLException e) {
            Log.e(TAG, "clearUser: ", e);
            return false;
        }
    }

    public boolean clearSetting() {
        token = null;
        password = null;
        isRemember = false;
        autoLogin = false;
        boolean status = saveSetting();
        Log.d(TAG, "clearSetting: status = " + status);
        return status;
    }

}
