package example.hearttrace;

import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;

import example.hearttrace.Record;

/**
 * Created by wu-pc on 2018/5/9.
 */

@DatabaseTable(tableName = "diary")
public class Diary extends Record {

}
