package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by chen on 2018/5/13.
 */

@DatabaseTable(tableName = "sentencebook")
public class Sentencebook implements Serializable {

    protected static final String TAG = "Sentencebook";

    private static final String default_name = "default";

    @DatabaseField(id = true, columnName = TAG)
    private long id;

    @DatabaseField(unique = true, columnName = "sentencebookName", canBeNull = false)
    private String sentencebookName;

    @DatabaseField
    private String description;

    @DatabaseField
    private int status;

    @DatabaseField
    private long modified;

    public Sentencebook(){};

    public Sentencebook(String sentencebookName)
    {
        this.sentencebookName = sentencebookName;
    }


    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getSentencebookName() {
        return sentencebookName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSentencebookName(String sentencebookName) {
        this.sentencebookName = sentencebookName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public long getModified() {
        return modified;
    }

    public List<Sentence> getAllSubSentence(DatabaseHelper helper) {
        try {
            Dao<Sentence, Long> dao = helper.getDaoAccess(Sentence.class);
            QueryBuilder<Sentence, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(Sentencebook.TAG, this).and().ge("status", 0);
            List<Sentence> subSentenceList = queryBuilder.query();
            return subSentenceList;
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void deleteSubSentence(DatabaseHelper helper) {
        try {
            UpdateBuilder<Sentence, Long> updateBuilder = helper.getDaoAccess(Sentence.class).updateBuilder();
            updateBuilder.
                    updateColumnValue("status", -1).
                    updateColumnValue("modified", System.currentTimeMillis());
            updateBuilder.where().eq(Sentencebook.TAG, this);
            Log.i("sentence", "批量删除 sentence " + this);
            int returnValue = updateBuilder.update();
            Log.i("sentence", "删除后返回值：" + returnValue);
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
    public void insert(DatabaseHelper helper) {
        try {
            id = helper.getIdWorker().nextId();
            Log.d(TAG, "insert: id = " + id);
            status = 0;
            modified = System.currentTimeMillis();
            Dao<Sentencebook, Long> dao = helper.getDaoAccess(Sentencebook.class);
            Log.i("sentencebook", "dao = " + dao + " 插入 sentencebook " + this);
            int returnValue = dao.create(this);
            Log.i("sentencebook", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void update(DatabaseHelper helper) {
        try {
            if(status != 0) status = 1;
            modified = System.currentTimeMillis();
            Dao<Sentencebook, Long> dao = helper.getDaoAccess(Sentencebook.class);
            Log.i("sentencebook", "dao = " + dao + " 更新 sentencebook " + this);
            int returnValue = dao.update(this);
            Log.i("sentencebook", "更新后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void refresh(DatabaseHelper helper) {
        try {
            Dao<Sentencebook, Long> dao = helper.getDaoAccess(Sentencebook.class);
            Log.i("sentencebook", "dao = " + dao + " refresh sentencebook " + this);
            int returnValue = dao.refresh(this);
            Log.i("sentencebook", "refresh后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            deleteSubSentence(helper);

            status = -1;
            modified = System.currentTimeMillis();
            Dao<Sentencebook, Long> dao = helper.getDaoAccess(Sentencebook.class);
            Log.i("sentencebook", "dao = " + dao + " 删除 sentencebook " + this);
            int returnValue = dao.update(this);
            Log.i("sentencebook", "删除后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Sentencebook> getAll(DatabaseHelper helper, Boolean ascending){
        try {
            QueryBuilder<Sentencebook, Long> qb = helper.getDaoAccess(Sentencebook.class).queryBuilder();
            Where<Sentencebook, Long> where = qb.where();
            where.ge("status", 0);
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static Sentencebook getByName(DatabaseHelper helper,String sentencebookName) {
        try {
            Dao<Sentencebook, Long> dao = helper.getDaoAccess(Sentencebook.class);
            QueryBuilder<Sentencebook, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("sentencebookName", sentencebookName).and().ge("status", 0);
            Sentencebook bookByName = queryBuilder.queryForFirst();
            return bookByName;
        }
        catch(SQLException e) {
            Log.e(TAG, "getByName: cannot query");
            return null;
        }
    }
}