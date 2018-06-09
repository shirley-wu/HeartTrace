package com.example.dell.db.examples;

import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Diarybook;
import com.example.dell.db.Label;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wu-pc on 2018/6/8.
 */

public class Example0 extends InstrumentationTestCase {

    private final static String TAG = "Example0";

    private DatabaseHelper helper = null;

    private Diarybook diarybook = null;

    private Diary diary = null;

    private Label label1 = null;

    private Label label2 = null;

    @Before
    public void setUp() {
        helper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());

        diarybook = new Diarybook("hi");
        diarybook.insert(helper);
    }

    @After
    public void tearDown() {
        if(label1 != null) {
            label1.delete(helper);
        }
        if(label2 != null) {
            label2.delete(helper);
        }
        if(diary != null) {
            diary.delete(helper);
        }
        if(diarybook != null) {
            diarybook.delete(helper);
        }
        helper.close();
    }

    @Test
    public void testExample0() throws SQLException {
        diary = new Diary("hiasjfhlaf");
        diary.setDate();
        diary.setDiarybook(diarybook);
        diary.insert(helper);

        label1 = new Label("label1");
        label1.insert(helper);

        label2 = new Label("label2");
        label2.insert(helper);

        diary.insertLabel(helper, label1);
        diary.insertLabel(helper, label2);

        List<Label> list = diary.getAllLabel(helper);
        assertEquals(2, list.size());
        for(Label l : list) {
            Log.d(getClass().getName(), "testExample0: label " + l.getLabelname());
        }
    }

}
