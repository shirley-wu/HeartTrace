package com.example.dell.db;

import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by huang on 5/10/2018.
 */

@DatabaseTable(tableName = "Sentence")
public class Sentence implements Serializable {
    protected static final String TAG = "Sentence";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true, columnName = Sentencebook.TAG, canBeNull = false)
    private Sentencebook sentencebook;

    @DatabaseField
    String text;

    @DatabaseField(dataType = DataType.DATE_STRING, columnName = "date", canBeNull = false)
    protected Date date;

    public Sentence(){
    };

    public Sentence(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
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
            Log.i("Sentence", "dao = " + dao + " 插入 Sentence " + this);
            Log.d(TAG, "insert: text = " + getText());
            if(getSentencebook() == null) {
                Log.d(TAG, "insert: sentencebook = null");
            } else {
                Log.d(TAG, "insert: sentencebook name = " + getSentencebook().getSentencebookName());
            }
            int returnValue = dao.create(this);
            Log.i("Sentence", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void update(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("Sentence", "dao = " + dao + " 更新 Sentence " + this);
            int returnValue = dao.update(this);
            Log.i("Sentence", "更新后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            DeleteBuilder<SentenceLabel, Integer> deleteBuilder = helper.getSentenceLabelDao().deleteBuilder();
            deleteBuilder.where().eq(SentenceLabel.SENTENCE_TAG, this);
            deleteBuilder.delete();

            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("Sentence", "dao = " + dao + " 删除 Sentence " + this);
            int returnValue = dao.delete(this);
            Log.i("Sentence", "删除后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void insertLabel(DatabaseHelper helper, Label label) {
        SentenceLabel SentenceLabel = new SentenceLabel();
        SentenceLabel.setSentence(this);
        SentenceLabel.setLabel(label);
        SentenceLabel.insert(helper);
    }

    public void deleteLabel(DatabaseHelper helper, Label label) {
        SentenceLabel SentenceLabel = new SentenceLabel();
        SentenceLabel.setSentence(this);
        SentenceLabel.setLabel(label);
        SentenceLabel.delete(helper);
    }

    public static List<Sentence> getByDate(DatabaseHelper helper, Date date) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            List<Sentence> SentenceList = dao.queryBuilder().where().eq("date", date).query();
            return SentenceList;
        }
        catch(SQLException e) {
            Log.e(TAG, "getSentenceByDate: cannot query");
            return null;
        }
    }

    public static List<Sentence> getAll(DatabaseHelper helper, Boolean ascending){
        try {
            QueryBuilder<Sentence, Integer> qb = helper.getSentenceDao().queryBuilder();
            qb.orderBy("date",ascending);
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public List<Label> getAllLabel(DatabaseHelper helper) throws SQLException {
        QueryBuilder<SentenceLabel, Integer> qb = helper.getSentenceLabelDao().queryBuilder();
        qb.where().eq(SentenceLabel.SENTENCE_TAG, this);

        QueryBuilder<Label, Integer> labelQb = helper.getLabelDao().queryBuilder();
        labelQb.join(qb);

        return labelQb.query();
    }

    public static List<Sentence> getByRestrict(DatabaseHelper helper, String text, Date begin, Date end, List<Label> labelList, Boolean ascending) throws SQLException {
        QueryBuilder<Sentence, Integer> qb = helper.getSentenceDao().queryBuilder();

        Boolean status1 = (text != null);
        Boolean status2 = (begin != null && end != null);
        if(status1 || status2){
            Where<Sentence, Integer> where = qb.where();
            if (status1) buildWhere(where, text);
            if (status2) buildWhere(where, begin, end);
            if (status1 && status2) where.and(2);
        }

        if(labelList != null && labelList.size() > 0) {
            buildQuery(qb, helper, labelList);
        }

        qb.orderBy("date", ascending);

        Log.d(TAG, "getByRestrict: " + qb.prepareStatementString());
        return qb.query();
    }

    public static long countByDateLabel (DatabaseHelper helper, Date begin, Date end, List<Label> labelList) {
        try {
            QueryBuilder<Sentence, Integer> queryBuilder = helper.getSentenceDao().queryBuilder();
            buildQuery(queryBuilder, helper, labelList);
            buildWhere(queryBuilder.where(), begin, end);
            return queryBuilder.countOf();
        }
        catch(SQLException e) {
            Log.e(TAG, "countByDateLabel: cannot access database", e);
            return -1;
        }
    }

    public static void buildQuery(QueryBuilder<Sentence, Integer> qb, DatabaseHelper helper, List<Label> labelList) throws SQLException {
        int size = labelList.size();
        if (size == 0) {
            throw new SQLException("Label list is empty, cannot construct");
        }

        for(int i = 0; i < size; i++) {
            QueryBuilder<SentenceLabel, Integer> sentenceLabelQb = helper.getSentenceLabelDao().queryBuilder();
            sentenceLabelQb.where().eq(DiaryLabel.LABEL_TAG, labelList.get(i));
            sentenceLabelQb.setAlias("query" + i);
            qb.join(sentenceLabelQb, QueryBuilder.JoinType.INNER, QueryBuilder.JoinWhereOperation.AND);
        }

        Log.d(TAG, "buildQuery: " + qb.prepareStatementString());
    }

    public static void buildWhere(Where<Sentence, Integer> where, String text) throws SQLException {
        String[] keywordList = text.split(" ");
        int length = keywordList.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                where.like("text", "%" + keywordList[i] + "%");
            }
            where.or(length);
        }
    }

    public static void buildWhere(Where<Sentence, Integer> where, Date begin, Date end) throws SQLException {
        where.between("date", begin, end);
    }
}
