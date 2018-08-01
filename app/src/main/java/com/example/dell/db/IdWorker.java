package com.example.dell.db;

import android.content.Context;
import android.util.Log;

import com.example.dell.appid.AppIdWorker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by wu-pc on 2018/8/1.
 */

public class IdWorker {

    final private static String TAG = "IdWorker";

    static private long twepoch = 1288834974657L;

    static private long workerIdBits = 5L;

    static private long datacenterIdBits = 5L;

    static private long maxWorkerId = -1L ^ (-1L << workerIdBits);

    static private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    static private long sequenceBits = 12L;

    static private long workerIdShift = sequenceBits;

    static private long datacenterIdShift = sequenceBits + workerIdBits;

    static private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    static private long sequenceMask = -1L ^ (-1L << sequenceBits);

    static private long lastTimestamp = -1L;

    private long datacenterId;

    private long workerId;

    private long sequence;

    public IdWorker(Context context) {
        // generate datacenter id
        try {
            datacenterId = (long) AppIdWorker.getAppId(context).hashCode();
        } catch (Exception e) {
            datacenterId = new Random().nextLong();
        } finally {
            datacenterId = datacenterId & maxDatacenterId;
        }

        // generate worker id
        workerId = System.identityHashCode(this);
        workerId = workerId & maxWorkerId;

        sequence = 0;

        Log.d(TAG, "IdWorker: worker starting.");
        Log.d(TAG, "IdWorker: timestamp left shift = " + timestampLeftShift);
        Log.d(TAG, "IdWorker: datacenter id bits = " + datacenterIdBits);
        Log.d(TAG, "IdWorker: max datacenter id = " + maxDatacenterId);
        Log.d(TAG, "IdWorker: datacenter id = " + datacenterId);
        Log.d(TAG, "IdWorker: worker id bits = " + workerIdBits);
        Log.d(TAG, "IdWorker: max worker id = " + maxWorkerId);
        Log.d(TAG, "IdWorker: worker id = " + workerId);
        Log.d(TAG, "IdWorker: sequence bits = " + sequenceBits);
        Log.d(TAG, "IdWorker: sequence = " + sequence);
    }

    public long getWorkerId(){
        return workerId;
    }

    public long getDatacenterId(){
        return datacenterId;
    }

    private long getTimestamp(){
        return System.currentTimeMillis();
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = getTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getTimestamp();
        }
        return timestamp;
    }

    public synchronized long nextId() {
        long timestamp = getTimestamp();

        if (timestamp < lastTimestamp) {
            RuntimeException e = new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp)
            );
            Log.e(TAG, "clock is moving backwards.  Rejecting requests until = " + lastTimestamp, e);
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

}
