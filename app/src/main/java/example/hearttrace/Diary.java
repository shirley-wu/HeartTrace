package example.hearttrace;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by wu-pc on 2018/5/9.
 */

@DatabaseTable(tableName = "Diary")
public class Diary {
    public static final String TAG = "diary";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true)
    private Diarybook diarybook;

    @DatabaseField
    String text;

<<<<<<< HEAD
    @DatabaseField(dataType = DataType.DATE_STRING)
    protected Date date;

    public Diary(){
    };
=======
    public Diary(){};
    public Diary(String text)
    {
        this.text = text;
    }
>>>>>>> 2d7a08f3c2b2e7baf9184fb3f316a488d7b92f38

    public Diary(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

<<<<<<< HEAD
    public void setText(String text){
=======
    public setText(String text)
    {
>>>>>>> 2d7a08f3c2b2e7baf9184fb3f316a488d7b92f38
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

    public Diarybook getDiarybook() {
        return diarybook;
    }

    public void setDiarybook(Diarybook diarybook) {
        this.diarybook = diarybook;
    }
}
