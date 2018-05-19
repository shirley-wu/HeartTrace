package example.hearttrace;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import junit.framework.TestResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by huang on 5/17/2018.
 */

public class LabelTest extends InstrumentationTestCase {
    private static final String TAG = "test";


    private DatabaseHelper databaseHelper;
    private Dao<Label, Integer> dao;

    @Before
    public void setUp() throws SQLException {
        Log.d(TAG, "setUp");
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        dao = databaseHelper.getLabelDao();
    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown");
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testRelatedDeleteDiary() throws SQLException {
        Label label = new Label("sad");
        label.insert(databaseHelper);

        Diary diary = new Diary("hhh");
        diary.insert(databaseHelper);
        diary.insertLabel(databaseHelper, label);

        assertEquals(1, diary.getAllLabel(databaseHelper).size());
        label.delete(databaseHelper);
        assertEquals(0, diary.getAllLabel(databaseHelper).size());

        diary.delete(databaseHelper);
    }

    @Test
    public void testRelatedDeleteSentence() throws SQLException {
        Label label = new Label("sad");
        label.insert(databaseHelper);

        Sentence sentence = new Sentence("hhh");
        sentence.insert(databaseHelper);
        sentence.insertLabel(databaseHelper, label);

        assertEquals(1, sentence.getAllLabel(databaseHelper).size());
        label.delete(databaseHelper);
        assertEquals(0, sentence.getAllLabel(databaseHelper).size());

        sentence.delete(databaseHelper);
    }
}
