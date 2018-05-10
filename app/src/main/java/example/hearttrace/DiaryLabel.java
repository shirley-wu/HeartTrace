package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.concurrent.Callable;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "tb_diary_label")
public class DiaryLabel {
    @DatabaseField(id = true, generatedId = true)
    private int id;
    @DatabaseField(foreign = true)
    private Diary diary;
    @DatabaseField(foreign = true)
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
