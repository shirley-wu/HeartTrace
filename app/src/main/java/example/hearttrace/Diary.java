package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wu-pc on 2018/5/9.
 */

@DatabaseTable(tableName = "Diary")
public class Diary extends Record {
    public static final String TAG = "Diary";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true)
    private Diarybook diarybook;

    @DatabaseField
    string text;

    public Diary(){};
    public Diary(string text)
    {
        this.text = text;
    }

    public int getId()
    {
        return id;
    }

    public getText()
    {
        return text;
    }

    public setText(string text)
    {
        this.text = text;
    }
}
