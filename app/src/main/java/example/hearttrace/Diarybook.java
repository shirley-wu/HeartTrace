package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wu-pc on 2018/5/10.
 */

@DatabaseTable(tableName = "diary_book")
public class Diarybook extends Recordbook {

    private static final String TAG = "Diarybook";

    private static final String default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;
    @DatabaseField
    private String diarybook_name;

    public Diarybook(){};
    public Diarybook(String diarybook_name)
    {
        this.diarybook_name = diarybook_name;
    }

    public int getId()
    {
        return id;
    }

    public String getDiarybook_name()
    {
        return diarybook_name;
    }

    public void setDiarybook_name(String diarybook_name)
    {
        this.diarybook_name = diarybook_name;
    }
}
