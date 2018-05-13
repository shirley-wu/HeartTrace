package example.hearttrace;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
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
        dao.create(diary);

        // query
        diaryList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(1, diaryList.size()); // TODO: not safe: assumes that there is no such text
        assertEquals(diary.getDate(), diaryList.get(0).getDate());

        // update
        diary.setText(updateText);
        dao.update(diary);
        diaryList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(0, diaryList.size()); // TODO: not safe: assumes that there is no such text
        diaryList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(diary.getDate(), diaryList.get(0).getDate()); // TODO: not safe: assumes that there is no such text

        // delete
        dao.delete(diary);
        diaryList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(0, diaryList.size()); // TODO: not safe: assumes that there is no such text
    }

}