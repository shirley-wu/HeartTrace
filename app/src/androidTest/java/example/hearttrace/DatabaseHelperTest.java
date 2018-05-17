package example.hearttrace;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wu-pc on 2018/5/10.
 */
public class DatabaseHelperTest {

    private DatabaseHelper helper;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        helper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
    }

    @After
    public void tearDown() {
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testGetDatabaseHelper() {
        // TODO: 这些测试不要放在这里。
        Diary diary = new Diary();
        diary.setText("test1");
        diary.setDate();
        diary.insert(helper);
        Label label = new Label("happy");
        Label label1 = new Label("sad");
        Label label2 = new Label("normal");
        Log.i(Diary.TAG, "this is the hot point");
        try {
            List<Label> labelNewDiary = Label.lookupForDiary(helper, diary);
            List<Diary> diaryHappy = Diary.lookupForLabel(helper,label1);
            List<Label> labelList = Label.getAllLabel(helper);
            List<Diary> diaryList = Diary.getAllDiary(helper);
            for(Diary i :diaryHappy){
                Log.i(Diary.TAG, i.getDate()+i.getText());
            }
            Log.i(Diary.TAG, "=====================================");
            for(Label i : labelNewDiary){
                Log.i(Label.TAG, i.getLabelname());
            }
            Log.i(Diary.TAG, "=====================================");
            for(Diary i : diaryList ){
                Log.i(Diary.TAG, i.getDate()+i.getText());
            }
            Log.i(Diary.TAG, "=====================================");
            for(Label i : labelList){
                Log.i(Label.TAG, i.getLabelname());
            }
            Log.i(Label.TAG, "=====================================");
            helper.close();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

}
