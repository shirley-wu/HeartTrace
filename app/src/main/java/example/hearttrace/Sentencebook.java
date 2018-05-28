package example.hearttrace;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.DeleteBuilder;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by chen on 2018/5/13.
 */

@DatabaseTable(tableName = "sentencebook")
public class Sentencebook {

    private static final String TAG = "Sentencebook";

    private static final String default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;
    @DatabaseField
    private String sentencebookName;

    public Sentencebook(){};
    public Sentencebook(String sentencebookName)
    {
        this.sentencebookName = sentencebookName;
    }

    public String getSentencebookName()
    {
        return sentencebookName;
    }

    public void setSentencebookName(String sentencebookName) {
        this.sentencebookName = sentencebookName;
    }

    public List<Sentence> getAllSubSentence(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();

            List<Sentence> subSentenceList = dao.queryBuilder().where().eq("Sentencebook", sentencebookName).query();

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

            deleteBuilder.where().eq("Sentencebook", this);
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

            deleteBuilder.where().eq("Sentencebook", this);
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
}