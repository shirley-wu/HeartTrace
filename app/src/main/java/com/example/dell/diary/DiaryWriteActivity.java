package com.example.dell.diary;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.diary.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.FileNotFoundException;


public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="Calendar";


    public static final int CHOOSE_PHOTO = 2;

    private ImageView picture;

    private Uri imageUri;

    private LinearLayout settingBottomSheetLayout;
    private TextInputLayout editor;
    private EditText diary;
    private ImageButton confirm;
    private ImageButton get_source;
    private ImageButton setting;
    private android.support.design.widget.CoordinatorLayout layout;
    private BottomSheetBehavior settingBottomSheetBehavior;
    private ImageButton set_theme1;
    private ImageButton set_theme2;
    private ImageButton set_theme3;
    private ImageButton set_theme4;
    private ImageButton set_theme5;
    private ImageButton set_theme6;
    private DiscreteSeekBar set_size;
    private Button set_font1;
    private Button set_font2;
    private Button set_font3;
    private Button drop_down;
    private TextView font_text1;
    private TextView font_text2;
    private TextView font_text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);
        getView();
        setOnListener();
        init();






    }

    public void getView()
    {
        editor = (TextInputLayout) findViewById(R.id.editLayout);
        diary = (EditText) findViewById(R.id.diaryWrite);
        confirm = (ImageButton) findViewById(R.id.confirm);
        get_source = (ImageButton) findViewById(R.id.source);
        setting = (ImageButton) findViewById(R.id.setting);
        layout = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.diaryWriteLayout);
        settingBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.settingBottomSheetLayout));
        set_theme1 = (ImageButton) findViewById(R.id.theme1);
        set_theme2 = (ImageButton) findViewById(R.id.theme2);
        set_theme3 = (ImageButton) findViewById(R.id.theme3);
        set_theme4 = (ImageButton) findViewById(R.id.theme4);
        set_theme5 = (ImageButton) findViewById(R.id.theme5);
        set_theme6 = (ImageButton) findViewById(R.id.theme6);
        set_size = (DiscreteSeekBar) findViewById(R.id.set_size);
        set_font1 = (Button) findViewById(R.id.font1);
        set_font2 = (Button) findViewById(R.id.font2);
        set_font3 = (Button) findViewById(R.id.font3);
        drop_down = (Button) findViewById(R.id.drop_down);
        font_text1 = (TextView) findViewById(R.id.font_text1);
        font_text2 = (TextView) findViewById(R.id.font_text2);
        font_text3 = (TextView) findViewById(R.id.font_text3);
        settingBottomSheetLayout = (LinearLayout) findViewById(R.id.settingBottomSheetLayout);
    }

    public void setOnListener()
    {
        confirm.setOnClickListener(this);
        get_source.setOnClickListener(this);
        setting.setOnClickListener(this);
        set_font1.setOnClickListener(this);
        set_font2.setOnClickListener(this);
        set_font3.setOnClickListener(this);
        drop_down.setOnClickListener(this);
        set_theme1.setOnClickListener(this);
        set_theme2.setOnClickListener(this);
        set_theme3.setOnClickListener(this);
        set_theme4.setOnClickListener(this);
        set_theme5.setOnClickListener(this);
        set_theme6.setOnClickListener(this);
        editor.setOnClickListener(this);
    }

    public void init()
    {
        //AssetManager mgr=getAssets();
        font_text1.setTypeface(Typeface.SERIF);
        font_text2.setTypeface(Typeface.SERIF);
        font_text3.setTypeface(Typeface.SERIF);
        //Typeface tf2=Typeface.createFromAsset(mgr, "fonts/font2.ttf");
        //font_text2.setTypeface(tf2);
        //Typeface tf3=Typeface.createFromAsset(mgr, "fonts/font3.otf");
        //font_text3.setTypeface(tf3);
        diary.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        diary.setTextSize(20);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                Toast.makeText(DiaryWriteActivity.this, diary.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.source:
                if (ContextCompat.checkSelfPermission(DiaryWriteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DiaryWriteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.setting:
                settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.drop_down:
                settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.font1:
                //AssetManager mgr1 = getAssets();
                //Typeface tf1 = Typeface.createFromAsset(mgr1, "fonts/font1.ttf");
                diary.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case R.id.font2:
                //AssetManager mgr2 = getAssets();
                //Typeface tf2 = Typeface.createFromAsset(mgr2, "fonts/font2.ttf");
                diary.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case R.id.font3:
                //AssetManager mgr3 = getAssets();
                //Typeface tf3 = Typeface.createFromAsset(mgr3, "fonts/font3.otf");
                diary.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case R.id.theme1:
                //diary.setTextColor(getResources().getColor(R.color.colorBase));
                layout.setBackgroundResource(R.drawable.background1);
                break;
            case R.id.theme2:
                //diary.setTextColor(getResources().getColor(R.color.colorBase));
                layout.setBackgroundResource(R.drawable.background2);
                break;
            case R.id.theme3:
                //diary.setTextColor(getResources().getColor(R.color.colorBase));
                layout.setBackgroundResource(R.drawable.background3);
                break;
            case R.id.theme4:
                //diary.setTextColor(getResources().getColor(R.color.colorBase));
                layout.setBackgroundResource(R.drawable.background4);
                break;
            case R.id.theme5:
                //diary.setTextColor(getResources().getColor(R.color.colorBase));
                layout.setBackgroundResource(R.drawable.background5);
                break;
            case R.id.theme6:
                //diary.setTextColor(getResources().getColor(R.color.colorBase));
                layout.setBackgroundResource(R.drawable.background6);
                break;
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                openAlbum();
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        ContentResolver cr = DiaryWriteActivity.this.getContentResolver();
        Bitmap bitmap = null;
        Bundle extras = null;
        try {
            //将对象存入Bitmap中
            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        double partion = imgWidth*1.0/imgHeight;
        double sqrtLength = Math.sqrt(partion*partion + 1);
        //新的缩略图大小
        double newImgW = 1024*(partion / sqrtLength);
        double newImgH = 1024*(1 / sqrtLength);
        float scaleW = (float) (newImgW/imgWidth);
        float scaleH = (float) (newImgH/imgHeight);

        Matrix mx = new Matrix();
        //对原图片进行缩放
        mx.postScale(scaleW, scaleH);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
        final ImageSpan imageSpan = new ImageSpan(this,bitmap);
        SpannableString spannableString = new SpannableString("test");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
        //光标移到下一行
        Editable editable = diary.getEditableText();
        int selectionIndex = diary.getSelectionStart();
        spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
        //将图片添加进EditText中
        editable.insert(selectionIndex, spannableString);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


}

