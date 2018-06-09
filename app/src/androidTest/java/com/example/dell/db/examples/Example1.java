package com.example.dell.db.examples;

import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Sentence;
import com.example.dell.db.Sentencebook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wu-pc on 2018/6/8.
 */

public class Example1 extends InstrumentationTestCase {

    private final static String TAG = "Example1";

    private String bookName = "hihihi";

    private DatabaseHelper helper = null;

    private Sentence sentence = null;

    @Before
    public void setUp() {
        helper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void tearDown() {
        if(sentence != null) {
            sentence.delete(helper);
        }
        helper.close();
    }

    @Test
    public void testExample0() throws SQLException {
        Sentencebook b = Sentencebook.getByName(helper, bookName);
        if(b == null) {
            b = new Sentencebook(bookName);
            b.insert(helper);
        }

        sentence = new Sentence("adfjanvnavop");
        sentence.setDate();
        sentence.setSentencebook(b);
        sentence.insert(helper);

        List<Sentence> l = b.getAllSubSentence(helper);

        Boolean exist = false;
        for(Sentence s : l) {
            if(s.getDate().equals(sentence.getDate()) && s.getText().equals(sentence.getText())) {
                exist = true;
                break;
            }
        }

        assertTrue(exist);
    }

}
