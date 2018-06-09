package com.example.dell.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import junit.framework.TestResult;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by huang on 5/17/2018.
 */

public class DiaryGetByRestrictTest extends InstrumentationTestCase {

    private static final String TAG = "DiaryGetByRestrictTest";

    private DatabaseHelper databaseHelper;
    private Dao<Diary, Integer> dao;

    private List<Label> labels = new ArrayList();
    private List<Diary> diaryShirly = new ArrayList();
    private List<Diary> diaryLisa = new ArrayList();
    private List<Diary> diaryMmp = new ArrayList();
    private Diarybook diarybook;

    @Before
    public void setUp() throws SQLException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        dao = databaseHelper.getDiaryDao();

        labels.add(new Label("label1sadfvabe"));
        labels.add(new Label("janvp"));
        labels.add(new Label("fancevbotvptjvo"));

        for(final Label label : labels) {
            label.insert(databaseHelper);
        }

        diarybook = new Diarybook("hi");
        diarybook.insert(databaseHelper);

        String text;

        text = "hello, this is Shirley";
        for(int i = 1; i <= 30; i++) {
            Diary diary = new Diary(text);
            diary.setDate(new Date(2016, 1, i));
            diary.setDiarybook(diarybook);
            diary.insert(databaseHelper);
            diary.insertLabel(databaseHelper, labels.get(0));
            diaryShirly.add(diary);
        }

        text = "hello, this is Lisa";
        for(int i = 1; i <= 29; i++) {
            Diary diary = new Diary(text);
            diary.setDate(new Date(2016, 1, i));
            diary.setDiarybook(diarybook);
            diary.insert(databaseHelper);
            diaryLisa.add(diary);
            diary.insertLabel(databaseHelper, labels.get(1));
        }

        text = "emmmmmmmmmmm";
        for(int i = 1; i <= 28; i++) {
            Diary diary = new Diary(text);
            diary.setDate(new Date(2015, 1, i));
            diary.setDiarybook(diarybook);
            diary.insert(databaseHelper);
            diaryLisa.add(diary);
            diary.insertLabel(databaseHelper, labels.get(0));
            diary.insertLabel(databaseHelper, labels.get(1));
        }
    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown");
        for(final Label label : labels) {
            label.delete(databaseHelper);
        }
        for(final Diary diary : diaryShirly) {
            diary.delete(databaseHelper);
        }
        for(final Diary diary : diaryLisa) {
            diary.delete(databaseHelper);
        }
        for(final Diary diary : diaryMmp) {
            diary.delete(databaseHelper);
        }
        diarybook.delete(databaseHelper);
        OpenHelperManager.releaseHelper();
    }

    // @Test
    public void testWhereSingleText() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        Where<Diary, Integer> where = qb.where();
        where.like("text", "%hi%");
        where.or(1);
        Log.d(TAG, "instance initializer: " + qb.prepareStatementString());
    }

    // @Test
    public void testWhereMultipleText() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        Where<Diary, Integer> where = qb.where();
        where.like("text", "%hi%");
        where.like("text", "%hello%");
        where.or(2);
        Log.d(TAG, "instance initializer: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereSingleText() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        Diary.buildWhere(qb.where(), "hellohi");
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereMultilpleText() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        Diary.buildWhere(qb.where(), "hello hi lueluelue blablabla");
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereDate() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        Diary.buildWhere(qb.where(), new Date(1998, 2, 19), new Date(2000, 2, 4));
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereDateAndSingleText() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        Where<Diary, Integer> w = qb.where();
        Diary.buildWhere(w, "hi");
        Diary.buildWhere(w, new Date(1998, 2, 19), new Date(2000, 2, 4));
        w.and(2);
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereDateAndMultipleText() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        Where<Diary, Integer> w = qb.where();
        Diary.buildWhere(w, "hi hello");
        Diary.buildWhere(w, new Date(1998, 2, 19), new Date(2000, 2, 4));
        w.and(2);
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildQuerySingleLabel() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        List<Label> labelList = new ArrayList();
        labelList.add(labels.get(0));
        Diary.buildQuery(qb, databaseHelper, labelList);
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildQueryMultipleLabel() throws SQLException {
        QueryBuilder<Diary, Integer> qb = databaseHelper.getDiaryDao().queryBuilder();
        Diary.buildQuery(qb, databaseHelper, labels);
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testGetByRestrictText() throws SQLException {
        List<Diary> list;

        list = Diary.getByRestrict(databaseHelper, "Shirley", null, null, null, true);
        assertEquals(30, list.size());

        list = Diary.getByRestrict(databaseHelper, "Lisa", null, null, null, true);
        assertEquals(29, list.size());

        list = Diary.getByRestrict(databaseHelper, "hello Lisa", null, null, null, true);
        assertEquals(30 + 29, list.size());
    }

    @Test
    public void testGetByRestrictDate() throws SQLException {
        List<Diary> list;
        list = Diary.getByRestrict(databaseHelper, null, new Date(2016, 1, 5),
                                   new Date(2016, 1, 23), null, true);
        assertEquals(2 * (23 - 5 + 1), list.size());
    }

    @Test
    public void testGetByRestrictLabel() throws SQLException {
        List<Label> labelList;
        List<Diary> list;

        labelList = new ArrayList();
        labelList.add(labels.get(0));
        list = Diary.getByRestrict(databaseHelper, null, null, null, labelList, true);
        assertEquals(30 + 28, list.size());

        labelList = new ArrayList();
        labelList.add(labels.get(1));
        list = Diary.getByRestrict(databaseHelper, null, null, null, labelList, true);
        assertEquals(29 + 28, list.size());

        labelList = new ArrayList();
        labelList.add(labels.get(1));
        labelList.add(labels.get(0));
        list = Diary.getByRestrict(databaseHelper, null, null, null, labelList, true);
        assertEquals(28, list.size());
    }

    @Test
    public void testGetByRestrictAllAscending() throws SQLException {
        List<Label> labelList = new ArrayList();
        List<Diary> list;

        labelList.add(labels.get(1));
        list = Diary.getByRestrict(databaseHelper, "this",
                new Date(2016, 1, 8),
                new Date(2016, 1, 27),
                labelList, true);
        assertEquals(27 - 8 + 1, list.size());
        assertEquals(new Date(2016, 1, 8).getTime(), list.get(0).getDate().getTime());
    }

    @Test
    public void testGetByRestrictAllDescending() throws SQLException {
        List<Label> labelList = new ArrayList();
        List<Diary> list;

        labelList.add(labels.get(1));
        list = Diary.getByRestrict(databaseHelper, "this",
                new Date(2016, 1, 8),
                new Date(2016, 1, 27),
                labelList, false);
        assertEquals(27 - 8 + 1, list.size());
        assertEquals(new Date(2016, 1, 27).getTime(), list.get(0).getDate().getTime());
    }

    @Test
    public void testDiarybook() {
        assertEquals("hi", diaryLisa.get(0).getDiarybook().getDiarybookName());
    }
}
