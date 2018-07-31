package com.example.dell.sync;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Diarybook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by wu-pc on 2018/7/9.
 */

public class SyncActionTest {

    final static private String TAG = "SyncActionTest";

    Context context;

    SyncAdapter syncAdapter;

    DatabaseHelper databaseHelper;

    Diarybook diarybook;

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
        syncAdapter = new SyncAdapter(context, true);
        databaseHelper = new DatabaseHelper(context);

        diarybook = new Diarybook("acetbv");
        diarybook.insert(databaseHelper);
        Log.d(TAG, "setUp: " + diarybook.getDiarybookName());

        Diary d1 = new Diary("1: hello");
        d1.setDate();
        d1.setDiarybook(diarybook);
        d1.setId(12);
        d1.insert(databaseHelper);

        Diary d2 = new Diary("2: hi");
        d2.setDate();
        d2.setDiarybook(diarybook);
        d2.setId(151);
        d2.insert(databaseHelper);
    }

    @After
    public void tearDown() {
        diarybook.delete(databaseHelper);
        databaseHelper.close();
    }

    @Test
    public void testSyncDiary() {
        // syncAdapter.sync(databaseHelper, Diary.class);
        syncAdapter.syncDiary(databaseHelper);
    }
}
