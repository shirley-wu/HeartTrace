package example.hearttrace;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
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
    String text;

    @DatabaseField(dataType = DataType.DATE_STRING, columnName = "date")
    protected Date date;

    public Diary(){
    };

    public Diary(String text)
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

    public Diarybook getDiarybook() {
        return diarybook;
    }

    public void setDiarybook(Diarybook diarybook) {
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
            QueryBuilder<Label, Integer> qb = getLabelQueryBuilder(helper);
            List<Label> labelList = qb.query();
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

    public List<Label> getAllLabel(DatabaseHelper helper){
        try{
            QueryBuilder<Label, Integer> labelDao = getLabelQueryBuilder(helper);
            return labelDao.query();
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Diary> getByRestrict(DatabaseHelper helper, String text, Date begin, Date end, List<Label> labelList) throws SQLException {
        QueryBuilder<Diary, Integer> qb;
        if(labelList != null && labelList.size() > 0){
            qb = getDiaryQueryBuilder(helper, labelList);
        }
        else{
            qb = helper.getDiaryDao().queryBuilder();
        }
        if(text != null){
            buildWhere(qb.where(), text);
        }
        if(begin != null && end != null){
            buildWhere(qb.where(), begin, end);
        }
        return qb.query();
    }

    public static int countByDateLabel (DatabaseHelper helper, Date begin, Date end, List<Label> labelList) {
        try {
            QueryBuilder<Diary, Integer> queryBuilder = helper.getDiaryDao().queryBuilder();
            queryBuilder.where().between("Date", begin, end);
            return queryBuilder.query().size();
        }
        catch(SQLException e) {
            Log.e(TAG, "countByDateLabel: cannot access database", e);
            return -1;
        }
    }

    private QueryBuilder<Label, Integer> getLabelQueryBuilder(DatabaseHelper helper) {
        // TODO
        return null;
    }

    private static QueryBuilder<Diary, Integer> getDiaryQueryBuilder(DatabaseHelper helper, List<Label> labelList) {
        // TODO
        return null;
    }

    private static void buildWhere(Where<Diary, Integer> where, String text) throws SQLException {
        String[] keywordList = text.split(" ");
        where.and();
        for(final String keyword : keywordList) {
            where.or().like("text", keyword);
        }
    }

    private static void buildWhere(Where<Diary, Integer> where, Date begin, Date end) throws SQLException {
        where.and();
        where.between("date", begin, end);
    }
}
