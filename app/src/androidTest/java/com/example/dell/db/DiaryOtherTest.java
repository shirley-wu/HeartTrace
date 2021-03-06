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

public class DiaryOtherTest {

    private static final String TAG = "DiaryGetByRestrictTest";

    private DatabaseHelper databaseHelper;

    private String originText;
    private String updateText;

    Diarybook diarybook = new Diarybook("jkbivdtcvnn");

    @Before
    public void setUp() throws SQLException {
        Log.d(TAG, "setUp");
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        diarybook.insert(databaseHelper);
    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown");
        diarybook.delete(databaseHelper);
        OpenHelperManager.releaseHelper();
    }


    @Test
    public void testGetDiaryByDateEndOfMonth() throws SQLException {
        Diary diary = new Diary();

        diary.setText(originText);

        Calendar calendar = Calendar.getInstance();
        calendar.set(1998, 3 - 1, 31, 23, 59);
        diary.setDate(calendar.getTime());
        diary.setDiarybook(diarybook);
        diary.insert(databaseHelper);

        List<Diary> dL = Diary.getByDate(databaseHelper, 1998, 3, 31);
        for(Diary d : dL) {
            Log.d(TAG, "testGetDiaryByDate: end of month diary " + d.getDate().toString());
        }
        assertEquals(1, dL.size());

        diary.delete(databaseHelper);
    }

    @Test
    public void testGetDiaryByDate() throws SQLException {
        int num = 24;
        List<Diary> diaryList = new ArrayList();
        for(int i = 1; i <= num; i++) {
            for(int j = 0; j < i; j++) {
                Diary diary = new Diary();
                diaryList.add(diary);
                diary.setText(originText + j);
                diary.setDate(new Date(1998, 8, i, j, 0));
                diary.setDiarybook(diarybook);
                diary.insert(databaseHelper);
            }
        }

        List<Diary> dL;
        for(int i = 1; i <= num; i++) {
            dL = Diary.getByDate(databaseHelper, 1998, 8, i);
            for(Diary d : dL) {
                Log.d(TAG, "testGetDiaryByDate: " + i + " diary " + d.getDate().toString());
            }
            assertEquals(i, dL.size());
        }

        for(final Diary diary : diaryList) {
            diary.delete(databaseHelper);
        }
    }

    @Test
    public void testCountByDateLabel() throws SQLException {
        Random random = new Random();

        int num = 20;
        List<Diary> diaryList = new ArrayList();

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
                Diary diary = new Diary();
                diaryList.add(diary);
                diary.setText(originText + j);
                diary.setDate(new Date(1998, 8, i));
                diary.setDiarybook(diarybook);
                diary.insert(databaseHelper);
                if(i % 3 == 0) {
                    diary.insertLabel(databaseHelper, label1);
                    if(begin <= i && i <= end){
                        c1++;
                    }
                }
                if(random.nextInt() % 2 == 0) {
                    diary.insertLabel(databaseHelper, label2);
                }
            }
        }

        Date beginDate = new Date(1998, 8, begin), endDate = new Date(1998, 8, end);
        List<Label> labelList = new ArrayList();
        labelList.add(label1);
        assertEquals(c1, Diary.countByDateLabel(databaseHelper, beginDate, endDate, labelList));
    }

    @Test
    public void testGetAllLabel() throws SQLException {
        Diary diary = new Diary("hello");
        diary.setDate();
        diary.setDiarybook(diarybook);
        diary.insert(databaseHelper);

        List<Label> labels = new ArrayList();
        labels.add(new Label("label1sadfvabe"));
        labels.add(new Label("janvp"));
        labels.add(new Label("fancevbotvptjvo"));

        for(final Label label : labels) {
            label.insert(databaseHelper);
            diary.insertLabel(databaseHelper, label);
        }

        List<Label> labels2 = diary.getAllLabel(databaseHelper);
        assertEquals(3, labels2.size());
        for(final Label label : labels2) {
            Log.d(TAG, "testGetAllLabel: label " + label.getLabelname());
        }

        diary.delete(databaseHelper);
        QueryBuilder<DiaryLabel, Integer> diaryLabelQb = databaseHelper.getDiaryLabelDao().queryBuilder();
        diaryLabelQb.where().eq(DiaryLabel.DIARY_TAG, diary);
        Log.d(TAG, "testGetAllLabel: " + diaryLabelQb.prepareStatementString());
        List<DiaryLabel> diaryLabelList = diaryLabelQb.query();
        assertEquals(0, diaryLabelList.size());

        for(final Label label : labels) {
            label.delete(databaseHelper);
        }
    }
}
