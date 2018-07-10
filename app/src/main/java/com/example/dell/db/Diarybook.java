package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.DeleteBuilder;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by chen on 2018/5/13.
 */

@DatabaseTable(tableName = "diarybook")
public class Diarybook implements Serializable {

    protected static final String TAG = "Diarybook";

    private static final String default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(unique = true, columnName = "diarybookName", canBeNull = false)
    private String diarybookName;

    @DatabaseField
    private String description;

    public Diarybook(){};
    public Diarybook(String diarybookName)
    {
        this.diarybookName = diarybookName;
    }

    public String getDiarybookName()
    {
        return diarybookName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDiarybookName(String diarybookName) {
        this.diarybookName = diarybookName;
    }

    public List<Diary> getAllSubDiary(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> dao = helper.getDao(Diary.class);
            List<Diary> subDiaryList = dao.queryBuilder().where().eq(Diarybook.TAG, this).query();
            return subDiaryList;
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void deleteSubDiary(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> dao = helper.getDao(Diary.class);
            DeleteBuilder<Diary, Integer> deleteBuilder = dao.deleteBuilder();

            deleteBuilder.where().eq(Diarybook.TAG, this);
            deleteBuilder.delete();
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
    public void insert(DatabaseHelper helper) {
        try {
            Dao<Diarybook, Integer> dao = helper.getDao(Diarybook.class);
            Log.i("diarybook", "dao = " + dao + " 插入 diarybook " + this);
            int returnValue = dao.create(this);
            Log.i("diarybook", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void update(DatabaseHelper helper) {
        try {
            Dao<Diarybook, Integer> dao = helper.getDao(Diarybook.class);
            Log.i("diarybook", "dao = " + dao + " 更新 diarybook " + this);
            int returnValue = dao.update(this);
            Log.i("diarybook", "更新后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void refresh(DatabaseHelper helper) {
        try {
            Dao<Diarybook, Integer> dao = helper.getDao(Diarybook.class);
            Log.i("diarybook", "dao = " + dao + " refresh diarybook " + this);
            int returnValue = dao.refresh(this);
            Log.i("diarybook", "refresh后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> subdao = helper.getDao(Diary.class);
            DeleteBuilder<Diary, Integer> deleteBuilder = subdao.deleteBuilder();

            deleteBuilder.where().eq(Diarybook.TAG, this);
            deleteBuilder.delete();

            Dao<Diarybook, Integer> dao = helper.getDao(Diarybook.class);
            Log.i("diarybook", "dao = " + dao + " 删除 diarybook " + this);
            int returnValue = dao.delete(this);
            Log.i("diarybook", "删除后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Diarybook> getAll(DatabaseHelper helper, Boolean ascending){
        try {
            QueryBuilder<Diarybook, Integer> qb = helper.getDao(Diarybook.class).queryBuilder();
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static Diarybook getByName(DatabaseHelper helper,String diarybookName) {
        try {
            Dao<Diarybook, Integer> dao = helper.getDao(Diarybook.class);
            Diarybook bookByName = dao.queryBuilder().where().eq("diarybookName", diarybookName).queryForFirst();
            return bookByName;
        }
        catch(SQLException e) {
            Log.e(TAG, "getByName: cannot query");
            return null;
        }
    }
}