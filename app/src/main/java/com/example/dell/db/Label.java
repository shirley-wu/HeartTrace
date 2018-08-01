package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.nio.file.attribute.DosFileAttributes;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "Label")
public class Label {
    public static final String TAG = "label";

    @DatabaseField(id = true, columnName = TAG)
    private long id;

    @DatabaseField(unique = true, columnName = "labelname")
    private String labelname;
    
    @DatabaseField
    private int status;
    
    @DatabaseField
    private long modified;

    public Label(){}

    public Label(String labelname){
        this.labelname = labelname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getLabelname(){
        return labelname;
    }

    public void setLabelname(String labelname){
        this.labelname = labelname;
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

    public void insert(DatabaseHelper helper) {
        try {
            id = helper.getIdWorker().nextId();
            Log.d(TAG, "insert: id = " + id);
            status = 0;
            modified = System.currentTimeMillis();
            Dao<Label, Long> dao = helper.getDaoAccess(Label.class);
            Log.d("label", "dao = " + dao + "  label " + this);
            int returnValue = dao.create(this);
            Log.d("label", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            int returnValue;

            status = -1;
            modified = System.currentTimeMillis();
            Dao<Label, Long> dao = helper.getDaoAccess(Label.class);
            Log.d("label", "dao = " + dao + " 删除 label " + this);
            returnValue = dao.update(this);
            Log.d("label", "删除后返回值：" + returnValue);

            UpdateBuilder<DiaryLabel, Long> diaryLabelIntegerUpdateBuilder = helper.getDaoAccess(DiaryLabel.class).updateBuilder();
            diaryLabelIntegerUpdateBuilder.
                    updateColumnValue("status", -1).
                    updateColumnValue("modified", System.currentTimeMillis());
            diaryLabelIntegerUpdateBuilder.where().eq(DiaryLabel.LABEL_TAG, this);
            Log.d("label", "批量删除 diary label " + this);
            returnValue = diaryLabelIntegerUpdateBuilder.update();
            Log.d("label", "删除后返回值：" + returnValue);

            UpdateBuilder<SentenceLabel, Long> sentenceLabelIntegerUpdateBuilder = helper.getDaoAccess(SentenceLabel.class).updateBuilder();
            sentenceLabelIntegerUpdateBuilder.
                    updateColumnValue("status", -1).
                    updateColumnValue("modified", System.currentTimeMillis());
            sentenceLabelIntegerUpdateBuilder.where().eq(SentenceLabel.LABEL_TAG, this);
            Log.d("label", "批量删除 sentence label " + this);
            returnValue = sentenceLabelIntegerUpdateBuilder.update();
            Log.d("label", "删除后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Label> getAllLabel(DatabaseHelper helper){
        try {
            Dao<Label, Long> dao = helper.getDaoAccess(Label.class);
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    /*public static QueryBuilder<Label, Long> makeQueryBuilderForDiary(DatabaseHelper helper, Diary diary){
        try{
            Dao<DiaryLabel, Long> diaryLabelDao = helper.getDaoAccess(DiaryLabel.class);
            Dao<Label, Long> labelDao = helper.getDaoAccess(Label.class);
            QueryBuilder<DiaryLabel, Long> diaryLabelBuilder = diaryLabelDao.queryBuilder();
            //diaryLabelBuilder.selectColumns(DiaryLabel.LABEL_TAG);
            //SelectArg diarySelectAry = new SelectArg();
            diaryLabelBuilder.where().eq(DiaryLabel.DIARY_TAG, diary);
            QueryBuilder<Label, Long> labelQueryBuilder = labelDao.queryBuilder();
            labelQueryBuilder.where().in(Label.TAG, diaryLabelBuilder);
            return labelQueryBuilder;
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }*/

    public static Label getByName(DatabaseHelper helper, String name) {
        try {
            Dao<Label, Long> dao = helper.getDaoAccess(Label.class);
            Label label = dao.queryBuilder().where().eq("labelname", name).queryForFirst();
            return label;
        }
        catch(SQLException e) {
            Log.e(TAG, "getByName: cannot query");
            return null;
        }
    }
}
