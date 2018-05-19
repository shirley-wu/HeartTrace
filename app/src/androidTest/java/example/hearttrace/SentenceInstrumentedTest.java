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
public class SentenceInstrumentedTest {

    final static String TAG = "SentenceInstrumentedTest";

    private DatabaseHelper databaseHelper;
    private Dao<Sentence, Integer> dao;

    private String originText;
    private String updateText;

    @Before
    public void setUp() throws SQLException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        dao = databaseHelper.getSentenceDao();
    }

    @After
    public void tearDown() {
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testGetDao() {
        assertEquals("Sentence", dao.getTableName());
    }

    @Test
    public void testSaveAndGetSentence() throws SQLException {
        originText = "Testing testing do not repeat testing testing 221341151" + (new Date()).getTime() + (new Random()).nextDouble();
        updateText = "hlelleelelfjakdl;jag alknals" + (new Date()).getTime() + (new Random()).nextDouble();

        Sentence sentence = new Sentence();
        List<Sentence> sentenceList;
        sentence.setText(originText);
        sentence.setDate();

        // create
        sentence.insert(databaseHelper);

        // query
        sentenceList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(1, sentenceList.size()); // TODO: not safe: assumes that there is no such text by wxq
        assertEquals(sentence.getDate(), sentenceList.get(0).getDate());

        // update
        sentence.setText(updateText);
        sentence.update(databaseHelper);
        sentenceList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(0, sentenceList.size()); // TODO: not safe: assumes that there is no such text by wxq
        sentenceList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(sentence.getDate(), sentenceList.get(0).getDate()); // TODO: not safe: assumes that there is no such text by wxq

        // delete
        sentence.delete(databaseHelper);
        sentenceList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(0, sentenceList.size()); // TODO: not safe: assumes that there is no such text by wxq
    }

    @Test
    public void testGetAllSentence() throws SQLException {
        List<Sentence> sentenceList = Sentence.getAll(databaseHelper);
        assertTrue(sentenceList.size() >= 0);
    }

}