package example.hearttrace;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

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

import static org.junit.Assert.*;

/**
 * Created by wu-pc on 2018/5/10.
 */
public class DiaryInstrumentedTest {

    private DatabaseHelper databaseHelper;
    private Dao<Diary, Integer> dao;

    private String originText = "Testing testing do not repeat testing testing 221341151" + (new Date()).getTime();
    private String updateText = "hlelleelelfjakdl;jag alknals" + (new Date()).getTime();

    @Before
    public void setUp() throws SQLException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        dao = databaseHelper.getDiaryDao();
    }

    @After
    public void tearDown() {
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testGetDao() {
        assertEquals("Diary", dao.getTableName());
    }

    @Test
    public void testSaveAndGetDiary() throws SQLException {
        Diary diary = new Diary();
        List<Diary> diaryList;
        diary.setText(originText);
        diary.setDate();

        // create
        diary.insert(databaseHelper);

        // query
        diaryList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(1, diaryList.size()); // TODO: not safe: assumes that there is no such text by wxq
        assertEquals(diary.getDate(), diaryList.get(0).getDate());

        // update
        diary.setText(updateText);
        diary.update(databaseHelper);
        diaryList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(0, diaryList.size()); // TODO: not safe: assumes that there is no such text by wxq
        diaryList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(diary.getDate(), diaryList.get(0).getDate()); // TODO: not safe: assumes that there is no such text by wxq

        // delete
        diary.delete(databaseHelper);
        diaryList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(0, diaryList.size()); // TODO: not safe: assumes that there is no such text by wxq
    }

    @Test
    public void testGetAllDiary() throws SQLException {
        List<Diary> diaryList = Diary.getAll(databaseHelper);
        assertTrue(diaryList.size() >= 0);
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

    // @Test
    void testCountByDateLabel() throws SQLException {
        Random random = new Random();

        int num = 20;
        List<Diary> diaryList = new ArrayList();

        Label label1 = new Label();
        label1.setLabelname("hi" + random.nextInt());
        label1.insertLabel(databaseHelper);

        Label label2 = new Label();
        label2.setLabelname("hi2" + random.nextInt());
        label2.insertLabel(databaseHelper);

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
        assertEquals(c1, Diary.countByDateLabel(databaseHelper, beginDate, endDate, label1));
    }

}