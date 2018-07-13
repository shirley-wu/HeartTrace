package com.example.dell.db;

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
import java.util.List;

/**
 * Created by huang on 5/17/2018.
 */

public class LabelTest extends InstrumentationTestCase {
    private static final String TAG = "test";

    private DatabaseHelper databaseHelper;

    private Diarybook diarybook = new Diarybook("agbap");
    private Sentencebook sentencebook = new Sentencebook("javbaove");

    @Before
    public void setUp() throws SQLException {
        Log.d(TAG, "setUp");
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        diarybook.insert(databaseHelper);
        sentencebook.insert(databaseHelper);
    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown");
        diarybook.delete(databaseHelper);
        sentencebook.delete(databaseHelper);
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testRelatedDeleteDiary() throws SQLException {
        Label label = new Label("sad");
        label.insert(databaseHelper);

        Diary diary = new Diary("hhh");
        diary.setDate();
        diary.setDiarybook(diarybook);
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
        sentence.setDate();
        sentence.setSentencebook(sentencebook);
        sentence.insert(databaseHelper);
        sentence.insertLabel(databaseHelper, label);

        assertEquals(1, sentence.getAllLabel(databaseHelper).size());
        label.delete(databaseHelper);
        assertEquals(0, sentence.getAllLabel(databaseHelper).size());

        sentence.delete(databaseHelper);
    }

    @Test
    public void getDiaryLabel() {
        Diary diary = null;
        Label label = null;
        try{
            diary = new Diary("agoisdhadfagg");
            label = new Label("dahlavakggsd");
            diary.insert(databaseHelper);
            label.insert(databaseHelper);
            diary.insertLabel(databaseHelper, label);
            List<DiaryLabel> l2 = databaseHelper.getDao(DiaryLabel.class).queryForAll();
            for (DiaryLabel dl : l2) {
                Log.d("test", "onClick: dl, diary" + dl.getDiary());
            }
        }
        catch(Exception e) {
            Log.e("test", "onClick: ", e);
        }
        finally {
            diary.delete(databaseHelper);
            label.delete(databaseHelper);
        }
    }
}
