package example.hearttrace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by wu-pc on 2018/5/9.
 * Copied from official example, and revised for my own purpose
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "heartTrace.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 2;

    // the DAO object we use to access the Diary table
    private Dao<Diary, Integer> diaryDao = null;
    private RuntimeExceptionDao<Diary, Integer> runtimeDiaryDao = null;

    private Dao<Diarybook, Integer> diarybookDao = null;
    private RuntimeExceptionDao<Diarybook, Integer> runtimeDiarybookDao = null;

    private Dao<>

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Diary.class);
            TableUtils.createTable(connectionSource, Diarybook.class);
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
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Diary.class, true);
            TableUtils.dropTable(connectionSource, Diarybook.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our Diary class. It will create it or just give the cached
     * value.
     */
    public Dao<Diary, Integer> getDiaryDao() throws SQLException {
        if (diaryDao == null) {
            diaryDao = getDao(Diary.class);
        }
        return diaryDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our Diary class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Diary, Integer> getRuntimeExceptionDiaryDao() {
        if (runtimeDiaryDao == null) {
            runtimeDiaryDao = getRuntimeExceptionDao(Diary.class);
        }
        return runtimeDiaryDao;
    }

    public Dao<Diarybook, Integer> getDiarybookDao() throws SQLException {
        if (diarybookDao == null) {
            diarybookDao = getDao(Diarybook.class);
        }
        return diarybookDao;
    }

    public RuntimeExceptionDao<Diarybook, Integer> getRuntimeExceptionDiarybookDao() {
        if (runtimeDiarybookDao == null) {
            runtimeDiarybookDao = getRuntimeExceptionDao(Diarybook.class);
        }
        return runtimeDiarybookDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        diaryDao = null;
        runtimeDiaryDao = null;
        diarySetDao = null;
        runtimeDiarySetDao = null;
    }
}
