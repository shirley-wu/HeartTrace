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

@DatabaseTable(tableName = "sentencebook")
public class Sentencebook implements Serializable {

    protected static final String TAG = "Sentencebook";

    private static final String default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(unique = true, columnName = "sentencebookName", canBeNull = false)
    private String sentencebookName;

    @DatabaseField
    private String description;

    public Sentencebook(){};
    public Sentencebook(String sentencebookName)
    {
        this.sentencebookName = sentencebookName;
    }

    public String getSentencebookName()
    {
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

    public List<Sentence> getAllSubSentence(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            List<Sentence> subSentenceList = dao.queryBuilder().where().eq(Sentencebook.TAG, this).query();
            return subSentenceList;
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void deleteSubSentence(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            DeleteBuilder<Sentence, Integer> deleteBuilder = dao.deleteBuilder();

            deleteBuilder.where().eq(Sentencebook.TAG, this);
            deleteBuilder.delete();
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
    public void insert(DatabaseHelper helper) {
        try {
            Dao<Sentencebook, Integer> dao = helper.getSentencebookDao();
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
            Dao<Sentencebook, Integer> dao = helper.getSentencebookDao();
            Log.i("sentencebook", "dao = " + dao + " 更新 sentencebook " + this);
            int returnValue = dao.update(this);
            Log.i("sentencebook", "更新后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> subdao = helper.getSentenceDao();
            DeleteBuilder<Sentence, Integer> deleteBuilder = subdao.deleteBuilder();

            deleteBuilder.where().eq(Sentencebook.TAG, this);
            deleteBuilder.delete();

            Dao<Sentencebook, Integer> dao = helper.getSentencebookDao();
            Log.i("sentencebook", "dao = " + dao + " 删除 sentencebook " + this);
            int returnValue = dao.delete(this);
            Log.i("sentencebook", "删除后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Sentencebook> getAll(DatabaseHelper helper, Boolean ascending){
        try {
            QueryBuilder<Sentencebook, Integer> qb = helper.getSentencebookDao().queryBuilder();
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static Sentencebook getByName(DatabaseHelper helper,String sentencebookName) {
        try {
            Dao<Sentencebook, Integer> dao = helper.getSentencebookDao();
            Sentencebook bookByName = dao.queryBuilder().where().eq("sentencebookName", sentencebookName).queryForFirst();
            return bookByName;
        }
        catch(SQLException e) {
            Log.e(TAG, "getByName: cannot query");
            return null;
        }
    }
}