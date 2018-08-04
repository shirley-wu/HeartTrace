package com.example.dell.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wu-pc on 2018/8/3.
 */

@DatabaseTable
public class Setting {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String password;

    @DatabaseField
    private String token;

    @DatabaseField
    private boolean isRemember;

    @DatabaseField
    private boolean autoLogin;

    @DatabaseField
    private long dataAnchor = -1;

    @DatabaseField
    private long picAnchor = -1;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsRemember() {
        return isRemember;
    }

    public void setIsRemember(boolean remember) {
        isRemember = remember;
    }

    public boolean getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public long getDataAnchor() {
        return dataAnchor;
    }

    public void setDataAnchor(long dataAnchor) {
        this.dataAnchor = dataAnchor;
    }

    public long getPicAnchor() {
        return picAnchor;
    }

    public void setPicAnchor(long picAnchor) {
        this.picAnchor = picAnchor;
    }
}
