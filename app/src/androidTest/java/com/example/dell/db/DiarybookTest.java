package com.example.dell.db;

import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by wu-pc on 2018/6/8.
 */

public class DiarybookTest extends InstrumentationTestCase {

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
        diarybook.delete(helper);
        diary.delete(helper);
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
        assertEquals(diary.getText(), diarybook.getAllSubDiary(helper).get(0).getText());
    }
}
