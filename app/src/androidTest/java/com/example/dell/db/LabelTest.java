package com.example.dell.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

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
        databaseHelper.clearAll();
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

        List<Label> labelList;
        labelList = diary.getAllLabel(databaseHelper);
        assertEquals(1, labelList.size());
        assertEquals(0, labelList.get(0).getStatus());
        Log.d(TAG, "testRelatedDeleteDiary: label modified = " + labelList.get(0).getModified());

        List<DiaryLabel> diaryLabelList = databaseHelper.getDaoAccess(DiaryLabel.class).queryForAll();
        assertEquals(1, diaryLabelList.size());
        assertEquals(0, diaryLabelList.get(0).getStatus());
        Log.d(TAG, "testRelatedDeleteDiary: diary label modified = " + diaryLabelList.get(0).getModified());

        label.delete(databaseHelper);

        labelList = diary.getAllLabel(databaseHelper);
        assertEquals(1, labelList.size());
        assertEquals(-1, labelList.get(0).getStatus());
        Log.d(TAG, "testRelatedDeleteDiary: label modified = " + labelList.get(0).getModified());

        diaryLabelList = databaseHelper.getDaoAccess(DiaryLabel.class).queryForAll();
        assertEquals(1, diaryLabelList.size());
        assertEquals(-1, diaryLabelList.get(0).getStatus());
        Log.d(TAG, "testRelatedDeleteDiary: diary label modified = " + diaryLabelList.get(0).getModified());
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

        List<Label> labelList;
        labelList = sentence.getAllLabel(databaseHelper);
        assertEquals(1, labelList.size());
        assertEquals(0, labelList.get(0).getStatus());
        Log.d(TAG, "testRelatedDeleteSentence: label modified = " + labelList.get(0).getModified());

        List<SentenceLabel> sentenceLabelList = databaseHelper.getDaoAccess(SentenceLabel.class).queryForAll();
        assertEquals(1, sentenceLabelList.size());
        assertEquals(0, sentenceLabelList.get(0).getStatus());
        Log.d(TAG, "testRelatedDeleteSentence: sentence label modified = " + sentenceLabelList.get(0).getModified());

        label.delete(databaseHelper);

        labelList = sentence.getAllLabel(databaseHelper);
        assertEquals(1, labelList.size());
        assertEquals(-1, labelList.get(0).getStatus());
        Log.d(TAG, "testRelatedDeleteSentence: label modified = " + labelList.get(0).getModified());

        sentenceLabelList = databaseHelper.getDaoAccess(SentenceLabel.class).queryForAll();
        assertEquals(1, sentenceLabelList.size());
        assertEquals(-1, sentenceLabelList.get(0).getStatus());
        Log.d(TAG, "testRelatedDeleteSentence: sentence label modified = " + sentenceLabelList.get(0).getModified());
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
            Log.e("test", "getDiaryLabel: ", e);
        }
    }

    @Test
    public void getSentenceLabel() {
        Sentence sentence = null;
        Label label = null;
        try{
            sentence = new Sentence("agoisdhadfagg");
            label = new Label("dahlavakggsd");
            sentence.insert(databaseHelper);
            label.insert(databaseHelper);
            sentence.insertLabel(databaseHelper, label);
            List<SentenceLabel> l2 = databaseHelper.getDao(SentenceLabel.class).queryForAll();
            for (SentenceLabel dl : l2) {
                Log.d("test", "onClick: dl, sentence" + dl.getSentence());
            }
        }
        catch(Exception e) {
            Log.e("test", "getSentenceLabel: ", e);
        }
    }
}
