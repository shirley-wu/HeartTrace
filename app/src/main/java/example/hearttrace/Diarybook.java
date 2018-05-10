package example.hearttrace;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wu-pc on 2018/5/10.
 */

@DatabaseTable(tableName = "diary_book")
public class Diarybook extends Recordbook {

    private static final String TAG = "Diarybook";

    private static final String default_name = "default";

    /*public static Diarybook getDefault(DatabaseHelper databaseHelper){
        try{
            Dao<Diarybook, Integer> dao = databaseHelper.getDiarybookDao();
            List<Diarybook> listDiarySet = dao.queryBuilder().where().eq("name", default_name).query();

            Diarybook diarybook;
            if(listDiarySet.size() == 0){
                diarybook = new Diarybook();
                diarybook.setName(default_name);
                dao.create(diarybook);
                Log.d("Diarybook", "getDefault: " + diarybook.id);
            }
            else{
                if(listDiarySet.size() > 1){
                    Log.e(TAG, "getDefault: have " + listDiarySet.size() + " default book, error!!!!!");
                }
                diarybook = listDiarySet.get(0);
            }
            return diarybook;
        } catch(SQLException e){
            return null;
        }
    }*/

}
