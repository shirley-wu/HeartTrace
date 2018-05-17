package example.hearttrace;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "tb_diary_label")
public class DiaryLabel {
    public static final String TAG = "tb_diary_label";

    public static final String DIARY_TAG = "tb_diary";

    public static final String LABEL_TAG = "tb_label";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;
    @DatabaseField(foreign = true, columnName = DIARY_TAG)
    private Diary diary;
    @DatabaseField(foreign = true, columnName = LABEL_TAG)
    private Label label;
    public DiaryLabel(){}
    public DiaryLabel(Diary diary, Label label){
        this.diary = diary;
        this.label = label;
    }
    public int getId(){
        return id;
    }
    public Diary getDiary(){
        return diary;
    }
    public Label getLabel(){
        return label;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setDiary(Diary diary){
        this.diary = diary;
    }
    public void setLabel(Label label){
        this.label = label;
    }

    public void insert(DatabaseHelper helper) {
        try {
            Dao<DiaryLabel, Integer> dao = helper.getDiaryLabelDao();
            Log.i("db_diary_label", "dao = " + dao + "  db_diary_label " + this);
            int returnValue = dao.create(this);
            Log.i("db_diary_label", "插入后返回值："+returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            Dao<DiaryLabel, Integer> dao = helper.getDiaryLabelDao();
            Log.i("db_diary_label", "dao = " + dao + "  db_diary_label " + this);
            int returnValue = dao.delete(this);
            Log.i("db_diary_label", "插入后返回值："+returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static QueryBuilder<Diary, Integer> queryDiaryByLabel(DatabaseHelper helper, Label label){
        try{
            Dao<DiaryLabel, Integer> diaryLabelDao = helper.getDiaryLabelDao();
            QueryBuilder<DiaryLabel, Integer> diaryLabelBuilder = diaryLabelDao.queryBuilder();
            diaryLabelBuilder.selectColumns(DiaryLabel.DIARY_TAG);

            Dao<Diary, Integer> diaryDao = helper.getDiaryDao();
            SelectArg labelSelectArg = new SelectArg();
            //设置条件语句（where label_id=?）
            diaryLabelBuilder.where().eq(DiaryLabel.LABEL_TAG, labelSelectArg);
            //创建外部查询日记表

            QueryBuilder<Diary, Integer> postQb = diaryDao.queryBuilder();
            //设置查询条件（where project_id in()）;
            postQb.where().in(Diary.TAG, diaryLabelBuilder);
            return postQb;
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static QueryBuilder<Label, String> queryLabelByDiary(DatabaseHelper helper, Diary diary){
        try{
            Dao<DiaryLabel, Integer> diaryLabelDao = helper.getDiaryLabelDao();
            QueryBuilder<DiaryLabel, Integer> diaryLabelBuilder = diaryLabelDao.queryBuilder();

            diaryLabelBuilder.selectColumns(DiaryLabel.LABEL_TAG);
            SelectArg selectArg = new SelectArg();
            //设置条件语句（where label_id=?）
            diaryLabelBuilder.where().eq(DiaryLabel.DIARY_TAG, selectArg);
            //创建外部查询日记表

            Dao<Label, Integer> labelDao = helper.getLabelDao();
            QueryBuilder<Label, Integer> labelQb = labelDao.queryBuilder();
            //设置查询条件（where project_id in()）;
            labelQb.where().in(Label.TAG, diaryLabelBuilder);
            return labelQb;
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
}
