package example.hearttrace;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wu-pc on 2018/5/10.
 */

@DatabaseTable(tableName = "diary_book")
public class Diarybook {

    private static final String TAG = "Diarybook";

    private static final String default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField
    private String diarybookName;

    public Diarybook(){};
    public Diarybook(String diarybookName){
        this.diarybookName = diarybookName;
    }

<<<<<<< HEAD
    public String getDiarybookName()
    {
=======
    public String getDiarybookName()
    {
>>>>>>> db-class
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
        }catch (SQLexception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void deteleSubDiary(DatabaseHelper helper) {
        try {
            Dao<Diary, Integer> dao = helper.getDiaryDao();

            dao.deleteBuilder().where().eq("Diarybook", diarybookName).delete();
        }catch (SQLexception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
}
