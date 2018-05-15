package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by chen on 2018/5/13.
 */

@DatabaseTable(tableName = "sentence_book")
public class Sentencebook {

    private static final String TAG = "Sentencebook";

    private static final String default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;
    @DatabaseField
    private String sentencebook_name;

    public Sentencebook(){};
    public Sentencebook(String sentencebook_name)
    {
        this.sentencebook_name = sentencebook_name;
    }

    public int getId()
    {
        return id;
    }

    public String getSentencebook_name()
    {
        return sentencebook_name;
    }

    public void setSentencebook_name(String sentencebook_name)
    {
        this.sentencebook_name = sentencebook_name;
    }
}