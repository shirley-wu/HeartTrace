package example.hearttrace;

import android.content.Context;
import android.provider.Telephony;
import android.support.test.InstrumentationRegistry;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wu-pc on 2018/5/13.
 */
public class SentenceInstrumentedTest {

    private DatabaseHelper databaseHelper;
    private Dao<Sentence, Integer> dao;

    private String originText = "Testing testing do not repeat testing testing dfankjvaiwbrpwcepwuvqwiovb";
    private String updateText = "asevrnawbcpamuoiavwebvwaprbwavup";

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
    public void testSaveAndGetDiary() throws SQLException {
        Sentence sentence = new Sentence();
        List<Sentence> sentenceList;
        sentence.setText(originText);
        sentence.setDate();

        // create
        dao.create(sentence);

        // query
        sentenceList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(1, sentenceList.size()); // TODO: not safe: assumes that there is no such text
        assertEquals(sentence.getDate(), sentenceList.get(0).getDate());

        // update
        sentence.setText(updateText);
        dao.update(sentence);
        sentenceList = dao.queryBuilder().where().eq("text", originText).query();
        assertEquals(0, sentenceList.size()); // TODO: not safe: assumes that there is no such text
        sentenceList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(sentence.getDate(), sentenceList.get(0).getDate()); // TODO: not safe: assumes that there is no such text

        // delete
        dao.delete(sentence);
        sentenceList = dao.queryBuilder().where().eq("text", updateText).query();
        assertEquals(0, sentenceList.size()); // TODO: not safe: assumes that there is no such text
    }

}