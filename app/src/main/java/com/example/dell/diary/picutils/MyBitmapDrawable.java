package com.example.dell.diary.picutils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by wu-pc on 2018/8/6.
 */

public class MyBitmapDrawable extends BitmapDrawable {

    public MyBitmapDrawable(Bitmap bitmap) {
        super(bitmap);
        int width = getIntrinsicWidth();
        int height = getIntrinsicHeight();
        setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
    }

}
