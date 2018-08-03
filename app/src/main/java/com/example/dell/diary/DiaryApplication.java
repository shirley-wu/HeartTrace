package com.example.dell.diary;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.dell.sync.SyncReceiver;

/**
 * Created by wu-pc on 2018/8/3.
 */

public class DiaryApplication extends Application {

    private static final String TAG = "DiaryApplication";

    private static final String BROADCAST_ACTION = "com.example.dell.diary.SYNC_ACTION";

    private SyncReceiver syncReceiver = null;

    private int mFinalCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: register SyncReceiver");

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
                if (mFinalCount == 1){
                    //说明从后台回到了前台
                    Log.d(TAG, "onActivityStarted: activity = " + activity);
                    Log.d(TAG, "onActivityStarted: register SyncReceiver");
                    syncReceiver = new SyncReceiver();
                    IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
                    registerReceiver(syncReceiver, intentFilter);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                if (mFinalCount == 0){
                    //说明从前台回到了后台
                    Log.d(TAG, "onActivityStopped: activity = " + activity);
                    Log.d(TAG, "onActivityStopped: unregister SyncReceiver");
                    unregisterReceiver(syncReceiver);
                    syncReceiver = null;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

}
