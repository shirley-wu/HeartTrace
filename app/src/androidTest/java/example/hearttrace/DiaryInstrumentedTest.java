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

import static org.junit.Assert.*;

/**
 * Created by wu-pc on 2018/5/10.
 */
public class DiaryInstrumentedTest {

    private DatabaseHelper databaseHelper;
    private Dao<Diary, Integer> dao;

    private String originText = "Testing testing do not repeat testing testing 221341151";
    private String updateText = "hlelleelelfjakdl;jag alknals";

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
        databaseHelper.insertDiary(diary);

        // query
        diaryList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(1, diaryList.size()); // TODO: not safe: assumes that there is no such text
        assertEquals(diary.getDate(), diaryList.get(0).getDate());

        // update
        diary.setText(updateText);
        databaseHelper.updateDiary(diary);
        diaryList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(0, diaryList.size()); // TODO: not safe: assumes that there is no such text
        diaryList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(diary.getDate(), diaryList.get(0).getDate()); // TODO: not safe: assumes that there is no such text

        // delete
        databaseHelper.deleteDiary(diary);
        diaryList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(0, diaryList.size()); // TODO: not safe: assumes that there is no such text
    }

    @Test
    public void testGetAllDiary() throws SQLException {
        List<Diary> diaryList = databaseHelper.getAllDiary();
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
            assertEquals(i, databaseHelper.getDiaryByDate(new Date(1998, 8, i)).size());
        }

        for(final Diary diary : diaryList) {
            databaseHelper.deleteDiary(diary);
        }
    }

}