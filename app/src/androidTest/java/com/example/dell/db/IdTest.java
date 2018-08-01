package com.example.dell.db;

import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

/**
 * Created by wu-pc on 2018/8/1.
 */

public class IdTest {

    final static private String TAG = "IdTest";

    @Test
    public void testTwoInstance() {
        IdWorker idWorker1 = new IdWorker(InstrumentationRegistry.getContext());
        IdWorker idWorker2 = new IdWorker(InstrumentationRegistry.getContext());
        Log.d(TAG, "testTwoInstance: worker1 id = " + idWorker1.nextId());
        Log.d(TAG, "testTwoInstance: worker2 id = " + idWorker2.nextId());
    }

    @Test
    public void testSeveralId() {
        IdWorker idWorker = new IdWorker(InstrumentationRegistry.getContext());
        for(int i=0; i<30; i++) {
            Log.d(TAG, "testSeveralId: i = " + i + " id = " + idWorker.nextId());
        }
    }
}
