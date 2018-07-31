package com.example.dell.db;

import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;

import com.j256.ormlite.dao.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by wu-pc on 2018/6/8.
 */

public class SentencebookTest extends InstrumentationTestCase {

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
    public void delete() {
        assertEquals(0, sentencebook.getStatus());
        sentencebook.delete(helper);
        assertEquals(-1, sentencebook.getStatus());
        List<Sentence> list = sentencebook.getAllSubSentence(helper);
        for(Sentence d : list) {
            assertEquals(-1, d.getStatus());
        }
    }

    @Test
    public void update() {
        assertEquals(0, sentencebook.getStatus());
        sentencebook.update(helper);
        assertEquals(0, sentencebook.getStatus());
        sentencebook.setStatus(9);
        sentencebook.update(helper);
        assertEquals(1, sentencebook.getStatus());
    }

}
