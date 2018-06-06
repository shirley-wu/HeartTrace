package com.example.dell.sync;

import android.test.InstrumentationTestCase;

import com.example.dell.db.Diary;

import org.junit.Test;

/**
 * Created by wu-pc on 2018/6/6.
 */

public class AccessorSimpleTest extends InstrumentationTestCase {
    // TODO: this dummy class has no use at all... plan to test using http://www.mock-server.com/.

    @Test
    public void getResponseDataTest() {
        SyncServerAccessor accessor = new SyncServerAccessor();
        String data = accessor.getResponseData("test url", Diary.class);
    }

}
