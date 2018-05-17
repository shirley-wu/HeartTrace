package example.hearttrace;

import android.provider.ContactsContract;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.query.In;
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

    public static List<Diary> getDiaryByDate(DatabaseHelper helper, Date date) {
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

    public void updateDiary(DatabaseHelper helper) {
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

    public void deleteDiary(DatabaseHelper helper) {
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

    public static List<Diary> getAllDiary(DatabaseHelper helper){
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    private static PreparedQuery<Diary> makePostsForLabelQuery(DatabaseHelper helper){
        try{
            Dao<DiaryLabel, Integer> diaryLabelDao = helper.getDiaryLabelDao();
            Dao<Diary, Integer> diaryDao = helper.getDiaryDao();
            QueryBuilder<DiaryLabel, Integer> diaryLabelBuilder = diaryLabelDao.queryBuilder();
            diaryLabelBuilder.selectColumns(DiaryLabel.DIARY_TAG);
            SelectArg labelSelectArg = new SelectArg();
            //设置条件语句（where label_id=?）
            diaryLabelBuilder.where().eq(DiaryLabel.LABEL_TAG, labelSelectArg);
            //创建外部查询日记表
            QueryBuilder<Diary, Integer> postQb = diaryDao.queryBuilder();
            //设置查询条件（where project_id in()）;
            postQb.where().in(Diary.TAG, diaryLabelBuilder);
            return postQb.prepare();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Diary> lookupForLabel(DatabaseHelper helper, Label label) throws SQLException {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();
            PreparedQuery<Diary> diaryForLabelQuery = makePostsForLabelQuery(helper);
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
}
