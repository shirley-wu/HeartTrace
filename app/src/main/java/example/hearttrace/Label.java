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
    @DatabaseField(id = true, columnName = TAG)
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

    public boolean insertLabel(DatabaseHelper helper, Label label) {
        try {
            Dao<Label, String> dao = helper.getLabelDao();
            Log.i("label", "dao = " + dao + "  label " + label);
            Dao.CreateOrUpdateStatus returnValue = dao.createOrUpdate(label);
            Log.i("label", "插入后返回值：" + returnValue.isCreated());
            return returnValue.isCreated();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Label> getAllLabel(DatabaseHelper helper){
        try {
            Dao<Label, String> dao = helper.getLabelDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static PreparedQuery<Label> makePostsForDiaryQuery(DatabaseHelper helper){
        try{
            Dao<DiaryLabel, Integer> diaryLabelDao = helper.getDiaryLabelDao();
            Dao<Label, String> labelDao = helper.getLabelDao();
            QueryBuilder<DiaryLabel, Integer> diaryLabelBuilder = diaryLabelDao.queryBuilder();
            diaryLabelBuilder.selectColumns(DiaryLabel.LABEL_TAG);
            SelectArg diarySelectAry = new SelectArg();
            diaryLabelBuilder.where().eq(DiaryLabel.DIARY_TAG, diarySelectAry);
            QueryBuilder<Label, String> postQb = labelDao.queryBuilder();
            postQb.where().in(Label.TAG, diaryLabelBuilder);
            return postQb.prepare();
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Label> lookupForDiary(DatabaseHelper helper, Diary diary){
        try{
            Dao<Label, String> labelDao = helper.getLabelDao();
            PreparedQuery<Label> labelForDiaryQuery = makePostsForDiaryQuery(helper);
            labelForDiaryQuery.setArgumentHolderValue(0, diary);
            return labelDao.query(labelForDiaryQuery);
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }


}
