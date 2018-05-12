package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "Label")
public class Label {
    public static final String TAG = "label";
    @DatabaseField(id = true, columnName = TAG)
    private String labelname;
    public Label(){}
    public Label(String labelname){
        this.labelname = labelname;
    }
    public String getLabelname(){
        return labelname;
    }
    public void setLabelname(String labelname){
        this.labelname = labelname;
    }
}
