package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by wu-pc on 2018/8/2.
 */

@DatabaseTable
public class Picture {

    final static private String TAG = "Picture";

    @DatabaseField
    private long id;

    @DatabaseField
    private long modified;

    public Picture() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public long getModified() {
        return modified;
    }

    public void insert(DatabaseHelper databaseHelper) {
        try {
            id = databaseHelper.getIdWorker().nextId();
            modified = System.currentTimeMillis();

            Dao<Picture, Long> dao = databaseHelper.getDaoAccess(Picture.class);
            int status = dao.create(this);
            Log.d(TAG, "insert: 插入返回值 = " + status);
        }
        catch (SQLException e) {
            Log.e(TAG, "insert: ", e);
        }
    }
}
