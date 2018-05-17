package example.hearttrace;

import android.provider.ContactsContract;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by huang on 5/10/2018.
 */

@DatabaseTable(tableName = "Sentence")
public class Sentence {
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

    public void deleteSentence(DatabaseHelper helper) {
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


    public static List<Sentence> getSentenceByDate(DatabaseHelper helper, Date date) {
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

    public static List<Sentence> getAllSentence(DatabaseHelper helper){
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
}
