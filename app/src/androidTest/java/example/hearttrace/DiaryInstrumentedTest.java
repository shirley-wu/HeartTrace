package example.hearttrace;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

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

    final static String TAG = "DiaryInstrumentedTest";

    private DatabaseHelper databaseHelper;
    private Dao<Diary, Integer> dao;

    private String originText;
    private String updateText;

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
        originText = "Testing testing do not repeat testing testing 221341151" + (new Date()).getTime() + (new Random()).nextDouble();
        updateText = "hlelleelelfjakdl;jag alknals" + (new Date()).getTime() + (new Random()).nextDouble();

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

}