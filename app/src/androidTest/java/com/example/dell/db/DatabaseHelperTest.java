package com.example.dell.db;

import android.content.Context;
import android.provider.Telephony;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wu-pc on 2018/5/10.
 */
public class DatabaseHelperTest {

    final static private String TAG = "DatabaseHelperTest";

    private DatabaseHelper helper;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        helper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
    }

    @After
    public void tearDown() {
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testGetDatabaseHelper() {

    }

    @Test
    public void testClearAll() throws SQLException {
        Label label = new Label("hi");
        label.insert(helper);

        Diarybook diarybook = new Diarybook("d");
        diarybook.insert(helper);

        Diary diary = new Diary("hi");
        diary.setDate();
        diary.setDiarybook(diarybook);
        diary.insert(helper);

        diary.insertLabel(helper, label);

        Sentencebook sentencebook = new Sentencebook("s");
        sentencebook.insert(helper);

        Sentence sentence = new Sentence("hello");
        sentence.setDate();
        sentence.setSentencebook(sentencebook);
        sentence.insert(helper);

        sentence.insertLabel(helper, label);

        assertTrue("label before", Label.getAllLabel(helper).size() >= 0);
        assertTrue("diarybook before", Diarybook.getAll(helper, false).size() >= 0);
        assertTrue("diary before", Diary.getAll(helper, false).size() >= 0);
        assertTrue("diary label before", helper.getDaoAccess(Diary.class).queryForAll().size() >= 0);
        assertTrue("sentence book before", Sentencebook.getAll(helper, false).size() >= 0);
        assertTrue("sentence before", Sentence.getAll(helper, false).size() >= 0);
        assertTrue("sentence label before", helper.getDaoAccess(Sentence.class).queryForAll().size() >= 0);
        
        helper.clearAll();

        assertTrue("label after", Label.getAllLabel(helper).size() == 0);
        assertTrue("diarybook after", Diarybook.getAll(helper, false).size() == 0);
        assertTrue("diary after", Diary.getAll(helper, false).size() == 0);
        assertTrue("diary label after", helper.getDaoAccess(Diary.class).queryForAll().size() == 0);
        assertTrue("sentence book after", Sentencebook.getAll(helper, false).size() == 0);
        assertTrue("sentence after", Sentence.getAll(helper, false).size() == 0);
        assertTrue("sentence label after", helper.getDaoAccess(Sentence.class).queryForAll().size() == 0);
    }

    @Test
    public void testPrintAll() {
        Class[] tableList = helper.getTableList();
        for (Class clazz : tableList) {
            try {
                Dao dao = helper.getDaoAccess(clazz);
                List list = dao.queryForAll();
                Log.d(TAG, "testPrintAll: class = " + clazz);
                for (Object o : list) {
                    Log.d(TAG, "testPrintAll: object = " + JSON.toJSONString(o));
                }
            }
            catch (Exception e) {
                Log.e(TAG, "testPrintAll: ", e);
            }
        }
    }

}
