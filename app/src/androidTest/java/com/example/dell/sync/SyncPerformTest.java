package com.example.dell.sync;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;

import com.example.dell.db.DatabaseHelper;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.Test;

/**
 * Created by wu-pc on 2018/7/8.
 */

public class SyncPerformTest extends InstrumentationTestCase {

    @Test
    public void testDiarySync() {
        Context context = InstrumentationRegistry.getTargetContext();
        SyncAdapter syncAdapter = new SyncAdapter(context, true);
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        syncAdapter.DiarySync(databaseHelper);
        OpenHelperManager.releaseHelper();
    }

}
