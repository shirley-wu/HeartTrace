package com.example.dell.sync;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Diarybook;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.json.JSONObject;

import java.sql.SQLException;

/**
 * Created by wu-pc on 2018/7/7.
 */

public class JsonTest extends InstrumentationTestCase {

    final static private String TAG = "JsonTest";

    private DatabaseHelper databaseHelper;

    private Diarybook diarybook = new Diarybook("fajskdlav");;

    private Diary diary = new Diary("hello");

    @Before
    public void setUp() throws SQLException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);

        diarybook.insert(databaseHelper);

        diary.setDate();
        diary.setDiarybook(diarybook);
        diary.insert(databaseHelper);
    }

    @After
    public void tearDown() {
        diarybook.delete(databaseHelper);
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testObjectIntoJson() {
        class D {
            private int a;

            private double b;

            public void setA(int a) {
                this.a = a;
            }

            public int getA() {
                return a;
            }

            public void setB(double b) {
                this.b = b;
            }

            public double getB() {
                return b;
            }
        }
        D d = new D();
        d.setA(13);
        d.setB(15.5);
        JSONObject json = JSONObject.fromObject(d);
        Log.d(TAG, "testObjectIntoJson: " + json.toString());
    }

}
