package example.hearttrace;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import java.nio.file.attribute.DosFileAttributes;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "Label")
public class Label {
    public static final String TAG = "label";
    @DatabaseField(generatedId = true, columnName = TAG)
    private Integer id;

    @DatabaseField(unique = true, columnName = "labelname")
    private String labelname;

    public Label(){}

    public Label(String labelname){
        this.labelname = labelname;
    }

    public String getLabelname(){
        return labelname;
    }

    public void setLabelname(String labelname){
        this.labelname = labelname;
    }

    public boolean insert(DatabaseHelper helper) { // TODO: cannot agree with setting string as pk by wxq
        try {
            Dao<Label, Integer> dao = helper.getLabelDao();
            Log.i("label", "dao = " + dao + "  label " + this);
            Dao.CreateOrUpdateStatus returnValue = dao.createOrUpdate(this); // TODO: cannot quite agree here. by wxq
            Log.i("label", "插入后返回值：" + returnValue.isCreated());
            return returnValue.isCreated();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Label> getAllLabel(DatabaseHelper helper){
        try {
            Dao<Label, Integer> dao = helper.getLabelDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static QueryBuilder<Label, Integer> makeQueryBuilderForDiary(DatabaseHelper helper, Diary diary){
        try{
            Dao<DiaryLabel, Integer> diaryLabelDao = helper.getDiaryLabelDao();
            Dao<Label, Integer> labelDao = helper.getLabelDao();
            QueryBuilder<DiaryLabel, Integer> diaryLabelBuilder = diaryLabelDao.queryBuilder();
            //diaryLabelBuilder.selectColumns(DiaryLabel.LABEL_TAG);
            //SelectArg diarySelectAry = new SelectArg();
            diaryLabelBuilder.where().eq(DiaryLabel.DIARY_TAG, diary);
            QueryBuilder<Label, Integer> labelQueryBuilder = labelDao.queryBuilder();
            labelQueryBuilder.where().in(Label.TAG, diaryLabelBuilder);
            return labelQueryBuilder;
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Label> lookupForDiary(DatabaseHelper helper, Diary diary){
        try{
            Dao<Label, Integer> labelDao = helper.getLabelDao();
            PreparedQuery<Label> labelForDiaryQuery = makeQueryBuilderForDiary(helper, diary).prepare();
            return labelDao.query(labelForDiaryQuery);
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

}
