package com.example.dell.sync;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Diarybook;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu-pc on 2018/7/8.
 */

public class SyncUtilsTest extends InstrumentationTestCase {

    final static private String TAG = "SyncUtilsTest";

    SyncAdapter syncAdapter;

    @Before
    public void setUp(){
        Context context = InstrumentationRegistry.getTargetContext();
        syncAdapter = new SyncAdapter(context, true);
    }

    /*@Test
    public void testPostSyncData() {
        String response = syncAdapter.postSyncData("Diary",
                "{\"DiaryList\":[" +
                        "{\"date\":1531030209658,\"diarybook\":{\"id\":134},\"id\":16,\"text\":\"jdakfldasj\"}," +
                        "{\"date\":1546253475874,\"diarybook\":{\"id\":151},\"id\":992,\"text\":\"afxamjncpau\"}" +
                        "]}");
        Log.d(TAG, "testPostSyncData: response\n" + response);
    }*/

    /*@Test
    public void testUnparseJson() {
        String jo = "{\"DiaryList\":" +
                "[{\"date\":\"1531030209658\",\"diarybook\":{\"diarybookName\":\"fajskdlav\"},\"id\":16,\"text\":\"jdakfldasj\"}," +
                "{\"date\":\"1546253475874\",\"diarybook\":{\"diarybookName\":\"sbrycaevgr\"},\"id\":992,\"text\":\"afxamjncpau\"}]}";
        List<Diary> list = syncAdapter.unparseJson(jo, Diary.class);
        assertEquals(2, list.size());
        assertEquals(1531030209658L, list.get(0).getDate().getTime());
        assertEquals("fajskdlav", list.get(0).getDiarybook().getDiarybookName());
        assertEquals(16, list.get(0).getId());
        assertEquals("jdakfldasj", list.get(0).getText());
        assertEquals(1546253475874L, list.get(1).getDate().getTime());
        assertEquals("sbrycaevgr", list.get(1).getDiarybook().getDiarybookName());
        assertEquals(992, list.get(1).getId());
        assertEquals("afxamjncpau", list.get(1).getText());
    }*/

    /*@Test
    public void testParseJson() {
        Diarybook diarybook = new Diarybook("acetbv");

        Diary d1 = new Diary("1: hello");
        d1.setDate();
        d1.setDiarybook(diarybook);
        d1.setId(12);

        Diary d2 = new Diary("2: hi");
        d2.setDate();
        d2.setDiarybook(diarybook);
        d2.setId(151);

        List<Diary> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);

        String jo = syncAdapter.parseJson(list, Diary.class);
        Log.d(TAG, "testParseDiarySync: jo " + jo);
    }*/

}
