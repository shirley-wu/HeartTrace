package example.hearttrace;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wu-pc on 2018/5/9.
 */

@DatabaseTable(tableName = "Diary")
public class Diary {
    public static final String TAG = "diary";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true, columnName = "diarybook")
    private Diarybook diarybook;

    @DatabaseField
    private String text;

    @DatabaseField(dataType = DataType.DATE_STRING, columnName = "date")
    protected Date date;

    @ForeignCollectionField
    private ForeignCollection<DiaryLabel> diaryLabels;

    public Diary(){
    };

    public Diary(String text){
        this.text = text;
    }

<<<<<<< HEAD
    public String getText()
    {
=======
    public int getId()
    {
        return id;
    }

    public String getText() {
>>>>>>> db-class
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

    public void refresh(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            Log.i("diary", "dao = " + dao + " 插入 diary " + this);
            int returnValue = dao.refresh(this);
            Log.i("diary", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            List<Label> labelList = getAllLabel(helper);
            for(final Label label : labelList) {
                deleteLabel(helper, label);
            }
            Log.i(TAG, "delete: 删除标签对应" + labelList);

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
        DiaryLabel diaryLabel = new DiaryLabel();
        diaryLabel.setDiary(this);
        diaryLabel.setLabel(label);
        diaryLabel.delete(helper);
    }

    public static List<Diary> getByDate(DatabaseHelper helper, Date date) {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            List<Diary> diaryList = dao.queryBuilder().where().eq("date", date).query();
            return diaryList;
        }
        catch(SQLException e) {
            Log.e(TAG, "getDiaryByDate: cannot query");
            return null;
        }
    }

    public static List<Diary> getAll(DatabaseHelper helper){
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public List<Label> getAllLabel(DatabaseHelper helper) throws SQLException {
        refresh(helper);

        List<Label> labelList = new ArrayList();
        for (DiaryLabel diaryLabel : diaryLabels) {
            labelList.add(diaryLabel.getLabel());
        }

        return labelList;
    }

    public static List<Diary> getByRestrict(DatabaseHelper helper, String text, Date begin, Date end, List<Label> labelList) throws SQLException {
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

        Log.d(TAG, "getByRestrict: " + qb.prepareStatementString());
        return qb.query();
    }

    public static int countByDateLabel (DatabaseHelper helper, Date begin, Date end, List<Label> labelList) {
        try {
            QueryBuilder<Diary, Integer> queryBuilder = helper.getDiaryDao().queryBuilder();
            buildQuery(queryBuilder, helper, labelList);
            buildWhere(queryBuilder.where(), begin, end);
            return queryBuilder.query().size();
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
