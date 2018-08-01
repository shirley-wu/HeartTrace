package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "tb_sentence_label")
public class SentenceLabel {

    public static final String TAG = "tb_sentence_label";

    public static final String SENTENCE_TAG = "tb_sentence";

    public static final String LABEL_TAG = "tb_label";

    @DatabaseField(id = true, columnName = TAG)
    private long id;

    @DatabaseField(foreign = true, columnName = SENTENCE_TAG)
    private Sentence sentence;

    @DatabaseField(foreign = true, columnName = LABEL_TAG)
    private Label label;

    @DatabaseField
    private int status;

    @DatabaseField
    private long modified;

    public SentenceLabel(){}

    public SentenceLabel(Sentence sentence, Label label){
        this.sentence = sentence;
        this.label = label;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Sentence getSentence(){
        return sentence;
    }

    public void setSentence(Sentence sentence){
        this.sentence = sentence;
    }

    public Label getLabel(){
        return label;
    }

    public void setLabel(Label label){
        this.label = label;
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
            Dao<SentenceLabel, Long> dao = helper.getDaoAccess(SentenceLabel.class);
            Log.i("db_sentence_label", "dao = " + dao + "  tb_sentence_label " + this);
            int returnValue = dao.create(this);
            Log.i("db_sentence_label", "插入后返回值："+returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            status = -1;
            modified = System.currentTimeMillis();
            Dao<SentenceLabel, Long> dao = helper.getDaoAccess(SentenceLabel.class);
            Log.i("db_sentence_label", "dao = " + dao + "  tb_sentence_label " + this);
            int returnValue = dao.update(this);
            Log.i("db_sentence_label", "删除后返回值："+returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
}
