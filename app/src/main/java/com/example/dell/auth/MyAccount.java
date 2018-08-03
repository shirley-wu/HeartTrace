package com.example.dell.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Setting;
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

    Dao userDao = null;
    
    Dao settingDao = null;

    private User user;

    private Setting setting;

    public MyAccount(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        try {
            userDao = databaseHelper.getDaoAccess(User.class);
            List<User> userList = userDao.queryForAll();
            Log.d(TAG, "MyAccount: userList size = " + userList.size());
            if (userList.size() == 0) user = new User();
            else user = userList.get(0);

            settingDao = databaseHelper.getDaoAccess(Setting.class);
            List<Setting> settingList = settingDao.queryForAll();
            Log.d(TAG, "MyAccount: settingList size = " + settingList.size());
            if (settingList.size() == 0) setting = new Setting();
            else setting = settingList.get(0);
        }
        catch (SQLException e) {
            Log.e(TAG, "MyAccount: ", e);
            userDao = null;
            user = null;
            settingDao = null;
            setting = null;
        }
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setToken(String token) {
        setting.setToken(token);
    }

    public String getToken() {
        return setting.getToken();
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


    public long getModified() {
        return user.getModified();
    }

    public void setModified(long modified) {
        user.setModified(modified);
    }

    public String getPassword() {
        return setting.getPassword();
    }

    public void setPassword(String password) {
        setting.setPassword(password);
    }

    public void setIsRemember(boolean isRemember){
        setting.setIsRemember(isRemember);
    }

    public boolean getIsRemember(){
        return setting.getIsRemember();
    }

    public boolean getAutoLogin() {
        return setting.getAutoLogin();
    }

    public void setAutoLogin(boolean autoLogin) {
        setting.setAutoLogin(autoLogin);
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

            Dao.CreateOrUpdateStatus status = userDao.createOrUpdate(user);
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
        try {
            Dao.CreateOrUpdateStatus status = settingDao.createOrUpdate(setting);
            Log.d(TAG, "settingUser: 新建 = " + status.isCreated());
            Log.d(TAG, "settingUser: 更新 = " + status.isUpdated());

            return true;
        }
        catch (SQLException e) {
            Log.e(TAG, "saveSetting: ", e);
            return false;
        }
    }

    public boolean clearUser() {
        try {
            List<User> list = userDao.queryForAll();
            for (User u : list) {
                int code = userDao.delete(u);
                Log.d(TAG, "clearUser: 删除返回值 = " + code);
            }
            userDao = null;
            user = null;

            return true;
        }
        catch (SQLException e) {
            Log.e(TAG, "clearUser: ", e);
            return false;
        }
    }

    public boolean clearSetting() {
        try {
            List<Setting> list = settingDao.queryForAll();
            for (Setting u : list) {
                int code = settingDao.delete(u);
                Log.d(TAG, "clearSetting: 删除返回值 = " + code);
            }
            settingDao = null;
            setting = null;

            return true;
        }
        catch (SQLException e) {
            Log.e(TAG, "clearUser: ", e);
            return false;
        }
    }

}
