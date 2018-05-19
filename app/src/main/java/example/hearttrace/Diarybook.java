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
 * Created by wu-pc on 2018/5/10.
 */

@DatabaseTable(tableName = "diarybook")
public class Diarybook {

    private static final String TAG = "Diarybook";

    private static final String default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(columnName = "diarybookname")
    private String diarybookName;

    public Diarybook(){};
    public Diarybook(String diarybookName){
        this.diarybookName = diarybookName;
    }

    public String getDiarybookName() {
        return diarybookName;
    }

    public void setDiarybookName(String diarybookName){
        this.diarybookName = diarybookName;
    }

    public List<Diary> getAllSubDiary(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();

            List<Diary> subDiaryList = dao.queryBuilder().where().eq("Diarybook", diarybookName).query();

            return subDiaryList;
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void deteleSubDiary(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();

            // dao.deleteBuilder().where().eq("Diarybook", diarybookName).delete();
            // TODO: Cannot compile
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }


}
