package com.example.dell.diary.picutils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;

import com.example.dell.db.Picture;
import com.example.dell.diary.DiaryWriteActivity;

/**
 * Created by wu-pc on 2018/8/6.
 */


public class MyImageGetter implements Html.ImageGetter {

    private final static String TAG = "MyImageGetter";

    private DiaryWriteActivity mActivity = null;

    public MyImageGetter(DiaryWriteActivity activity) {
        mActivity = activity;
    }

    @Override
    public Drawable getDrawable(String s) {
        Log.d(TAG, "getDrawable: source = " + s);
        Picture picture = new Picture(s);
        Bitmap bitmap = picture.getBitmap(mActivity);
        bitmap = ScaleUtils.scaleImageForScreen(mActivity, bitmap);
        return new BitmapDrawable(bitmap);
    }

}
