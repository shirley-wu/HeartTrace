package com.example.dell.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wu-pc on 2018/8/3.
 */

public class SyncReceiver extends BroadcastReceiver {

    private static final String TAG = "SyncReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: context = " + context);
        String message = intent.getStringExtra("message");
        Log.d(TAG, "onReceive: message = " + message);
        Toast.makeText(context, "心迹：" + message, Toast.LENGTH_LONG).show();
    }

}
