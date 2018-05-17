package example.hearttrace;

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
 * Created by wu-pc on 2018/5/9.
 * 接口：
 *     getId getText setText getDate setDate
 *     void insert(DatabaseHelper)
 *     void update(DatabaseHelper)
 *     void delete(DatabaseHelper)
 *     void insertLabel(DatabaseHelper, Label)
 *     void insertLabel(DatabaseHelper, List<Label>)
 *     void deleteLabel(DatabaseHelper, Label)
 *     void deleteLabel(DatabaseHelper, List<Label>)
 *     static List<Diary> getByDate(DatabaseHelper)
 *     static List<Diary> getAll(DatabaseHelper)
 *     static List<Diary> lookupForLabel(DatabaseHelper, Label)
 *     static List<Diary> lookupForLabel(DatabaseHelper helper, List<Label> labelList)
 *     static List<Diary> getDiaryByBook (DatabaseHelper helper, Diarybook diarybook)
 *     static int countByDateLabel (DatabaseHelper helper, Date begin, Date end, Label label)
 *     TODO: need get Label
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

    public int getId()
    {
        return id;
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

    public void insertLabel(DatabaseHelper helper, List<Label> labelList) {
        for(final Label label : labelList) {
            insertLabel(helper, label);
        }
    }

    public void deleteLabel(DatabaseHelper helper, Label label) {
        DiaryLabel diaryLabel = new DiaryLabel();
        diaryLabel.setDiary(this);
        diaryLabel.setLabel(label);
        diaryLabel.delete(helper);
    }

    public void deleteLabel(DatabaseHelper helper, List<Label> labelList) {
        for(final Label label : labelList) {
            insertLabel(helper, label);
        }
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
            Dao<Label, String> labelDao = helper.getLabelDao();
            PreparedQuery<Label> labelForDiaryQuery = DiaryLabel.queryLabelByDiary(helper, this).prepare();
            labelForDiaryQuery.setArgumentHolderValue(0, this);
            return labelDao.query(labelForDiaryQuery);
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Diary> lookupForLabel(DatabaseHelper helper, Label label) throws SQLException {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            PreparedQuery<Diary> diaryForLabelQuery = makePostsForLabelQuery(helper).prepare();
            diaryForLabelQuery.setArgumentHolderValue(0, label);
            return dao.query(diaryForLabelQuery);
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Diary> lookupForLabel(DatabaseHelper helper, List<Label> labelList) throws SQLException {
        try {
            List<List<Diary>> diaryListsForAllLabel = null;
            for(Label i : labelList){
                diaryListsForAllLabel.add(lookupForLabel(helper, i));
            }
            for(int i = diaryListsForAllLabel.size()-2; i >= 0 ; i-- ){
                for(Diary j :diaryListsForAllLabel.get(i)){
                    if(!diaryListsForAllLabel.get(i+1).contains(j)){
                        diaryListsForAllLabel.get(i).remove(j);
                    }
                }
            }
            return diaryListsForAllLabel.get(0);
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Diary> getDiaryByBook (DatabaseHelper helper, Diarybook diarybook){
        try {
            Dao<Diary, Integer> diaryDao = helper.getDiaryDao();
            List<Diary> diaryList = diaryDao.queryBuilder().where().eq("diarybook", diarybook).query();
            return diaryList;
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static int countByDateLabel (DatabaseHelper helper, Date begin, Date end, Label label) {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            QueryBuilder<Diary, Integer> queryBuilder = makePostsForLabelQuery(helper);
            queryBuilder.where().between("Date", begin, end);
            return dao.query(queryBuilder.prepare()).size();
        }
        catch(SQLException e) {
            Log.e(TAG, "countByDateLabel: cannot access database", e);
            return -1;
        }
    }
}
