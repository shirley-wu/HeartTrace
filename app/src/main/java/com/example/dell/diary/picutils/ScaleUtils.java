package com.example.dell.diary.picutils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by wu-pc on 2018/8/6.
 */

public class ScaleUtils {

    final static private String TAG = "ScaleUtils";

    static public Bitmap scaleImageForScreen(Activity activity, Bitmap bitmap) {
        // 获取屏幕参数
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthPixels = displayMetrics.widthPixels * 0.8F;
        Log.d(TAG, "scaledImageForScreen: widthPixels = " + widthPixels);

        float density = displayMetrics.density;
        Log.d(TAG, "scaledImageForScreen: density = " + density);

        return scaleImage(bitmap, widthPixels * density);
    }

    static public Bitmap scaleImage(Bitmap image, float targetWidth) {
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
