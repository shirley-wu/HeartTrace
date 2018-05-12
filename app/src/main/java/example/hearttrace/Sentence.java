package example.hearttrace;


import com.j256.ormlite.field.DatabaseField;

/**
 * Created by huang on 5/10/2018.
 */

public class Sentence extends Record {
    @DatabaseField(generatedId = true, columnName = "TAG")
    private int id;
}
