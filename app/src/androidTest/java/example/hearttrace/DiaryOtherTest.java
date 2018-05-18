package example.hearttrace;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by wu-pc on 2018/5/19.
 */

public class DiaryOtherTest {

    private static final String TAG = "DiaryGetByRestrictTest";

    private DatabaseHelper databaseHelper;
    private Dao<Diary, Integer> dao;

    private String originText;
    private String updateText;

    @Before
    public void setUp() throws SQLException {
        Log.d(TAG, "setUp");
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        dao = databaseHelper.getDiaryDao();
    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown");
        OpenHelperManager.releaseHelper();
    }


    @Test
    public void testGetDiaryByDate() throws SQLException {
        int num = 20;
        List<Diary> diaryList = new ArrayList();
        for(int i = 1; i <= num; i++) {
            for(int j = 0; j < i; j++) {
                Diary diary = new Diary();
                diaryList.add(diary);
                diary.setText(originText + j);
                diary.setDate(new Date(1998, 8, i));
                dao.create(diary);
            }
        }

        Date date;
        for(int i = 1; i <= num; i++) {
            assertEquals(i, Diary.getByDate(databaseHelper, new Date(1998, 8, i)).size());
        }

        for(final Diary diary : diaryList) {
            diary.delete(databaseHelper);
        }
    }

    @Test
    public void testCountByDateLabel() throws SQLException {
        Random random = new Random();

        int num = 20;
        List<Diary> diaryList = new ArrayList();

        Label label1 = new Label();
        label1.setLabelname("hi" + random.nextInt());
        label1.insert(databaseHelper);

        Label label2 = new Label();
        label2.setLabelname("hi2" + random.nextInt());
        label2.insert(databaseHelper);

        int c1 = 0;

        int begin = 5, end = 15;

        for(int i = 1; i <= num; i++) {
            for(int j = 0; j < i; j++) {
                Diary diary = new Diary();
                diaryList.add(diary);
                diary.setText(originText + j);
                diary.setDate(new Date(1998, 8, i));
                dao.create(diary);
                if(i % 3 == 0) {
                    diary.insertLabel(databaseHelper, label1);
                    if(begin <= i && i <= end){
                        c1++;
                    }
                }
                if(random.nextInt() % 2 == 0) {
                    diary.insertLabel(databaseHelper, label2);
                }
            }
        }

        Date beginDate = new Date(1998, 8, begin), endDate = new Date(1998, 8, end);
        List<Label> labelList = new ArrayList();
        labelList.add(label1);
        assertEquals(c1, Diary.countByDateLabel(databaseHelper, beginDate, endDate, labelList));
    }

    @Test
    public void testGetAllLabel() throws SQLException {
        Diary diary = new Diary("hello");
        diary.insert(databaseHelper);

        List<Label> labels = new ArrayList();
        labels.add(new Label("label1sadfvabe"));
        labels.add(new Label("janvp"));
        labels.add(new Label("fancevbotvptjvo"));

        for(final Label label : labels) {
            label.insert(databaseHelper);
            diary.insertLabel(databaseHelper, label);
        }

        List<Label> labels2 = diary.getAllLabel(databaseHelper);
        assertEquals(3, labels2.size());
        for(final Label label : labels2) {
            Log.d(TAG, "testGetAllLabel: label " + label.getLabelname());
        }

        for(final Label label : labels) {
            label.delete(databaseHelper);
        }
        diary.delete(databaseHelper);
    }
}
