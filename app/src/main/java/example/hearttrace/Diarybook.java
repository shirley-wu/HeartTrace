package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
    public Diarybook(String diarybookNname)
    {
        this.diarybookName = diarybookName;
    }

    public int getId()
    {
        return id;
    }

    public String getDiarybookName()
    {
        return diarybookName;
    }

    public void setDiarybookName(String diarybookName)
    {
        this.diarybookName = diarybookName;
    }
}
