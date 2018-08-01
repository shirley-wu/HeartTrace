package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wu-pc on 2018/5/9.
 */

@DatabaseTable(tableName = "Sentence")
public class Sentence implements Serializable
{
    protected static final String TAG = "sentence";

    @DatabaseField(id = true, columnName = TAG)
    private long id;

    @DatabaseField(foreign = true, columnName = Sentencebook.TAG)//, canBeNull = false)
    private Sentencebook sentencebook;

    @DatabaseField
    private String htmlText;

    @DatabaseField
    private String text;

    @DatabaseField(dataType = DataType.DATE_STRING, columnName = "date", canBeNull = false)
    protected Date date;

    @DatabaseField
    private boolean isLike = false;

    @DatabaseField
    private float letterSpacing = (float)0.2;

    @DatabaseField
    private int lineSpacingMultiplier = 0;

    @DatabaseField
    private int lineSpacingExtra = 1;

    @DatabaseField
    private float textSize = (float) 20;

    @DatabaseField
    private int textAlignment = 0;

    @DatabaseField
    private int status;

    @DatabaseField
    private long modified;

    public Sentence(){
    };

    public Sentence(String text){
        this.text = text;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getText() {
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

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setDate(Date date){
        // dangerous!!!!! for test only.
        this.date = date;
        Log.d(TAG, "setDate: dangerous call!, set into " + date.toString());
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public float getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    public int getLineSpacingMultiplier() {
        return lineSpacingMultiplier;
    }

    public void setLineSpacingMultiplier(int lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
    }

    public int getLineSpacingExtra() {
        return lineSpacingExtra;
    }

    public void setLineSpacingExtra(int lineSpacingExtra) {
        this.lineSpacingExtra = lineSpacingExtra;
    }

    public int getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(int textAlignment) {
        this.textAlignment = textAlignment;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public Sentencebook getSentencebook(){
        return sentencebook;
    }

    public void setSentencebook(Sentencebook sentencebook){
        this.sentencebook = sentencebook;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public long getModified() {
        return this.modified;
    }

    public int insert(DatabaseHelper helper) {
        try {
            id = helper.getIdWorker().nextId();
            Log.d(TAG, "insert: id = " + id);
            status = 0;
            modified = System.currentTimeMillis();
            Dao<Sentence, Long> dao = helper.getDaoAccess(Sentence.class);
            Log.d("sentence", "dao = " + dao + " 插入 sentence " + this);
            int returnValue = dao.create(this);
            Log.d("sentence", "插入后返回值：" + returnValue);
            return returnValue;
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void update(DatabaseHelper helper) {
        try {
            if(status != 0) status = 1;
            modified = System.currentTimeMillis();
            Dao<Sentence, Long> dao = helper.getDaoAccess(Sentence.class);
            Log.d("sentence", "dao = " + dao + " 更新 sentence " + this);
            int returnValue = dao.update(this);
            Log.d("sentence", "更新后返回值：" + returnValue);
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
            Dao<Sentence, Long> dao = helper.getDaoAccess(Sentence.class);
            Log.d("sentence", "dao = " + dao + " 删除 sentence " + this);
            returnValue = dao.update(this);
            Log.d("sentence", "删除后返回值：" + returnValue);

            UpdateBuilder<SentenceLabel, Long> updateBuilder = helper.getDaoAccess(SentenceLabel.class).updateBuilder();
            updateBuilder.
                    updateColumnValue("status", -1).
                    updateColumnValue("modified", System.currentTimeMillis());
            updateBuilder.where().eq(SentenceLabel.SENTENCE_TAG, this);
            Log.d("sentence", "批量删除 sentence label " + this);
            returnValue = updateBuilder.update();
            Log.d("sentence", "删除后返回值：" + returnValue);
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

    public void deleteLabel(DatabaseHelper helper, Label label) {
        try {
            QueryBuilder<SentenceLabel, Long> qb = helper.getDaoAccess(SentenceLabel.class).queryBuilder();
            Where<SentenceLabel, Long> where = qb.where();
            where.eq(SentenceLabel.SENTENCE_TAG, this).and().eq(SentenceLabel.LABEL_TAG, label);
            List<SentenceLabel> l = qb.query();
            for (SentenceLabel dl : l) {
                dl.delete(helper);
            }
        }
        catch(SQLException e) {
            Log.e(TAG, "deleteLabel: ", e);
        }
    }

    public static List<Sentence> getByDate(DatabaseHelper helper, int year, int month, int day, boolean ascending) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date begin = calendar.getTime();
            Log.d(TAG, "getByDate: begin " + begin);

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            Date end = calendar.getTime();
            Log.d(TAG, "getByDate: end "   + end);

            QueryBuilder<Sentence, Long> qb = helper.getDaoAccess(Sentence.class).queryBuilder();
            Where<Sentence, Long> where = qb.where();
            buildWhere(where, begin, end);
            where.ge("status", 0);
            qb.orderBy("date",ascending);
            List<Sentence> sentenceList = qb.query();
            return sentenceList;
        }
        catch(SQLException e) {
            Log.e(TAG, "getSentenceByDate: cannot query");
            return null;
        }
    }

    public static List<Sentence> getAll(DatabaseHelper helper, boolean ascending){
        try {
            QueryBuilder<Sentence, Long> qb = helper.getDaoAccess(Sentence.class).queryBuilder();
            qb.where().ge("status", 0);
            qb.orderBy("date",ascending);
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Sentence> getAllLike(DatabaseHelper helper, Boolean ascending){
        try {
            QueryBuilder<Sentence, Long> qb = helper.getDaoAccess(Sentence.class).queryBuilder();
            Where<Sentence, Long> where = qb.where();
            where.eq("isLike", true);
            where.ge("status", 0);
            qb.orderBy("date", ascending);
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public List<Label> getAllLabel(DatabaseHelper helper) throws SQLException {
        QueryBuilder<SentenceLabel, Long> qb = helper.getDaoAccess(SentenceLabel.class).queryBuilder();
        Where<SentenceLabel, Long> where = qb.where();
        where.eq(SentenceLabel.SENTENCE_TAG, this);
        where.ge("status", 0);

        QueryBuilder<Label, Long> labelQb = helper.getDaoAccess(Label.class).queryBuilder();
        labelQb.join(qb);

        return labelQb.query();
    }

    public static List<Sentence> getByRestrict(DatabaseHelper helper, String text, Date begin,
                                            Date end, List<Label> labelList, Boolean ascending) throws SQLException {
        QueryBuilder<Sentence, Long> qb = helper.getDaoAccess(Sentence.class).queryBuilder();

        Boolean status1 = (text != null);
        Boolean status2 = (begin != null && end != null);
        if(status1 || status2){
            Where<Sentence, Long> where = qb.where();
            if (status1) buildWhere(where, text);
            if (status2) buildWhere(where, begin, end);
            if (status1 && status2) where.and(2);
            where.ge("status", 0);
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
            QueryBuilder<Sentence, Long> queryBuilder = helper.getDaoAccess(Sentence.class).queryBuilder();
            Where<Sentence, Long> where = queryBuilder.where();
            buildQuery(queryBuilder, helper, labelList);
            buildWhere(where, begin, end);
            where.ge("status", 0);
            return queryBuilder.countOf();
        }
        catch(SQLException e) {
            Log.e(TAG, "countByDateLabel: cannot access database", e);
            return -1;
        }
    }

    public static void buildQuery(QueryBuilder<Sentence, Long> qb, DatabaseHelper helper, List<Label> labelList) throws SQLException {
        int size = labelList.size();
        if (size == 0) {
            throw new SQLException("Label list is empty, cannot construct");
        }

        for(int i = 0; i < size; i++) {
            QueryBuilder<SentenceLabel, Long> sentenceLabelQb = helper.getDaoAccess(SentenceLabel.class).queryBuilder();
            sentenceLabelQb.where().eq(SentenceLabel.LABEL_TAG, labelList.get(i));
            sentenceLabelQb.setAlias("query" + i);
            qb.join(sentenceLabelQb, QueryBuilder.JoinType.INNER, QueryBuilder.JoinWhereOperation.AND);
        }

        Log.d(TAG, "buildQuery: " + qb.prepareStatementString());
    }

    public static void buildWhere(Where<Sentence, Long> where, String text) throws SQLException {
        String[] keywordList = text.split(" ");
        int length = keywordList.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                where.like("text", "%" + keywordList[i] + "%");
            }
            where.or(length);
        }
    }

    public static void buildWhere(Where<Sentence, Long> where, Date begin, Date end) throws SQLException {
        where.between("date", begin, end);
    }
}
