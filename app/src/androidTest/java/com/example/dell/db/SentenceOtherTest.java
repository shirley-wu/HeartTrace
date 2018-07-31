package com.example.dell.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by wu-pc on 2018/5/19.
 */

public class SentenceOtherTest {

    private static final String TAG = "SentenceGetByRestrictTest";

    private DatabaseHelper databaseHelper;

    private String originText;
    private String updateText;

    Sentencebook sentencebook = new Sentencebook("jkbivdtcvnn");

    @Before
    public void setUp() throws SQLException {
        Log.d(TAG, "setUp");
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        sentencebook.insert(databaseHelper);
    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown");
        sentencebook.delete(databaseHelper);
        OpenHelperManager.releaseHelper();
    }


    @Test
    public void testGetSentenceByDateEndOfMonth() throws SQLException {
        Sentence sentence = new Sentence();

        sentence.setText(originText);

        Calendar calendar = Calendar.getInstance();
        calendar.set(1998, 3 - 1, 31, 23, 59);
        sentence.setDate(calendar.getTime());
        sentence.setSentencebook(sentencebook);
        sentence.insert(databaseHelper);

        List<Sentence> dL = Sentence.getByDate(databaseHelper, 1998, 3, 31, true);
        for(Sentence d : dL) {
            Log.d(TAG, "testGetSentenceByDate: end of month sentence " + d.getDate().toString());
        }
        assertEquals(1, dL.size());

        sentence.delete(databaseHelper);
    }

    @Test
    public void testGetSentenceByDate() throws SQLException {
        int num = 24;
        List<Sentence> sentenceList = new ArrayList();
        for(int i = 1; i <= num; i++) {
            for(int j = 0; j < i; j++) {
                Sentence sentence = new Sentence();
                sentenceList.add(sentence);
                sentence.setText(originText + j);
                sentence.setDate(new Date(1998 - 1900, 8 - 1, i, j, 0));
                sentence.setSentencebook(sentencebook);
                sentence.insert(databaseHelper);
            }
        }

        List<Sentence> dL;
        for(int i = 1; i <= num; i++) {
            dL = Sentence.getByDate(databaseHelper, 1998, 8, i, true);
            for(Sentence d : dL) {
                Log.d(TAG, "testGetSentenceByDate: " + i + " sentence " + d.getDate().toString());
            }
            assertEquals(i, dL.size());
        }

        for(final Sentence sentence : sentenceList) {
            sentence.delete(databaseHelper);
        }
    }

    @Test
    public void testCountByDateLabel() throws SQLException {
        Random random = new Random();

        int num = 20;
        List<Sentence> sentenceList = new ArrayList();

        Label label1 = new Label();
        label1.setLabelname("hi" + random.nextInt());
        label1.insert(databaseHelper);

        Label label2 = new Label();
        label2.setLabelname("hi2" + random.nextInt());
        label2.insert(databaseHelper);

        int c1 = 0;

        int begin = 5, end = 15;

        for(int i = 1; i <= num; i++) {
            for(int j = 0; j < i; j++) {
                Sentence sentence = new Sentence();
                sentenceList.add(sentence);
                sentence.setText(originText + j);
                sentence.setDate(new Date(1998, 8, i));
                sentence.setSentencebook(sentencebook);
                sentence.insert(databaseHelper);
                if(i % 3 == 0) {
                    sentence.insertLabel(databaseHelper, label1);
                    if(begin <= i && i <= end){
                        c1++;
                    }
                }
                if(random.nextInt() % 2 == 0) {
                    sentence.insertLabel(databaseHelper, label2);
                }
            }
        }

        Date beginDate = new Date(1998, 8, begin), endDate = new Date(1998, 8, end);
        List<Label> labelList = new ArrayList();
        labelList.add(label1);
        assertEquals(c1, Sentence.countByDateLabel(databaseHelper, beginDate, endDate, labelList));
    }

    @Test
    public void testGetAllLabel() throws SQLException {
        Sentence sentence = new Sentence("hello");
        sentence.setDate();
        sentence.setSentencebook(sentencebook);
        sentence.insert(databaseHelper);

        List<Label> labels = new ArrayList();
        labels.add(new Label("label1sadfvabe"));
        labels.add(new Label("janvp"));
        labels.add(new Label("fancevbotvptjvo"));

        for(final Label label : labels) {
            label.insert(databaseHelper);
            sentence.insertLabel(databaseHelper, label);
        }

        List<Label> labels2 = sentence.getAllLabel(databaseHelper);
        assertEquals(3, labels2.size());
        for(final Label label : labels2) {
            Log.d(TAG, "testGetAllLabel: label " + label.getLabelname());
        }

        sentence.delete(databaseHelper);
        QueryBuilder<SentenceLabel, Integer> sentenceLabelQb = databaseHelper.getDaoAccess(SentenceLabel.class).queryBuilder();
        sentenceLabelQb.where().eq(SentenceLabel.SENTENCE_TAG, sentence);
        Log.d(TAG, "testGetAllLabel: " + sentenceLabelQb.prepareStatementString());
        List<SentenceLabel> sentenceLabelList = sentenceLabelQb.query();
        assertEquals(0, sentenceLabelList.size());

        for(final Label label : labels) {
            label.delete(databaseHelper);
        }
    }

    @Test
    public void testGetLike() {
        List<Sentence> sentenceList = new ArrayList();

        for(int i = 0; i < 10; i++) {
            Sentence sentence = new Sentence();
            sentence.setDate();
            sentence.setSentencebook(sentencebook);
            sentence.setText("hello " + i);
            sentence.setIsLike(i % 2 == 0);
            sentenceList.add(sentence);
            sentence.insert(databaseHelper);
        }

        List<Sentence> l = Sentence.getAllLike(databaseHelper, false);
        assertEquals(5, l.size());
        assertEquals("hello 8", l.get(0).getText());

        for(final Sentence d : sentenceList) {
            d.delete(databaseHelper);
        }
    }
}
