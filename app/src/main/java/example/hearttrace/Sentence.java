package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by huang on 5/10/2018.
 */

@DatabaseTable(tableName = "Sentence")
public class Sentence extends Record {
    public static final String TAG = "Sentence";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true)
    private Sentencebook sentencebook;

    @DatabaseField(foreign = true)
    private Diary diary;

    @DatabaseField
    string text;

    public Sentence(){};
    public Sentence(string text)
    {
        this.text = text;
    }

    public int getId()
    {
        return id;
    }

    public string getText()
    {
        return text;
    }

    public string setText(string text)
    {
        this.text = text;
    }
}
