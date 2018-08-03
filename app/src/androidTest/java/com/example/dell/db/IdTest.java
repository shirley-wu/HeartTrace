package com.example.dell.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.mock.MockContext;
import android.util.Log;

import org.junit.Test;

/**
 * Created by wu-pc on 2018/8/1.
 */

public class IdTest {

    final static private String TAG = "IdTest";

    @Test
    public void testTwoInstance() {
        IdWorker idWorker1 = new IdWorker(InstrumentationRegistry.getTargetContext());
        IdWorker idWorker2 = new IdWorker(InstrumentationRegistry.getTargetContext());
        Log.d(TAG, "testTwoInstance: worker1 id = " + idWorker1.nextId());
        Log.d(TAG, "testTwoInstance: worker2 id = " + idWorker2.nextId());
    }

    @Test
    public void testSeveralId() {
        // cannot work in test suites
        IdWorker idWorker = new IdWorker(InstrumentationRegistry.getTargetContext());
        for(int i=0; i<30; i++) {
            Log.d(TAG, "testSeveralId: i = " + i + " id = " + idWorker.nextId());
        }
    }
}
