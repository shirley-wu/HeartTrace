package com.example.dell.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.Test;

import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wu-pc on 2018/8/2.
 */

public class PictureTest {

    final static private String TAG = "PictureTest";

    @Test
    public void testParse() {
        Picture picture = new Picture("img_1340156180471.jpg");
        assertEquals(1340156180471L, picture.getId());
    }

    @Test
    public void testBase64() throws SQLException {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        Picture picture = (Picture) databaseHelper.getDaoAccess(Picture.class).queryForAll().get(0);          // TODO: dangerous

        String string = picture.readBase64(context);
        Log.d(TAG, "testBase64: string = " + string);
        boolean status = picture.writeBase64(context, string);
        Log.d(TAG, "testBase64: status = " + status);
    }

}
