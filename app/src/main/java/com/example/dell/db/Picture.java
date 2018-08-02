package com.example.dell.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by wu-pc on 2018/8/2.
 */

@DatabaseTable
public class Picture {

    final static private String TAG = "Picture";

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "/HeartTrace/pic/";

    private static final String IN_PATH = "/HeartTrace/pic/";

    @DatabaseField
    private long id;

    @DatabaseField
    private long modified;

    private String parentPath = null;

    public Picture() {

    }

    public Picture(long id) {
        this.id = id;
    }

    public Picture(String fileName) {
        id = Long.parseLong(fileName.substring(4).replace(".jpg", ""));
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public long getModified() {
        return modified;
    }

    public String getFileName() {
        return "img_" + id + ".jpg";
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getParentPath(Context context) {
        if (parentPath == null) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                parentPath = SD_PATH;
            } else {
                parentPath = context.getApplicationContext().getFilesDir().getAbsolutePath() + IN_PATH;
            }
        }
        return parentPath;
    }

    public boolean saveBitmap(Context context, DatabaseHelper databaseHelper, Bitmap bitmap) throws RuntimeException {
        if (bitmap == null) {
            throw new RuntimeException("No picture to be saved");
        }
        try {
            // 设定参数
            id = databaseHelper.getIdWorker().nextId();
            modified = System.currentTimeMillis();

            // 获取路径
            String filePath = getParentPath(context) + getFileName();
            try {
                File filePic = new File(filePath);
                if (!filePic.exists()) {
                    filePic.getParentFile().mkdirs();
                    filePic.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(filePic);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Log.e(TAG, "saveBitmap: ", e);
                return false;
            }

            Dao<Picture, Long> dao = databaseHelper.getDaoAccess(Picture.class);
            int status = dao.create(this);
            Log.d(TAG, "saveBitmap: 插入返回值 = " + status);

            return true;
        }
        catch (SQLException e) {
            Log.e(TAG, "saveBitmap: ", e);
            return false;
        }
    }

    public Bitmap getBitmap(Context context) {
        String imgPath = getParentPath(context) + getFileName();
        return BitmapFactory.decodeFile(imgPath);
    }

    public String readBase64(Context context, DatabaseHelper databaseHelper) {
        try {
            String filePath = getParentPath(context) + getFileName();

            File file = new File(filePath);
            InputStream inputStream = new FileInputStream(file);

            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();

            return Base64.encodeToString(data, Base64.DEFAULT);
        }
        catch (Exception e) {
            Log.e(TAG, "readBase64: ", e);
            return null;
        }
    }

    public boolean writeBase64(Context context, String content) {
        try {
            String filePath = getParentPath(context) + getFileName();

            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] data = Base64.decode(content, Base64.DEFAULT);
            outputStream.write(data);
            outputStream.close();

            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "writeBase64: ", e);
            return false;
        }
    }
}
