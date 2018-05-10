package example.hearttrace;

import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wu-pc on 2018/5/9.
 */

public abstract class Record {

    private final String TAG = "Record";

    @DatabaseField(generatedId = true)
    protected Integer id;

    @DatabaseField
    protected String text;

    @DatabaseField(dataType = DataType.DATE_STRING)
    protected Date date;

    public Record() {
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setDate(){
        if(date == null) {
            date = new Date();
        }
    }

    public Date getDate(){
        return date;
    }

}
