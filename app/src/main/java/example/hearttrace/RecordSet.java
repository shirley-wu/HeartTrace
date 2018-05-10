package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by wu-pc on 2018/5/10.
 */

public class RecordSet {

    @DatabaseField(generatedId = true)
    Integer id;

    @DatabaseField
    String name;

}
