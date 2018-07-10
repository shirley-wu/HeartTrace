package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "tb_sentence_label")
public class SentenceLabel {
    public static final String TAG = "tb_sentence_label";
    public static final String SENTENCE_TAG = "tb_sentence";
    public static final String LABEL_TAG = "tb_label";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id ;

    @DatabaseField(foreign = true, columnName = SENTENCE_TAG)
    private Sentence sentence;

    @DatabaseField(foreign = true, columnName = LABEL_TAG)
    private Label label;

    public SentenceLabel(){}

    public SentenceLabel(Sentence sentence, Label label){
        this.sentence = sentence;
        this.label = label;
    }

    public int getId(){
        return id;
    }
    public Sentence getSentence(){
        return sentence;
    }
    public Label getLabel(){
        return label;
    }
    public void setSentence(Sentence sentence){
        this.sentence = sentence;
    }
    public void setLabel(Label label){
        this.label = label;
    }

    public void insert(DatabaseHelper helper) {
        try {
            Dao<SentenceLabel, Integer> dao = helper.getDao(SentenceLabel.class);
            Log.i("db_diary_label", "dao = " + dao + "  db_diary_label " + this);
            int returnValue = dao.create(this);
            Log.i("db_diary_label", "插入后返回值："+returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            Dao<SentenceLabel, Integer> dao = helper.getDao(SentenceLabel.class);
            Log.i("db_diary_label", "dao = " + dao + "  db_diary_label " + this);
            int returnValue = dao.delete(this);
            Log.i("db_diary_label", "删除后返回值："+returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    /*public static QueryBuilder<Sentence, Integer> querySentenceByLabel(DatabaseHelper helper, Label label){
        try{
            Dao<SentenceLabel, Integer> sentenceLabelDao = helper.getSentenceLabelDao();
            QueryBuilder<SentenceLabel, Integer> sentenceLabelBuilder = sentenceLabelDao.queryBuilder();
            sentenceLabelBuilder.selectColumns(SentenceLabel.SENTENCE_TAG);

            Dao<Sentence, Integer> sentenceDao = helper.getSentenceDao();
            SelectArg labelSelectArg = new SelectArg();
            //设置条件语句（where label_id=?）
            sentenceLabelBuilder.where().eq(DiaryLabel.LABEL_TAG, labelSelectArg);
            //创建外部查询日记表

            QueryBuilder<Sentence, Integer> postQb = sentenceDao.queryBuilder();
            //设置查询条件（where project_id in()）;
            postQb.where().in(Diary.TAG, sentenceLabelBuilder);
            return postQb;
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static QueryBuilder<Label, String> queryLabelBySentence(DatabaseHelper helper, Sentence sentence){
        try{
            Dao<DiaryLabel, Integer> diaryLabelDao = helper.getDiaryLabelDao();
            QueryBuilder<DiaryLabel, Integer> diaryLabelBuilder = diaryLabelDao.queryBuilder();

            diaryLabelBuilder.selectColumns(DiaryLabel.LABEL_TAG);
            SelectArg selectArg = new SelectArg();
            //设置条件语句（where label_id=?）
            diaryLabelBuilder.where().eq(DiaryLabel.DIARY_TAG, selectArg);
            //创建外部查询日记表

            Dao<Label, String> labelDao = helper.getLabelDao();
            QueryBuilder<Label, String> labelQb = labelDao.queryBuilder();
            //设置查询条件（where project_id in()）;
            labelQb.where().in(Label.TAG, diaryLabelBuilder);
            return labelQb;
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }

        Dao<Diary, Integer> diaryDao = helper.getDiaryDao();
        QueryBuilder<Diary, Integer> query = diaryDao.queryBuilder();
    }*/
}
