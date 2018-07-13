package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by chen on 5/19/2018.
 */

@DatabaseTable(tableName = "SearchHistory")
public class SearchHistory {
    public static final String TAG = "SearchHistory";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(unique = true)
    String entry;

    public SearchHistory() {
    };

    public SearchHistory(String entry) {
        this.entry = entry;
    }

    public String getEntry() {
        return this.entry;
    }

    public void insert(DatabaseHelper helper) {
        try {
            Dao<SearchHistory, Integer> dao = helper.getDaoAccess(SearchHistory.class);
            Log.i("SearchHistory", "dao = " + dao + " 插入 " + this);
            int returnValue = dao.create(this);
            Log.i("SearchHistory", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void deleteEntry(DatabaseHelper helper) {
        try {
            Dao<SearchHistory, Integer> dao = helper.getDaoAccess(SearchHistory.class);

            dao.delete(this);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static void deleteAllEntry(DatabaseHelper helper) {
        try {
            Dao<SearchHistory, Integer> dao = helper.getDaoAccess(SearchHistory.class);

            dao.deleteBuilder().delete();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
}