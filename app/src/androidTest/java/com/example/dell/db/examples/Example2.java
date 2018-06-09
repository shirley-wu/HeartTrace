package com.example.dell.db.examples;

import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Diarybook;
import com.example.dell.db.Label;
import com.example.dell.db.Sentence;
import com.example.dell.db.Sentencebook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wu-pc on 2018/6/8.
 */

public class Example2 extends InstrumentationTestCase {

    private final static String TAG = "Example2";

    private Diarybook book = new Diarybook("whatever");

    private Label label = new Label("label0");

    private List<Diary> list = new ArrayList();

    private DatabaseHelper helper;

    @Before
    public void setUp() {
        helper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        book.insert(helper);
        label.insert(helper);
        for(int i = 0; i < 10; i++) {
            Diary diary = new Diary("diary" + i);
            diary.setDiarybook(book);
            diary.setDate(new Date(2017, 1, i));
            diary.insert(helper);
            if(i%2 == 0) {
                diary.insertLabel(helper, label);
            }
        }
    }

    @After
    public void tearDown() {
        label.delete(helper);
        book.delete(helper);
        helper.close();
    }

    @Test
    public void testGetAllDiariesFromLabel() throws SQLException {
        List<Label> labelList = new ArrayList();
        labelList.add(label);

        List<Diary> d = Diary.getByRestrict(helper, null, null, null, labelList, true);

        assertEquals(5, d.size());
        for(int i = 0; i < 5; i ++) {
            assertEquals("diary" + i*2, d.get(i).getText());
        }
    }

}
