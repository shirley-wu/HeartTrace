package example.hearttrace;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by huang on 5/10/2018.
 */

@DatabaseTable(tableName = "Sentence")
public class Sentence {
    public static final String TAG = "Sentence";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true)
    private Sentencebook sentencebook;

    @DatabaseField
    String text;

    @DatabaseField(dataType = DataType.DATE_STRING)
    protected Date date;

    @DatabaseField(foreign = true, canBeNull = true)
    private Diary diary;

<<<<<<< HEAD
    public Sentence(){
    };

    public Sentence(String text){
        this.text = text;
    }

    public String getText(){
=======
    @DatabaseField
    String text;

    public Sentence(){};
    public Sentence(String text)
    {
        this.text = text;
    }

    public int getId()
    {
        return id;
    }

    public String getText()
    {
>>>>>>> 2d7a08f3c2b2e7baf9184fb3f316a488d7b92f38
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(){
        if(date == null) {
            date = new Date();
        }
    }

    public Sentencebook getSentencebook() {
        return sentencebook;
    }

    public void setSentencebook(Sentencebook sentencebook) {
        this.sentencebook = sentencebook;
    }
}
