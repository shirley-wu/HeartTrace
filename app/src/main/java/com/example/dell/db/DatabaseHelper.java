package com.example.dell.db;

import android.content.Context;
import android.provider.Telephony;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by wu-pc on 2018/5/9.
 * Copied from official example, and revised for my own purpose
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    final static private String TAG = "DatabaseHelper";

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "heartTrace.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 13;

    private static final Class[] tableList = {
            Diarybook.class,
            Sentencebook.class,
            Diary.class,
            Sentence.class,
            Label.class,
            DiaryLabel.class,
            SentenceLabel.class,
            // SearchHistory.class,
    };

    // the DAO object we use to access the Diary table
    private Map<Class, Dao> daoMap = new Hashtable<>();

    private Map<Class, RuntimeExceptionDao> runtimeExceptionDaoMap = new Hashtable<>();

    private IdWorker idWorker;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        idWorker = new IdWorker(context);
        SQLiteDatabase.loadLibs(context);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            for(Class clazz : tableList) {
                TableUtils.createTable(connectionSource, clazz);
                Log.d(TAG, "onCreate: create table " + clazz.getName());
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     * Don't need it by now.
     */
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            for(Class clazz : tableList) {
                TableUtils.dropTable(connectionSource, clazz, true);
                Log.d(TAG, "onUpgrade: drop table " + clazz.getName());
            }
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    protected String getPassword() {
        return "hello I'm password";
    }

    /**
     * Returns the Database Access Object (DAO) for our Diary class. It will create it or just give the cached
     * value.
     */
    public Dao getDaoAccess(Class clazz) throws SQLException {
        if (daoMap.containsKey(clazz)) {
            return daoMap.get(clazz);
        }
        else {
            Dao dao = getDao(clazz);
            daoMap.put(clazz, dao);
            return dao;
        }
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our Diary class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao getRuntimeExceptionDaoAccess(Class clazz) {
        if (runtimeExceptionDaoMap.containsKey(clazz)) {
            return runtimeExceptionDaoMap.get(clazz);
        }
        else {
            RuntimeExceptionDao dao = getRuntimeExceptionDao(clazz);
            runtimeExceptionDaoMap.put(clazz, dao);
            return dao;
        }
    }

    public IdWorker getIdWorker() {
        return idWorker;
    }

    public void clearAll() {
        for(Class clazz : tableList) {
            try {
                TableUtils.clearTable(getConnectionSource(), clazz);
                Log.d(TAG, "clearAll: clear table " + clazz.getName());
            }
            catch(SQLException e) {
                Log.e(TAG, "clearAll: ", e);
            }
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        daoMap.clear();
        runtimeExceptionDaoMap.clear();
    }
}
