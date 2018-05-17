package example.hearttrace;

import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by huang on 5/10/2018.
 * 接口：
 *     getId getText setText getDate setDate
 *     void insert(DatabaseHelper)
 *     void update(DatabaseHelper)
 *     void delete(DatabaseHelper)
 *     void insertLabel(DatabaseHelper, Label)
 *     void insertLabel(DatabaseHelper, List<Label>)
 *     static List<Sentence> getByDate(DatabaseHelper)
 *     static List<Sentence> getAll(DatabaseHelper)
 *     TODO: not ok by wxq, SentenceLabel not provided by wxq
 *     static List<Sentence> lookupForLabel(DatabaseHelper, Label)
 *     static List<Diary> lookupForLabel(DatabaseHelper helper, List<Label> labelList)
 *     static List<Diary> getDiaryByBook (DatabaseHelper helper, Diarybook diarybook)
 *     static int countByDateLabel (DatabaseHelper helper, Date begin, Date end, Label label)
 */

@DatabaseTable(tableName = "Sentence")
public class Sentence { // TODO: quite a lot to revise here. by wxq
    public static final String TAG = "Sentence";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true)
    private Sentencebook sentencebook;

    @DatabaseField
    String text;

    @DatabaseField(dataType = DataType.DATE_STRING)
    protected Date date;

    @DatabaseField(foreign = true)
    private Diary diary;

    public Sentence(){
    };

    public Sentence(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(){
        if(date == null) {
            date = new Date();
        }
    }

    public void setDate(Date date){
        // dangerous!!!!! for test only.
        this.date = date;
        Log.i(TAG, "setDate: dangerous call!");
    }

    public Sentencebook getSentencebook() {
        return sentencebook;
    }

    public void setSentencebook(Sentencebook sentencebook) {
        this.sentencebook = sentencebook;
    }

    public void insert(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("sentence", "dao = " + dao + " 插入 sentence " + this);
            int returnValue = dao.create(this);
            Log.i("sentence", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void update(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("sentence", "dao = " + dao + " 更新 sentence " + this);
            int returnValue = dao.update(this);
            Log.i("sentence", "更新后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("sentence", "dao = " + dao + " 删除 sentence " + this);
            int returnValue = dao.delete(this);
            Log.i("sentence", "删除后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void insertLabel(DatabaseHelper helper, Label label) {
        SentenceLabel sentenceLabel = new SentenceLabel();
        sentenceLabel.setSentence(this);
        sentenceLabel.setLabel(label);
        sentenceLabel.insert(helper);

    }

    public void insertLabel(DatabaseHelper helper, List<Label> labelList) {
        for(final Label label : labelList) {
            insertLabel(helper, label);
        }
    }

    public static List<Sentence> getByDate(DatabaseHelper helper, Date date) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            List<Sentence> diaryList = dao.queryBuilder().where().eq("date", date).query();
            return diaryList;
        }
        catch(SQLException e) {
            Log.e(TAG, "getDiaryByDate: cannot query");
            return null;
        }
    }

    public static List<Sentence> getAll(DatabaseHelper helper){
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void updateSentence(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("sentence", "dao = " + dao + " 更新 sentence " + this);
            int returnValue = dao.update(this);
            Log.i("sentence", "更新后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    private PreparedQuery<Sentence> makePostsForLabel(DatabaseHelper helper){
        try{
            QueryBuilder<SentenceLabel, Integer> sentenceLabelQueryBuilder = helper.getSentenceLabelDao().queryBuilder();
            sentenceLabelQueryBuilder.selectColumns(Sentence.TAG);
            SelectArg labelSelectArg = new SelectArg();
            sentenceLabelQueryBuilder.where().eq(SentenceLabel.LABEL_TAG, labelSelectArg);
            QueryBuilder<Sentence, Integer> postQb = helper.getSentenceDao().queryBuilder();
            postQb.where().in(Sentence.TAG, sentenceLabelQueryBuilder);
            return postQb.prepare();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Sentence> lookupForLabel(DatabaseHelper helper, Label label){
        try{
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            PreparedQuery<Sentence> sentenceForLabelQuery = makePost
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
}
