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

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;
    @DatabaseField(foreign = true, columnName = SENTENCE_TAG)
    private Sentence sentence;
    @DatabaseField(foreign = true, columnName = LABEL_TAG)
    private Label label;
    public SentenceLabel(){}
    public SentenceLabel(Sentence sentence, Label label){
        this.sentence = sentence;
        this.label = label;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
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

    public void insert(DatabaseHelper helper) {
        try {
            Dao<SentenceLabel, Integer> dao = helper.getSentenceLabelDao();
            Log.i("db_sentence_label", "dao = " + dao + "  db_sentence_label " + this);
            int returnValue = dao.create(this);
            Log.i("db_sentence_label", "插入后返回值："+returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            Dao<SentenceLabel, Integer> dao = helper.getSentenceLabelDao();
            Log.i("db_sentence_label", "dao = " + dao + "  db_sentence_label " + this);
            int returnValue = dao.delete(this);
            Log.i("db_sentence_label", "删除后返回值："+returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
}
