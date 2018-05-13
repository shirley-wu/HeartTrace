package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by chen on 2018/5/13.
 */

@DatabaseTable(tableName = "sentence_book")
public class Sentencebook extends Recordbook {

    private static final string TAG = "Sentencebook";

    private static final string default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;
    @DatabaseField
    private string sentencebook_name;

    public Sentencebook(){};
    public Sentencebook(string sentencebook_name)
    {
        this.sentencebook_name = sentencebook_name;
    }

    public int getId()
    {
        return id;
    }

    public string getSentencebook_name()
    {
        return sentencebook_name;
    }

    public void setSentencebook_name(string sentencebook_name)
    {
        this.sentencebook_name = sentencebook_name;
    }
}