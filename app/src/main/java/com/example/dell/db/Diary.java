package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
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

@DatabaseTable(tableName = "Diary")
public class Diary implements Serializable
{
    protected static final String TAG = "diary";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true, columnName = Diarybook.TAG)//, canBeNull = false)
    private Diarybook diarybook;

    @DatabaseField
    private String htmlText;

    @DatabaseField
    private String text;

    @DatabaseField(dataType = DataType.DATE_STRING, columnName = "date", canBeNull = false)
    protected Date date;

    @DatabaseField
    private boolean like = false;

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


    public Diary(){
    };

    public Diary(String text){
        this.text = text;
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
        Log.i(TAG, "setDate: dangerous call!, set into " + date.toString());
    }

    public boolean getLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
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

    public Diarybook getDiarybook(){
        return diarybook;
    }

    public void setDiarybook(Diarybook diarybook){
        this.diarybook = diarybook;
    }

    public void insert(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            Log.i("diary", "dao = " + dao + " 插入 diary " + this);
            int returnValue = dao.create(this);
            Log.i("diary", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void update(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            Log.i("diary", "dao = " + dao + " 更新 diary " + this);
            int returnValue = dao.update(this);
            Log.i("diary", "更新后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            DeleteBuilder<DiaryLabel, Integer> deleteBuilder = helper.getDiaryLabelDao().deleteBuilder();
            deleteBuilder.where().eq(DiaryLabel.DIARY_TAG, this);
            deleteBuilder.delete();

            Dao<Diary, Integer> dao = helper.getDiaryDao();
            Log.i("diary", "dao = " + dao + " 删除 diary " + this);
            int returnValue = dao.delete(this);
            Log.i("diary", "删除后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void insertLabel(DatabaseHelper helper, Label label) {
        DiaryLabel diaryLabel = new DiaryLabel();
        diaryLabel.setDiary(this);
        diaryLabel.setLabel(label);
        diaryLabel.insert(helper);
    }

    public void deleteLabel(DatabaseHelper helper, Label label) {
        try {
            QueryBuilder<DiaryLabel, Integer> qb = helper.getDiaryLabelDao().queryBuilder();
            Where<DiaryLabel, Integer> where = qb.where();
            where.eq(DiaryLabel.DIARY_TAG, this).and().eq(DiaryLabel.LABEL_TAG, label);
            List<DiaryLabel> l = qb.query();
            for (DiaryLabel dl : l) {
                dl.delete(helper);
            }
        }
        catch(SQLException e) {
            Log.e(TAG, "deleteLabel: ", e);
        }
    }

    public static List<Diary> getByDate(DatabaseHelper helper, int year, int month, int day) {
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

            QueryBuilder<Diary, Integer> qb = helper.getDiaryDao().queryBuilder();
            Where<Diary, Integer> where = qb.where();
            buildWhere(where, begin, end);
            List<Diary> diaryList = qb.query();
            return diaryList;
        }
        catch(SQLException e) {
            Log.e(TAG, "getDiaryByDate: cannot query");
            return null;
        }
    }

    public static List<Diary> getAll(DatabaseHelper helper, Boolean ascending){
        try {
            QueryBuilder<Diary, Integer> qb = helper.getDiaryDao().queryBuilder();
            qb.orderBy("date",ascending);
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Diary> getAllLike(DatabaseHelper helper, Boolean ascending){
        try {
            QueryBuilder<Diary, Integer> qb = helper.getDiaryDao().queryBuilder();
            Where<Diary, Integer> where = qb.where();
            where.eq("like", true);
            qb.orderBy("date",ascending);
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public List<Label> getAllLabel(DatabaseHelper helper) throws SQLException {
        QueryBuilder<DiaryLabel, Integer> qb = helper.getDiaryLabelDao().queryBuilder();
        qb.where().eq(DiaryLabel.DIARY_TAG, this);

        QueryBuilder<Label, Integer> labelQb = helper.getLabelDao().queryBuilder();
        labelQb.join(qb);

        return labelQb.query();
    }

    public static List<Diary> getByRestrict(DatabaseHelper helper, String text, Date begin,
                                            Date end, List<Label> labelList, Boolean ascending) throws SQLException {
        QueryBuilder<Diary, Integer> qb = helper.getDiaryDao().queryBuilder();

        Boolean status1 = (text != null);
        Boolean status2 = (begin != null && end != null);
        if(status1 || status2){
            Where<Diary, Integer> where = qb.where();
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
            QueryBuilder<Diary, Integer> queryBuilder = helper.getDiaryDao().queryBuilder();
            buildQuery(queryBuilder, helper, labelList);
            buildWhere(queryBuilder.where(), begin, end);
            return queryBuilder.countOf();
        }
        catch(SQLException e) {
            Log.e(TAG, "countByDateLabel: cannot access database", e);
            return -1;
        }
    }

    public static void buildQuery(QueryBuilder<Diary, Integer> qb, DatabaseHelper helper, List<Label> labelList) throws SQLException {
        int size = labelList.size();
        if (size == 0) {
            throw new SQLException("Label list is empty, cannot construct");
        }

        for(int i = 0; i < size; i++) {
            QueryBuilder<DiaryLabel, Integer> diaryLabelQb = helper.getDiaryLabelDao().queryBuilder();
            diaryLabelQb.where().eq(DiaryLabel.LABEL_TAG, labelList.get(i));
            diaryLabelQb.setAlias("query" + i);
            qb.join(diaryLabelQb, QueryBuilder.JoinType.INNER, QueryBuilder.JoinWhereOperation.AND);
        }

        Log.d(TAG, "buildQuery: " + qb.prepareStatementString());
    }

    public static void buildWhere(Where<Diary, Integer> where, String text) throws SQLException {
        String[] keywordList = text.split(" ");
        int length = keywordList.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                where.like("text", "%" + keywordList[i] + "%");
            }
            where.or(length);
        }
    }

    public static void buildWhere(Where<Diary, Integer> where, Date begin, Date end) throws SQLException {
        where.between("date", begin, end);
    }
}
