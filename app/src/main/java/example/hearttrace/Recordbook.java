package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by wu-pc on 2018/5/10.
 */

public class Recordbook {

    @DatabaseField(generatedId = true)
    Integer id;

    @DatabaseField
    String name;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
