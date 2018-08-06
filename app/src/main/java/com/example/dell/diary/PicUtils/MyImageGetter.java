package com.example.dell.diary.PicUtils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.dell.db.Picture;
import com.example.dell.diary.DiaryWriteActivity;

/**
 * Created by wu-pc on 2018/8/6.
 */


class MyImageGetter implements Html.ImageGetter {

    private final static String TAG = "MyImageGetter";

    private DiaryWriteActivity mActivity = null;

    public MyImageGetter(DiaryWriteActivity activity) {
        mActivity = activity;
    }

    @Override
    public Drawable getDrawable(String s) {
        Picture picture = new Picture(s);
        Bitmap bitmap = picture.getBitmap(mActivity);
        bitmap = scaledImageForScreen(bitmap);
        return new BitmapDrawable(bitmap);
    }

    private Bitmap scaledImageForScreen(Bitmap bitmap) {
        // 获取屏幕参数
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthPixels = displayMetrics.widthPixels * 0.8F;
        Log.d(TAG, "scaledImageForScreen: widthPixels = " + widthPixels);

        float density = displayMetrics.density;
        Log.d(TAG, "scaledImageForScreen: density = " + density);

        return scaledImage(bitmap, widthPixels * density);
    }

    private Bitmap scaledImage(Bitmap image, float targetWidth) {
        // 获取图片宽高
        int width = image.getWidth();
        Log.d(TAG, "scaledImage: width = " + width);
        int height = image.getHeight();

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        // 计算宽高缩放率
        float scale = targetWidth / width;
        Log.d(TAG, "scaledImage: scale = " + scale);

        // 缩放图片
        matrix.postScale(scale, scale);
        Bitmap new_image = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
        Log.d(TAG, "scaledImage: new_image width = " + new_image.getWidth());
        return new_image;
    }
}
