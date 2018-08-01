package com.example.dell.appid;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by wu-pc on 2018/8/1.
 */

public class AppIdWorker {

    final static private String TAG = "AppIdWorker";

    final static private String INSTALLATION = "INSTALLATION";

    public static String getAppId(Context context) throws Exception {
        File installation = new File(context.getFilesDir(), INSTALLATION);

        if (!installation.exists()) {
            FileOutputStream out = context.openFileOutput(INSTALLATION, Context.MODE_PRIVATE);
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
            out.close();
        }

        RandomAccessFile file = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) file.length()];
        file.readFully(bytes);
        file.close();
        return new String(bytes);
    }

}
