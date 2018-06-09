package com.example.dell.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
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

    private Sentencebook sentencebook = new Sentencebook("fajskdlav");

    private String originText;
    private String updateText;

    @Before
    public void setUp() throws SQLException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        dao = databaseHelper.getSentenceDao();
        sentencebook.insert(databaseHelper);
    }

    @After
    public void tearDown() {
        sentencebook.delete(databaseHelper);
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
        sentence.setSentencebook(sentencebook);

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
        List<Sentence> sentenceList = Sentence.getAll(databaseHelper, true);
        assertTrue(sentenceList.size() >= 0);
    }

    @Test
    public void testGetAllDescendingSentence() throws SQLException {
        String text = "akkljdvfavba";

        Sentence sentence = new Sentence(text);
        sentence.setDate(new Date(4000, 1, 1));
        sentence.setSentencebook(sentencebook);
        sentence.insert(databaseHelper);

        List<Sentence> sentenceList = Sentence.getAll(databaseHelper, false);
        assertTrue(sentenceList.size() > 0);
        assertEquals(sentence.getDate(), sentenceList.get(0).getDate());

        sentence.delete(databaseHelper);
    }

    @Test
    public void testGetAllAscendingSentence() throws SQLException {
        String text = "akkljdvfavba";

        Sentence sentence = new Sentence(text);
        sentence.setDate(new Date(1000, 1, 1));
        sentence.setSentencebook(sentencebook);
        sentence.insert(databaseHelper);

        List<Sentence> sentenceList = Sentence.getAll(databaseHelper, true);
        assertTrue(sentenceList.size() > 0);
        assertEquals(sentenceList.get(0).getDate(), sentence.getDate());

        sentence.delete(databaseHelper);
    }

}