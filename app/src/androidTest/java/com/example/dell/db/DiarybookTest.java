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

public class DiarybookTest extends InstrumentationTestCase {

    final static private String TAG = "DiarybookTest";

    private Diarybook diarybook = new Diarybook("adsfavva");
    
    private Diary diary = new Diary("fdaov");

    private DatabaseHelper helper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());

    @Before
    public void setUp() throws Exception {
        super.setUp();
        diarybook.insert(helper);
        diary.setDate();
        diary.setDiarybook(diarybook);
        diary.insert(helper);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        helper.clearAll();
        helper.close();
    }

    @Test
    public void testGetByName() {
        Diarybook b;
        b = Diarybook.getByName(helper, "null");
        assertEquals(null, b);
        b = Diarybook.getByName(helper, diarybook.getDiarybookName());
        assertEquals(diarybook.getDiarybookName(), b.getDiarybookName());
    }
    
    @Test
    public void testInsertDiary() {
        assertEquals(1, diarybook.getAllSubDiary(helper).size());
        List<Diary> list = diarybook.getAllSubDiary(helper);
        assertEquals(diary.getText(), list.get(0).getText());
        Diarybook b = list.get(0).getDiarybook();
        b.refresh(helper);
        assertEquals(diarybook.getDiarybookName(), b.getDiarybookName());
    }

    @Test
    public void testDelete() {
        assertEquals(0, diarybook.getStatus());
        Log.d(TAG, "delete: modified before delete " + diarybook.getModified());
        
        List<Diary> list = diarybook.getAllSubDiary(helper);
        for(Diary d : list) {
            assertEquals(0, d.getStatus());
            Log.d(TAG, "delete: diary modified before delete " + d.getModified());
        }
        
        diarybook.delete(helper);
        assertEquals(-1, diarybook.getStatus());
        Log.d(TAG, "delete: modified after delete " + diarybook.getModified());
        
        list = diarybook.getAllSubDiary(helper);
        /*for(Diary d : list) {
            assertEquals(-1, d.getStatus());
            Log.d(TAG, "delete: diary modified after delete " + d.getModified());
        }*/
        assertEquals(0, list.size());
        list = Diary.getAll(helper, false);
        assertEquals(0, list.size());
    }

    @Test
    public void testUpdate() {
        assertEquals(0, diarybook.getStatus());
        Log.d(TAG, "update: modified before update " + diarybook.getModified());
        diarybook.update(helper);
        assertEquals(0, diarybook.getStatus());
        Log.d(TAG, "update: modified after update " + diarybook.getModified());
        diarybook.setStatus(9);
        diarybook.update(helper);
        assertEquals(1, diarybook.getStatus());
        Log.d(TAG, "update: modified after update 2 " + diarybook.getModified());
    }

}
