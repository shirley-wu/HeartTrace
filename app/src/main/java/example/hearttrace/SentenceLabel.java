package example.hearttrace;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by huang on 5/10/2018.
 */
@DatabaseTable(tableName = "tb_sentence_label")
public class SentenceLabel {
    @DatabaseField(id = true, generatedId = true)
    private int id ;
    @DatabaseField
    private Sentence sentence;
    @DatabaseField
    private Label label;
    public SentenceLabel(){}
    public SentenceLabel(Sentence sentence, Label label){
        this.sentence = sentence;
        this.label = label;
    }
    public int getId(){
        return id;
    }
    public Sentence getSentence(){
        return sentence;
    }
    public Label getLabel(){
        return label;
    }
    public void setSentence(Sentence sentence){
        this.sentence = sentence;
    }
    public void setLabel(Label label){
        this.label = label;
    }
}
