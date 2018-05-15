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
    private String sentencebookName;

    public Sentencebook(){};
    public Sentencebook(String sentencebookName)
    {
        this.sentencebookName = sentencebookName;
    }

    public int getId()
    {
        return id;
    }

    public String getSentencebookName()
    {
        return sentencebookName;
    }

    public void setSentencebookName(String sentencebookName)
    {
        this.sentencebookName = sentencebookName;
    }
}