package com.example.dell.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wu-pc on 2018/6/3.
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    DatabaseHelper helper;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: begin");

        try{
            DiarySync();
            SentenceSync();
            DiarybookSync();
            SentencebookSync();
            SentenceLabelSync();
            LabelSync();
            DiaryLabelSync();
            SentenceLabelSync();
        }
        catch(SQLException e) {
            Log.d(TAG, "onPerformSync: error");
        }

        Log.d(TAG, "onPerformSync: end");
    }

    public void DiarySync() throws SQLException {

    }

    public void SentenceSync() throws SQLException {

    }

    public void DiarybookSync() throws SQLException {

    }

    public void SentencebookSync() throws SQLException {

    }

    public void LabelSync() throws SQLException {

    }

    public void DiaryLabelSync() throws SQLException {

    }

    public void SentenceLabelSync() throws SQLException {

    }

}