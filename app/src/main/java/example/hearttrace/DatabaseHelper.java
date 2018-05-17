package example.hearttrace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import java.util.Date;

/**
 * Created by wu-pc on 2018/5/9.
 * Copied from official example, and revised for my own purpose
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    final static private String TAG = "DatabaseHelper";

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "heartTrace.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 4;

    // the DAO object we use to access the Diary table
    private Dao<Diary, Integer> diaryDao = null;
    private RuntimeExceptionDao<Diary, Integer> runtimeDiaryDao = null;

    private Dao<DiaryLabel, Integer> diaryLabelDao = null;
    private RuntimeExceptionDao<DiaryLabel, Integer> runtimeDiaryLabelDao = null;

    private Dao<Diarybook, Integer> diarybookDao = null;
    private RuntimeExceptionDao<Diarybook, Integer> runtimeDiarybookDao = null;

    private Dao<Sentence, Integer> sentenceDao = null;
    private RuntimeExceptionDao<Sentence, Integer> runtimeSentenceDao = null;

    private Dao<Label, String> labelDao = null;
    private RuntimeExceptionDao<Label, String> runtimeLabelDao = null;

    private Dao<SentenceLabel, Integer> sentenceLabelDao = null;
    private RuntimeExceptionDao<SentenceLabel, Integer> runtimeSentenceLabelDao = null;
    private PreparedQuery<Label> labelForDiaryQuery;
    private PreparedQuery<Diary> diaryForLabelQuery;

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
            TableUtils.createTable(connectionSource, DiaryLabel.class);
            TableUtils.createTable(connectionSource, Label.class);
            TableUtils.createTable(connectionSource, Sentence.class);
            TableUtils.createTable(connectionSource, SentenceLabel.class);
            TableUtils.createTable(connectionSource, Sentencebook.class);
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
            TableUtils.dropTable(connectionSource, DiaryLabel.class, true);
            TableUtils.dropTable(connectionSource, Label.class, true);
            TableUtils.dropTable(connectionSource, Sentence.class, true);
            TableUtils.dropTable(connectionSource, SentenceLabel.class, true);
            TableUtils.dropTable(connectionSource, Sentencebook.class, true);
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

    public Dao<Sentence, Integer> getSentenceDao() throws SQLException {
        if(sentenceDao == null) {
            sentenceDao = getDao(Sentence.class);
        }
        return sentenceDao;
    }

    public Dao<Label, String> getLabelDao() throws SQLException {
        if (labelDao == null) {
            labelDao = getDao(Label.class);
        }
        return labelDao;
    }

    public Dao<DiaryLabel, Integer> getDiaryLabelDao() throws SQLException {
        if (diaryLabelDao == null) {
            diaryLabelDao = getDao(DiaryLabel.class);
        }
        return diaryLabelDao;
    }

    //for the sentence

    public Dao<SentenceLabel, Integer> getSentenceLabelDao() throws SQLException {
        if (sentenceLabelDao == null) {
            sentenceLabelDao = getDao(SentenceLabel.class);
        }
        return sentenceLabelDao;
    }

    public Dao<Sentencebook, Integer> getSentencebookDao() throws SQLException {
        if (sentencebookDao == null) {
            sentencebookDao = getDao(Sentencebook.class);
        }
        return sentencebookDao;
    }

    public Dao<Diarybook, Integer> getDiarybookDao() throws SQLException {
        if (diarybookDao == null) {
            diarybookDao = getDao(Diarybook.class);
        }
        return diarybookDao;
    }

    public List<Label> getAllLabel(){
        try {
            Dao<Label, String> dao = getLabelDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
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

    public RuntimeExceptionDao<Diarybook, Integer> getRuntimeExceptionDiarybookDao() {
        if (runtimeDiarybookDao == null) {
            runtimeDiarybookDao = getRuntimeExceptionDao(Diarybook.class);
        }
        return runtimeDiarybookDao;
    }

    public RuntimeExceptionDao<Sentence, Integer> getRuntimeExceptionSentenceDao() {
        if (runtimeDiaryDao == null) {
            runtimeSentenceDao = getRuntimeExceptionDao(Sentence.class);
        }
        return runtimeSentenceDao;
    }

    public RuntimeExceptionDao<Sentencebook, Integer> getRuntimeExceptionSentencebookDao() {
        if (runtimeSentencebookDao == null) {
            runtimeSentencebookDao = getRuntimeExceptionDao(Sentencebook.class);
        }
        return runtimeSentencebookDao;
    }

    public RuntimeExceptionDao<DiaryLabel, Integer> getRuntimeExceptionDiaryLabelDao() {
        if (runtimeDiaryLabelDao == null) {
            runtimeDiaryLabelDao = getRuntimeExceptionDao(DiaryLabel.class);
        }
        return runtimeDiaryLabelDao;
    }

    public RuntimeExceptionDao<SentenceLabel, Integer> getRuntimeExceptionSentenceLabelDao() {
        if (runtimeSentenceLabelDao == null) {
            runtimeSentenceLabelDao = getRuntimeExceptionDao(SentenceLabel.class);
        }
        return runtimeSentenceLabelDao;
    }
    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        diaryDao = null;
        runtimeDiaryDao = null;
        diarybookDao = null;
        runtimeDiarybookDao = null;
    }

    public RuntimeExceptionDao<Label, Integer> getRuntimeExceptionLabelDao() {
        if (runtimeLabelDao == null) {
            runtimeLabelDao = getRuntimeExceptionDao(Label.class);
        }
        return runtimeLabelDao;
    }
}
