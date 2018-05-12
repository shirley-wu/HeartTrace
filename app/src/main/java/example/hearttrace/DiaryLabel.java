package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.concurrent.Callable;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "tb_diary_label")
public class DiaryLabel {
    public static final String TAG = "tb_diary_label";

    public static final String DIARY_TAG = "tb_diary";

    public static final String LABEL_TAG = "tb_label";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;
    @DatabaseField(foreign = true, columnName = DIARY_TAG)
    private Diary diary;
    @DatabaseField(foreign = true, columnName = LABEL_TAG)
    private Label label;
    public DiaryLabel(){}
    public DiaryLabel(Diary diary, Label label){
        this.diary = diary;
        this.label = label;
    }
    public int getId(){
        return id;
    }
    public Diary getDiary(){
        return diary;
    }
    public Label getLabel(){
        return label;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setDiary(Diary diary){
        this.diary = diary;
    }
    public void setLabel(Label label){
        this.label = label;
    }

}
