package com.example.dell.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import static android.content.Context.ACCOUNT_SERVICE;

/**
 * Created by wu-pc on 2018/6/3.
 */

public class SyncTest {

    private static final String TAG = "SyncTest";

    private static final String ACCOUNT_TYPE = "dell.example.com";

    private static final String AUTHORITY = "com.example.dell.sync.provider";

    private Account mAccount;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        mAccount = CreateSyncAccount(context, "dummy");
        ContentResolver.setIsSyncable(mAccount, AUTHORITY, 1);
    }

    @Test
    public void testPeriodSync() throws InterruptedException {
        ContentResolver.addPeriodicSync(
                mAccount, AUTHORITY,
                Bundle.EMPTY, 1); // 1 seconds period
        Log.d(TAG, "testPeriodSync: begin to sleep");
        Thread.sleep(5500);
        Log.d(TAG, "testPeriodSync: sleep end");
    }

    @Test
    public void testRequestSync() throws InterruptedException {
        ContentResolver.requestSync(mAccount, AUTHORITY, Bundle.EMPTY);
        Log.d(TAG, "testRequestSync: begin to sleep");
        Thread.sleep(500);
        Log.d(TAG, "testRequestSync: sleep end");
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context, String name) {
        // Create the account type and default account
        Account newAccount = new Account(
                name, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            return newAccount;
        } else {
            Log.d(context.toString(), "CreateSyncAccount: cannot create");
            return newAccount;
        }
    }

}
