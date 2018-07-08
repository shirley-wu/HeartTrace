package com.example.dell.sync;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Diarybook;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu-pc on 2018/7/7.
 */

public class JsonTest extends InstrumentationTestCase {

    final static private String TAG = "JsonTest";

    private DatabaseHelper databaseHelper;

    private Diarybook diarybook = new Diarybook("fajskdlav");;

    private Diary d1 = new Diary("1: hello");

    private Diary d2 = new Diary("2: hi");

    @Before
    public void setUp() throws SQLException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);

        diarybook.insert(databaseHelper);

        d1.setDate();
        d1.setDiarybook(diarybook);
        d1.insert(databaseHelper);

        d2.setDate();
        d2.setDiarybook(diarybook);
        d2.insert(databaseHelper);
    }

    @After
    public void tearDown() {
        diarybook.delete(databaseHelper);
        OpenHelperManager.releaseHelper();
    }

    @Test
    public void testObjectIntoJson() {
        String jo;
        jo = JSON.toJSONString(d1);
        Log.d(TAG, "testObjectIntoJson: d1 -> " + jo);
        jo = JSON.toJSONString(d2);
        Log.d(TAG, "testObjectIntoJson: d2 -> " + jo);
    }

    @Test
    public void testArrayIntoJson() {
        List<Diary> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);
        String jo;
        jo = JSON.toJSONString(list);
        Log.d(TAG, "testArrayIntoJson: " + jo);
    }

    @Test
    public void testJsonIntoObject() {
        String jo = "{\"date\":1531030209658,\"diarybook\":{\"diarybookName\":\"fajskdlav\"},\"id\":16,\"text\":\"jdakfldasj\"}";
        Diary diary;
        // diary = JSON.parseObject(jo, new TypeReference<Diary>(){});
        diary = JSON.parseObject(jo, Diary.class);
        assertEquals(1531030209658L, diary.getDate().getTime());
        assertEquals("fajskdlav", diary.getDiarybook().getDiarybookName());
        assertEquals(16, diary.getId());
        assertEquals("jdakfldasj", diary.getText());
    }

    @Test
    public void testJsonIntoArray() {
        String jo = "[" +
                "{\"date\":1531030209658,\"diarybook\":{\"diarybookName\":\"fajskdlav\"},\"id\":16,\"text\":\"jdakfldasj\"}," +
                "{\"date\":1546253475874,\"diarybook\":{\"diarybookName\":\"sbrycaevgr\"},\"id\":992,\"text\":\"afxamjncpau\"}" +
                "]";
        List<Diary> list = JSON.parseArray(jo, Diary.class);
        assertEquals(2, list.size());
        assertEquals(1531030209658L, list.get(0).getDate().getTime());
        assertEquals("fajskdlav", list.get(0).getDiarybook().getDiarybookName());
        assertEquals(16, list.get(0).getId());
        assertEquals("jdakfldasj", list.get(0).getText());
        assertEquals(1546253475874L, list.get(1).getDate().getTime());
        assertEquals("sbrycaevgr", list.get(1).getDiarybook().getDiarybookName());
        assertEquals(992, list.get(1).getId());
        assertEquals("afxamjncpau", list.get(1).getText());
    }

}
