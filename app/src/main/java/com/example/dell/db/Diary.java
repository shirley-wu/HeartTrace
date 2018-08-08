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

@DatabaseTable(tableName = "Diary")
public class Diary implements Serializable
{
    protected static final String TAG = "diary";

    @DatabaseField(id = true, columnName = TAG)
    private long id;

    @DatabaseField(foreign = true, columnName = Diarybook.TAG)//, canBeNull = false)
    private Diarybook diarybook;

    @DatabaseField
    private String htmlText;

    @DatabaseField
    private String text;

    @DatabaseField(dataType = DataType.DATE_STRING, columnName = "date", canBeNull = false)
    protected Date date;

    @DatabaseField
    private boolean islike = false;

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

    @DatabaseField
    private int fontType = 2;

    @DatabaseField
    private int alignmentType = 2;

    @DatabaseField
    private int background = 0;

    public Diary(){
    };

    public Diary(String text){
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

    public boolean getIslike() {
        return islike;
    }

    public void setIslike(boolean islike) {
        this.islike = islike;
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

    public int getFontType() {
        return fontType;
    }

    public void setFontType(int fontType) {
        this.fontType = fontType;
    }

    public int getAlignmentType() {
        return alignmentType;
    }

    public void setAlignmentType(int alignmentType) {
        this.alignmentType = alignmentType;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int insert(DatabaseHelper helper) {
        try {
            id = helper.getIdWorker().nextId();
            Log.d(TAG, "insert: id = " + id);
            status = 0;
            modified = System.currentTimeMillis();
            Dao<Diary, Long> dao = helper.getDaoAccess(Diary.class);
            Log.d("diary", "dao = " + dao + " 插入 diary " + this);
            int returnValue = dao.create(this);
            Log.d("diary", "插入后返回值：" + returnValue);
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
            Dao<Diary, Long> dao = helper.getDaoAccess(Diary.class);
            Log.d("diary", "dao = " + dao + " 更新 diary " + this);
            int returnValue = dao.update(this);
            Log.d("diary", "更新后返回值：" + returnValue);
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
            Dao<Diary, Long> dao = helper.getDaoAccess(Diary.class);
            Log.d("diary", "dao = " + dao + " 删除 diary " + this);
            returnValue = dao.update(this);
            Log.d("diary", "删除后返回值：" + returnValue);

            UpdateBuilder<DiaryLabel, Long> updateBuilder = helper.getDaoAccess(DiaryLabel.class).updateBuilder();
            updateBuilder.
                    updateColumnValue("status", -1).
                    updateColumnValue("modified", System.currentTimeMillis());
            updateBuilder.where().eq(DiaryLabel.DIARY_TAG, this);
            Log.d("diary", "批量删除 diary label " + this);
            returnValue = updateBuilder.update();
            Log.d("diary", "删除后返回值：" + returnValue);
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
            QueryBuilder<DiaryLabel, Long> qb = helper.getDaoAccess(DiaryLabel.class).queryBuilder();
            Where<DiaryLabel, Long> where = qb.where();
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

    public static List<Diary> getByDate(DatabaseHelper helper, int year, int month, int day, boolean ascending) {
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

            QueryBuilder<Diary, Long> qb = helper.getDaoAccess(Diary.class).queryBuilder();

            Where<Diary, Long> where = qb.where();
            buildWhere(where, begin, end);
            where.ge("status", 0);
            where.and(2);

            qb.orderBy("date",ascending);
            List<Diary> diaryList = qb.query();
            return diaryList;
        }
        catch(SQLException e) {
            Log.e(TAG, "getDiaryByDate: cannot query");
            return null;
        }
    }

    public static List<Diary> getAll(DatabaseHelper helper, boolean ascending){
        try {
            QueryBuilder<Diary, Long> qb = helper.getDaoAccess(Diary.class).queryBuilder();
            qb.where().ge("status", 0);
            qb.orderBy("date",ascending);
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Diary> getAllLike(DatabaseHelper helper, Boolean ascending){
        try {
            QueryBuilder<Diary, Long> qb = helper.getDaoAccess(Diary.class).queryBuilder();
            qb.where().eq("islike", true).and().ge("status", 0);
            qb.orderBy("date", ascending);
            return qb.query();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public List<Label> getAllLabel(DatabaseHelper helper) throws SQLException {
        QueryBuilder<DiaryLabel, Long> qb = helper.getDaoAccess(DiaryLabel.class).queryBuilder();
        qb.where().eq(DiaryLabel.DIARY_TAG, this).and().ge("status", 0);

        QueryBuilder<Label, Long> labelQb = helper.getDaoAccess(Label.class).queryBuilder();
        labelQb.join(qb);

        return labelQb.query();
    }

    public static List<Diary> getByRestrict(DatabaseHelper helper, String text, Date begin,
                                            Date end, List<Label> labelList, Boolean ascending) throws SQLException {
        QueryBuilder<Diary, Long> qb = helper.getDaoAccess(Diary.class).queryBuilder();

        Boolean status1 = (text != null);
        Boolean status2 = (begin != null && end != null);
        if(status1 || status2){
            Where<Diary, Long> where = qb.where();
            if (status1) buildWhere(where, text);
            if (status2) buildWhere(where, begin, end);
            where.ge("status", 0);
            if (status1 && status2) where.and(3);
            else where.and(2);
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
            QueryBuilder<Diary, Long> queryBuilder = helper.getDaoAccess(Diary.class).queryBuilder();
            Where<Diary, Long> where = queryBuilder.where();
            buildWhere(where, begin, end);
            where.ge("status", 0);
            where.and(2);
            buildQuery(queryBuilder, helper, labelList);
            return queryBuilder.countOf();
        }
        catch(SQLException e) {
            Log.e(TAG, "countByDateLabel: cannot access database", e);
            return -1;
        }
    }

    public static void buildQuery(QueryBuilder<Diary, Long> qb, DatabaseHelper helper, List<Label> labelList) throws SQLException {
        int size = labelList.size();
        if (size == 0) {
            throw new SQLException("Label list is empty, cannot construct");
        }

        for(int i = 0; i < size; i++) {
            QueryBuilder<DiaryLabel, Long> diaryLabelQb = helper.getDaoAccess(DiaryLabel.class).queryBuilder();
            diaryLabelQb.where().eq(DiaryLabel.LABEL_TAG, labelList.get(i));
            diaryLabelQb.setAlias("query" + i);
            qb.join(diaryLabelQb, QueryBuilder.JoinType.INNER, QueryBuilder.JoinWhereOperation.AND);
        }

        Log.d(TAG, "buildQuery: " + qb.prepareStatementString());
    }

    public static void buildWhere(Where<Diary, Long> where, String text) throws SQLException {
        String[] keywordList = text.split(" ");
        int length = keywordList.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                where.like("text", "%" + keywordList[i] + "%");
            }
            where.or(length);
        }
    }

    public static void buildWhere(Where<Diary, Long> where, Date begin, Date end) throws SQLException {
        where.between("date", begin, end);
    }
}
