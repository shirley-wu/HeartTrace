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
    private Dao<Sentence, Integer> dao;

    private String originText;
    private String updateText;

    @Before
    public void setUp() throws SQLException {
        Log.d(TAG, "setUp");
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        dao = databaseHelper.getSentenceDao();
    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown");
        OpenHelperManager.releaseHelper();
    }


    @Test
    public void testGetSentenceByDate() throws SQLException {
        int num = 20;
        List<Sentence> sentenceList = new ArrayList();
        for(int i = 1; i <= num; i++) {
            for(int j = 0; j < i; j++) {
                Sentence sentence = new Sentence();
                sentenceList.add(sentence);
                sentence.setText(originText + j);
                sentence.setDate(new Date(1998, 8, i));
                dao.create(sentence);
            }
        }

        Date date;
        for(int i = 1; i <= num; i++) {
            assertEquals(i, Sentence.getByDate(databaseHelper, new Date(1998, 8, i)).size());
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
                dao.create(sentence);
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
        QueryBuilder<SentenceLabel, Integer> qb = databaseHelper.getSentenceLabelDao().queryBuilder();
        qb.where().eq(SentenceLabel.SENTENCE_TAG, sentence);
        Log.d(TAG, "testGetAllLabel: " + qb.prepareStatementString());
        List<SentenceLabel> sentenceLabelList = qb.query();
        assertEquals(0, sentenceLabelList.size());

        for(final Label label : labels) {
            label.delete(databaseHelper);
        }
    }
}
