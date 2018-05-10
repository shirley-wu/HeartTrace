package example.hearttrace;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by wu-pc on 2018/5/10.
 */
public class DiaryTest {

    @Test
    public void testText(){
        Diary diary = new Diary();
        String text = "dsakfjslbvajelbkajcfanw;ewae\ncrgjbcwemcfjgblascwm]qwtvbq9]";
        diary.setText(text);
        assertEquals(text, diary.getText());
    }

    @Test
    public void testDate(){
        Diary diary = new Diary();
        diary.setDate();
        Date date = diary.getDate();
        for(int i = 0; i < 1000; i++){
            for(int j = 0; j < 1000; j++){
                for(int k = 0; k < 1000; k++){
                    for(int l = 0; l < 1000; l++) ;
                }
            }
        }
        diary.setDate();
        assertEquals(date, diary.getDate());
    }

}