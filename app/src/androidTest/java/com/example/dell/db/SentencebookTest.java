package com.example.dell.db;

import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        sentencebook.setDescription("hihihihi");
        sentencebook.insert(helper);
        sentence.setDate();
        sentence.setSentencebook(sentencebook);
        sentence.insert(helper);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        sentencebook.delete(helper);
        sentence.delete(helper);
        helper.close();
    }

    @Test
    public void testGetByName() {
        Sentencebook b;
        b = Sentencebook.getByName(helper, "null");
        assertEquals(null, b);
        b = Sentencebook.getByName(helper, sentencebook.getSentencebookName());
        assertEquals(sentencebook.getSentencebookName(), b.getSentencebookName());
        assertEquals(sentencebook.getDescription(), b.getDescription());
    }
    
    @Test
    public void testInsertSentence() {
        assertEquals(1, sentencebook.getAllSubSentence(helper).size());
        assertEquals(sentence.getText(), sentencebook.getAllSubSentence(helper).get(0).getText());
    }
}
