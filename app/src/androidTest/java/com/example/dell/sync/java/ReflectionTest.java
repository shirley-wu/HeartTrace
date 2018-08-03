package com.example.dell.sync.java;

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

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Created by wu-pc on 2018/7/9.
 */

public class ReflectionTest extends InstrumentationTestCase {

    final private static String TAG = "ReflectionTest";

    DatabaseHelper helper;

    Diarybook diarybook;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        helper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);

        diarybook = new Diarybook("hi");
        diarybook.insert(helper);
        for(int i = 1; i <= 30; i++) {
            Diary diary = new Diary("hello" + i);
            diary.setDate(new Date(2016, 1, i));
            diary.setDiarybook(diarybook);
            diary.insert(helper);
        }
    }

    @After

    public void tearDown() {
        diarybook.delete(helper);
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testSyncReflection() throws Exception {
        Class c = Diary.class;
        Method getAll = c.getDeclaredMethod("getAll", DatabaseHelper.class, boolean.class);
        List list = (List) getAll.invoke(null, helper, false);
        for(Object o : list) {
            Log.d(TAG, "getAllTest: class " + o.getClass().getName());
            assertEquals(c, o.getClass());
        }

        Method setDate = c.getDeclaredMethod("setDate");
        Method iou = c.getDeclaredMethod("insertOrUpdate", DatabaseHelper.class);
        for (Object o : list) {
            setDate.invoke(o);
            iou.invoke(o, helper);
        }
    }

}
