package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wu-pc on 2018/5/9.
 */

@DatabaseTable(tableName = "diary")
public class Diary extends Record {

    @DatabaseField(foreign = true)
    DiarySet diarySet;

}
