package com.example.dell.db;

import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by wu-pc on 2018/6/8.
 */

public class SentencebookTest extends InstrumentationTestCase {

    final static private String TAG = "SentencebookTest";

    private Sentencebook sentencebook = new Sentencebook("adsfavva");

    private Sentence sentence = new Sentence("fdaov");

    private DatabaseHelper helper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());

    @Before
    public void setUp() throws Exception {
        super.setUp();
        sentencebook.insert(helper);
        sentence.setDate();
        sentence.setSentencebook(sentencebook);
        sentence.insert(helper);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        helper.clearAll();
        helper.close();
    }

    @Test
    public void testGetByName() {
        Sentencebook b;
        b = Sentencebook.getByName(helper, "null");
        assertEquals(null, b);
        b = Sentencebook.getByName(helper, sentencebook.getSentencebookName());
        assertEquals(sentencebook.getSentencebookName(), b.getSentencebookName());
    }

    @Test
    public void testInsertSentence() {
        assertEquals(1, sentencebook.getAllSubSentence(helper).size());
        List<Sentence> list = sentencebook.getAllSubSentence(helper);
        assertEquals(sentence.getText(), list.get(0).getText());
        Sentencebook b = list.get(0).getSentencebook();
        b.refresh(helper);
        assertEquals(sentencebook.getSentencebookName(), b.getSentencebookName());
    }

    @Test
    public void testDelete() {
        assertEquals(0, sentencebook.getStatus());
        Log.d(TAG, "delete: modified before delete " + sentencebook.getModified());

        List<Sentence> list = sentencebook.getAllSubSentence(helper);
        for(Sentence d : list) {
            assertEquals(0, d.getStatus());
            Log.d(TAG, "delete: sentence modified before delete " + d.getModified());
        }

        sentencebook.delete(helper);
        assertEquals(-1, sentencebook.getStatus());
        Log.d(TAG, "delete: modified after delete " + sentencebook.getModified());

        list = sentencebook.getAllSubSentence(helper);
        /*for(Sentence d : list) {
            assertEquals(-1, d.getStatus());
            Log.d(TAG, "delete: sentence modified after delete " + d.getModified());
        }*/
        assertEquals(0, list.size());
        list = Sentence.getAll(helper, false);
        assertEquals(0, list.size());
    }

    @Test
    public void testUpdate() {
        assertEquals(0, sentencebook.getStatus());
        Log.d(TAG, "update: modified before update " + sentencebook.getModified());
        sentencebook.update(helper);
        assertEquals(0, sentencebook.getStatus());
        Log.d(TAG, "update: modified after update " + sentencebook.getModified());
        sentencebook.setStatus(9);
        sentencebook.update(helper);
        assertEquals(1, sentencebook.getStatus());
        Log.d(TAG, "update: modified after update 2 " + sentencebook.getModified());
    }

}
