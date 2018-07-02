package com.example.dell.diary;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.LinkAddress;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Label;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,View.OnLongClickListener {
//    private ViewPager vp;
//    private DiaryFragment diaryFragment;
//    private DiaryFragment preDiaryFragment;
//    private DiaryFragment nextDiaryFragment;
//    private List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<android.support.v4.app.Fragment>();
//    private DiaryFragmentAdapter mFragmentAdapter;

    public static final int CHOOSE_PHOTO = 2;
    private final int ICON_LIST_DIALOG = 1;
    int tagId = 0;
    public String label_name = "happy";
    private int flag = 1;
    private boolean labelProperty;
    private boolean isExisted;
    private int labelSize;
    private int[] imgIds = {R.drawable.happy, R.drawable.normal, R.drawable.sad,
                            R.drawable.embarrassed, R.drawable.shocked, R.drawable.foolish,
                            R.drawable.travel, R.drawable.work, R.drawable.study, R.drawable.entertainment, R.drawable.love};
    private String tag= null;
    Diary diary;
    List<Diary> diaryList = new ArrayList<>();
    List<Label> labelList = new ArrayList<>();
    List<Label> label_this = new ArrayList<>();
    private List<ImageView> imageItems = new ArrayList<ImageView>(3);
    int index;
    ActionBar actionBar;
    private TextView diaryDate;
    private TextView diaryWeekday;
    private ImageView diaryIcon;
    private ImageView diaryIcon1;
    private ImageView diaryIcon2;
    private ImageView diaryIcon3;
    private ImageView diaryIcon4;
    private EditText diary_write;
    private LinearLayout edit_layout;
    private LinearLayout date_layout;
    private LinearLayout set_layout;
    private ConstraintLayout floatingButtons;
    private DiscreteSeekBar set_size;
    private ImageButton confirm;
    private int start=0;
    private int count=0;
    private int font_color=8;
    private int font_type=1;
    private int style=0;
    private boolean is_retract=false;
    private boolean is_underline=false;
    private boolean is_bold=false;
    private boolean is_italic=false;
    private ImageButton font_set;
    private BottomSheetBehavior font_setting_bottom_sheet;
    private TextView set_font1;
    private TextView set_font2;
    private TextView set_font3;
    private ImageButton set_center;
    private ImageButton set_left;
    private ImageButton set_right;
    private TextView font_padding1;
    private TextView font_padding2;
    private TextView font_padding3;
    private ImageButton line_spacing1;
    private ImageButton line_spacing2;
    private ImageButton line_spacing3;
    private ImageButton set_underline;
    private ImageButton set_bold;
    private ImageButton set_italic;
    private ImageButton insert_image;
    private ObjectAnimator objAnimatorX;

    private SpannableStringBuilder spannableString = new SpannableStringBuilder();
    public List<String> weekList = new ArrayList<>(Arrays.asList("周日","周一","周二","周三"," 周四","周五","周六"));

    private float mPosX, mPosY, mCurPosX, mCurPosY;
    private static final int FLING_MIN_DISTANCE = 20;// 移动最小距离
    private static final int FLING_MIN_VELOCITY = 200;// 移动最大速度
    //构建手势探测器
    private GestureDetector mGestureDetector;
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private ImageView emptyImage;
    String originType;

    private FloatingActionButton addDiary;
    private FloatingActionButton edit;
    private FloatingActionButton enterBottle;
    private FloatingActionButton like;
    private FloatingActionButton add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        getView();
        setOnListener();
        init();
//        //initViewPage();
        mGestureDetector = new GestureDetector(new MyGestureListener()); //使用派生自OnGestureListener

        edit_layout.setOnTouchListener(this);
        edit_layout.setFocusable(true);
        edit_layout.setClickable(true);
        edit_layout.setLongClickable(true);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorite:
                        Toast.makeText(DiaryWriteActivity.this, "暂不支持查看收藏T^T", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.statistics:
                        Intent intent = new Intent(DiaryWriteActivity.this,StatisticsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.calendar_search:
                        Intent intent2 = new Intent(DiaryWriteActivity.this,CalendarActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.time_line:
                        Intent intent3 = new Intent(DiaryWriteActivity.this,TimeLineActivity.class);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        });
    }
    protected void onRestart(){
        super.onRestart();
        Log.d("restart",originType);
        reStartInit();
    }
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private class MyGestureListener implements GestureDetector.OnGestureListener{
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            //Toast.makeText(DiaryWriteActivity.this, "onDown", Toast.LENGTH_SHORT).show();
            return false;
        }

        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
            if(confirm.getVisibility() == View.INVISIBLE && emptyImage.getVisibility() == View.INVISIBLE){
                //Toast.makeText(DiaryWriteActivity.this, "onFling", Toast.LENGTH_LONG).show();
                if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    // Fling left
                    //Log.i("MyGesture", "Fling left");
                    //Toast.makeText(DiaryWriteActivity.this, "Fling Left", Toast.LENGTH_SHORT).show();
                    if(index == diaryList.size()-1){
                        Toast.makeText(DiaryWriteActivity.this, "这已经是最新的日记啦",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        index = index + 1;
                        diary = diaryList.get(index);
                        String date = (diary.getDate().getYear()+1900)+"年"+(diary.getDate().getMonth()+1)+"月"+diary.getDate().getDate()+"日";
                        diaryDate.setText(date);
                        diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                        diary_write.setText(Html.fromHtml(diary.getHtmlText()));
                        getImage(diary.getText());
                        setTextFormmat(diary);
                        getLabelsOfDiary(diary,helper);
                    }
                    getImage(diary.getText());
                }
                else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                    // Fling right
                    //Log.i("MyGesture", "Fling right");
                    //Toast.makeText(DiaryWriteActivity.this, "Fling Right", Toast.LENGTH_SHORT).show();
                    //右滑 上一篇
                    if(index == 0){
                        Toast.makeText(DiaryWriteActivity.this, "没有更早的日记了哦",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        index = index -1;
                        diary = diaryList.get(index);
                        String date = (diary.getDate().getYear()+1900)+"年"+(diary.getDate().getMonth()+1)+"月"+diary.getDate().getDate()+"日";
                        diaryDate.setText(date);
                        diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                        diary_write.setText(Html.fromHtml(diary.getHtmlText()));
                        getImage(diary.getText());
                        setTextFormmat(diary);
                        getLabelsOfDiary(diary,helper);
                    }
                    getImage(diary.getText());
                }
                return true;
            }
            return false;
        }
    }

    public void initViewPage(){
//        vp = (ViewPager)findViewById(R.id.diaryViewPager);
//        diaryFragment = new DiaryFragment();
//        preDiaryFragment = new DiaryFragment();
//        nextDiaryFragment = new DiaryFragment();
//        mFragmentList.add(preDiaryFragment);
//        mFragmentList.add(diaryFragment);
//        mFragmentList.add(nextDiaryFragment);
//        //vp.addOnPageChangeListener(this);
//        mFragmentAdapter = new DiaryFragmentAdapter(getSupportFragmentManager(),mFragmentList);
//        vp.setOffscreenPageLimit(3);
//        vp.setAdapter(mFragmentAdapter);
//        vp.setCurrentItem(1);
    }
    public void getView()
    {
        floatingButtons = (ConstraintLayout)findViewById(R.id.floating_buttons);
        addDiary = (FloatingActionButton)findViewById(R.id.add_diary);
        add = (FloatingActionButton)findViewById(R.id.add);
        enterBottle = (FloatingActionButton)findViewById(R.id.enterBottle);
        like = (FloatingActionButton)findViewById(R.id.like);
        edit = (FloatingActionButton)findViewById(R.id.edit);
        diary_write = (EditText) findViewById(R.id.diaryWrite);
        edit_layout = (LinearLayout) findViewById(R.id.edit_layout);
        date_layout = (LinearLayout) findViewById(R.id.date_layout);
        set_layout = (LinearLayout)findViewById(R.id.settings_column);
        set_size = (DiscreteSeekBar) findViewById(R.id.set_size);
        confirm = (ImageButton) findViewById(R.id.confirm);
        font_set = (ImageButton) findViewById(R.id.font_setting);
        font_setting_bottom_sheet =  BottomSheetBehavior.from(findViewById(R.id.fontSettingBottomSheetLayout));
        set_font1 = (TextView) findViewById(R.id.font1);
        set_font2 = (TextView) findViewById(R.id.font2);
        set_font3 = (TextView) findViewById(R.id.font3);
        set_center = (ImageButton) findViewById(R.id.set_center);
        set_left = (ImageButton) findViewById(R.id.set_left);
        set_right = (ImageButton) findViewById(R.id.set_right);
        font_padding1 = (TextView) findViewById(R.id.font_padding1);
        font_padding2 = (TextView) findViewById(R.id.font_padding2);
        font_padding3 = (TextView) findViewById(R.id.font_padding3);
        line_spacing1 = (ImageButton) findViewById(R.id.line_spacing1);
        line_spacing2 = (ImageButton) findViewById(R.id.line_spacing2);
        line_spacing3 = (ImageButton) findViewById(R.id.line_spacing3);
        set_underline = (ImageButton) findViewById(R.id.underline);
        set_bold = (ImageButton) findViewById(R.id.bold);
        set_italic = (ImageButton) findViewById(R.id.italic);
        insert_image = (ImageButton) findViewById(R.id.insert_image);
        diaryIcon = (ImageView) findViewById(R.id.diary_content_icon);
        diaryIcon1 = (ImageView) findViewById(R.id.diary_content_icon1);
        diaryIcon2 = (ImageView) findViewById(R.id.diary_content_icon2);
        diaryIcon3 = (ImageView) findViewById(R.id.diary_content_icon3);
        diaryIcon4 = (ImageView) findViewById(R.id.diary_content_icon4);
        emptyImage = (ImageView) findViewById(R.id.empty_image);
    }

    public void setOnListener()
    {
        //ViewPager的监听事件
//        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                /*此方法在页面被选中时调用*/
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
//                arg0 ==1的时辰默示正在滑动，
//                arg0==2的时辰默示滑动完毕了，
//                arg0==0的时辰默示什么都没做。*/
//            }
//        });

        diary_write.addTextChangedListener(new TextWatcher() {
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                start=i;
                count=i2;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editchange(editable);
            }

        });
        edit_layout.setOnClickListener(this);
        set_size.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                diary_write.setTextSize(4*set_size.getProgress());
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });
        add.setOnClickListener(this);
        addDiary.setOnClickListener(this);
        enterBottle.setOnClickListener(this);
        like.setOnClickListener(this);
        edit.setOnClickListener(this);
        confirm.setOnClickListener(this);
        font_set.setOnClickListener(this);
        set_font1.setOnClickListener(this);
        set_font2.setOnClickListener(this);
        set_font3.setOnClickListener(this);
        set_center.setOnClickListener(this);
        set_left.setOnClickListener(this);
        set_right.setOnClickListener(this);
        font_padding1.setOnClickListener(this);
        font_padding2.setOnClickListener(this);
        font_padding3.setOnClickListener(this);
        line_spacing1.setOnClickListener(this);
        line_spacing2.setOnClickListener(this);
        line_spacing3.setOnClickListener(this);
        set_underline.setOnClickListener(this);
        set_bold.setOnClickListener(this);
        set_italic.setOnClickListener(this);
        insert_image.setOnClickListener(this);
        diaryIcon.setOnClickListener(this);
        diaryIcon1.setOnLongClickListener(this);
        diaryIcon1.setOnLongClickListener(this);
        diaryIcon2.setOnLongClickListener(this);
        diaryIcon3.setOnLongClickListener(this);
        diaryIcon4.setOnLongClickListener(this);
    }

    public void init()
    {
        imageItems.add(diaryIcon1);
        imageItems.add(diaryIcon2);
        imageItems.add(diaryIcon3);
        imageItems.add(diaryIcon4);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_diary_content);
        toolbar.setTitle("心迹");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.nav_view);
        diaryDate = (TextView)findViewById(R.id.diary_content_date);
        diaryWeekday = (TextView)findViewById(R.id.diary_content_weekday);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }
        //navView.setCheckedItem(R.id.favorite);
        //navView.setItemIconTintList(null);
        Intent intent = getIntent();
        diaryList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        originType = intent.getStringExtra("diary_origin");
        if(originType.equals("welcome")){
            diaryList = Diary.getAll(helper,true);
            if(diaryList.size() == 0){

            }
            else{
                index = diaryList.size()-1;
                diary = diaryList.get(index);
            }
        }
        else if(originType.equals("diary")){
            diaryList = Diary.getAll(helper,true);
            //diary = (Diary) intent.getSerializableExtra("diary_list");
            index = intent.getIntExtra("diary_index",diaryList.size());
            diary = diaryList.get(index);
            //labelList = Label.getAllLabel(helper);
        }
        else if(originType.equals("add_diary")){
            diaryList = Diary.getAll(helper,true);
            index = intent.getIntExtra("diary_index",diaryList.size());
        }
        else if(originType.equals("search")) {
            // Get the Bundle Object
           Bundle bundleObject = intent.getExtras();
//            // Get ArrayList Bundle
           diaryList = (ArrayList<Diary>) bundleObject.getSerializable("search_list");
            Collections.reverse(diaryList);
           diary = (Diary) intent.getSerializableExtra("diary_list");
           index = intent.getIntExtra("diary_index",diaryList.size());
           diary = diaryList.get(index);
        }

        if(originType.equals("add_diary")){
            Date date = new Date();
            String today = (date.getYear()+1900)+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日";
            diaryDate.setText(today);
            diaryWeekday.setText(weekList.get(date.getDay()));
            //diaryIcon.setImageDrawable(setTag(tagId));
            actionBar.hide();

            floatingButtons.setVisibility(View.INVISIBLE);
            emptyImage.setVisibility(View.INVISIBLE);

            initTextFormmat();
        }
        else if(diaryList.size() == 0){
            emptyImage.setVisibility(View.VISIBLE);
            font_set.setVisibility(View.INVISIBLE);
            insert_image.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);

            diary_write.setText("");
            diaryDate.setText("");
            diaryWeekday.setText("");
            diary_write.setEnabled(false);
            floatingButtons.setVisibility(View.VISIBLE);
            addDiary.setVisibility(View.INVISIBLE);
            enterBottle.setVisibility(View.INVISIBLE);
            like.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);

        }
        else {
            Log.i("show",diary.getHtmlText());
            diary_write.setText(Html.fromHtml(diary.getHtmlText()));
            getImage(diary.getText());
            setTextFormmat(diary);
            getLabelsOfDiary(diary,helper);
            String date = (diary.getDate().getYear()+1900)+"年"+(diary.getDate().getMonth()+1)+"月"+diary.getDate().getDate()+"日";
            diaryDate.setText(date);
            diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
            diary_write.setEnabled(false);
            font_set.setVisibility(View.INVISIBLE);
            insert_image.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);

            emptyImage.setVisibility(View.INVISIBLE);
            floatingButtons.setVisibility(View.VISIBLE);
            addDiary.setVisibility(View.INVISIBLE);
            enterBottle.setVisibility(View.INVISIBLE);
            like.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);

            diary_write.setSelection(diary_write.getText().length());
        }
//

    }

    public void reStartInit(){
        imageItems.add(diaryIcon1);
        imageItems.add(diaryIcon2);
        imageItems.add(diaryIcon3);
        imageItems.add(diaryIcon4);

        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        if(originType.equals("welcome")){
            diaryList = Diary.getAll(helper,true);
            if(diaryList.size() == 0){

            }
            else{
                index = diaryList.size()-1;
                diary = diaryList.get(index);
            }
        }
        else if(originType.equals("diary")){
            diaryList = Diary.getAll(helper,true);
            //diary = (Diary) intent.getSerializableExtra("diary_list");
            diary = diaryList.get(index);
            //labelList = Label.getAllLabel(helper);
        }
        else if(originType.equals("add_diary")){
            diaryList = Diary.getAll(helper,true);
        }
        else if(originType.equals("search")) {
            // Get the Bundle Object

            Bundle bundleObject = getIntent().getExtras();
//            // Get ArrayList Bundle
            diaryList = (ArrayList<Diary>) bundleObject.getSerializable("search_list");
            Collections.reverse(diaryList);
            diary = diaryList.get(index);
        }

        if(originType.equals("add_diary")){
            Date date = new Date();
            String today = (date.getYear()+1900)+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日";
            diaryDate.setText(today);
            diaryWeekday.setText(weekList.get(date.getDay()));
            //diaryIcon.setImageDrawable(setTag(tagId));
            actionBar.hide();

            floatingButtons.setVisibility(View.INVISIBLE);
            emptyImage.setVisibility(View.INVISIBLE);

            initTextFormmat();
        }
        else if(diaryList.size() == 0){
            emptyImage.setVisibility(View.VISIBLE);
            font_set.setVisibility(View.INVISIBLE);
            insert_image.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);

            diary_write.setText("");
            diaryDate.setText("");
            diaryWeekday.setText("");
            diary_write.setEnabled(false);
            floatingButtons.setVisibility(View.VISIBLE);
            addDiary.setVisibility(View.INVISIBLE);
            enterBottle.setVisibility(View.INVISIBLE);
            like.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
        }
        else {
            Log.i("show",diary.getHtmlText());
            diary_write.setText(Html.fromHtml(diary.getHtmlText()));
            getImage(diary.getText());
            setTextFormmat(diary);
            getLabelsOfDiary(diary,helper);
            String date = (diary.getDate().getYear()+1900)+"年"+(diary.getDate().getMonth()+1)+"月"+diary.getDate().getDate()+"日";
            diaryDate.setText(date);
            diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
            diary_write.setEnabled(false);
            font_set.setVisibility(View.INVISIBLE);
            insert_image.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);

            emptyImage.setVisibility(View.INVISIBLE);
            floatingButtons.setVisibility(View.VISIBLE);
            addDiary.setVisibility(View.INVISIBLE);
            enterBottle.setVisibility(View.INVISIBLE);
            like.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);

            diary_write.setSelection(diary_write.getText().length());
        }
//
    }

    private void initTextFormmat()
    {
        diary_write.setTextSize(20);
        diary_write.setLineSpacing(0,1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            diary_write.setLetterSpacing((float) 0.2);
        }
    }

    private void setTextFormmat(Diary diary)
    {
        diary_write.setTextSize(diary.getTextSize());
        diary_write.setLineSpacing(diary.getLineSpacingExtra(),diary.getLineSpacingMultiplier());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            diary_write.setLetterSpacing(diary.getLetterSpacing());
        }
    }

    public void onClick(View view) {
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        switch (view.getId()) {
            case R.id.diary_content_icon:
                for(int i=0;i<imageItems.size();i++) {
                    int radius = 90;
                    float distanceX = (float) (flag * radius * (i + 1));
                    objAnimatorX = ObjectAnimator.ofFloat(imageItems.get(i), "x", imageItems.get(i).getX(), imageItems.get(i).getX() + distanceX);
                    objAnimatorX.setDuration(120);
                    objAnimatorX.setStartDelay(50);
                    objAnimatorX.start();
                    if(flag == 1) imageItems.get(i).setVisibility(View.VISIBLE);
                    else if(flag == -1) imageItems.get(i).setVisibility(View.INVISIBLE);
                }
                flag = -flag;
                break;
            case R.id.add:
                if(addDiary.getVisibility() == View.INVISIBLE) {
                    if(emptyImage.getVisibility() == View.VISIBLE || !diary.getLike()){
                        like.setImageResource(R.drawable.unlike);
                    }
                    else{
                        like.setImageResource(R.drawable.like);
                    }
                    addDiary.setVisibility(View.VISIBLE);
                    enterBottle.setVisibility(View.VISIBLE);
                    like.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                }
                else {
                    addDiary.setVisibility(View.INVISIBLE);
                    enterBottle.setVisibility(View.INVISIBLE);
                    like.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.enterBottle:
                Intent intent = new Intent(DiaryWriteActivity.this, BottlesActivity.class);
                startActivity(intent);
                break;
            case R.id.edit:
                if(emptyImage.getVisibility() == View.INVISIBLE){
                    diary_write.setEnabled(true);
                    font_set.setVisibility(View.VISIBLE);
                    insert_image.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.VISIBLE);
                    emptyImage.setVisibility(View.INVISIBLE);
                    floatingButtons.setVisibility(View.INVISIBLE);
                    actionBar.hide();
                }
                break;
            case R.id.like:
                if(emptyImage.getVisibility() == View.INVISIBLE){
                    if(diary.getLike()) {
                        like.setImageResource(R.drawable.unlike);
                        diary.setLike(false);
                        diary.update(helper);
                    }
                    else{
                        like.setImageResource(R.drawable.like);
                        diary.setLike(true);
                        diary.update(helper);
                    }
                }
                break;
            case R.id.add_diary:
                Intent intent1 = new Intent(DiaryWriteActivity.this,DiaryWriteActivity.class);
                intent1.putExtra("diary_origin","add_diary");
                startActivity(intent1);
                //finish();
                break;
            case R.id.edit_layout:
                if(confirm.getVisibility()==View.VISIBLE){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        diary_write.requestFocus();
                        imm.showSoftInput(diary_write,0);
                    }
                }
                else{
                    addDiary.setVisibility(View.INVISIBLE);
                    enterBottle.setVisibility(View.INVISIBLE);
                    like.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.confirm:
                String htmlText;
                //Log.i("test", Html.toHtml(diary_write.getText()));
                if(originType.equals("add_diary")){
                    diary = new Diary();
                    diary.setText(diary_write.getText().toString());
                    htmlText = colorSpanAdjust(Html.toHtml(diary_write.getText()));
                    diary.setHtmlText(htmlText);
                    diary.setTextSize(diary_write.getTextSize());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        diary.setLetterSpacing(diary_write.getLetterSpacing());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        diary.setLineSpacingMultiplier((int)diary_write.getLineSpacingMultiplier());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        diary.setLineSpacingExtra((int)diary_write.getLineSpacingExtra());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Log.i("fommat",diary_write.getTextSize()+" "+diary_write.getLetterSpacing()+" "+(int)diary_write.getLineSpacingMultiplier()+" "+(int)diary_write.getLineSpacingExtra());
                    }
                    Date date = new Date();
                    diary.setDate(date);
                    diary.insert(helper);
                    diaryList.add(diary);

                    originType = "welcome";
                }
                else{
                    diary.setText(diary_write.getText().toString());
                    htmlText = colorSpanAdjust(Html.toHtml(diary_write.getText()));
                    diary.setHtmlText(htmlText);
                    diary.setTextSize(diary_write.getTextSize());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        diary.setLetterSpacing(diary_write.getLetterSpacing());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        diary.setLineSpacingMultiplier((int)diary_write.getLineSpacingMultiplier());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        diary.setLineSpacingExtra((int)diary_write.getLineSpacingExtra());
                    }
                    diary.update(helper);
                    diaryList.remove(index);
                    diaryList.add(index,diary);
                }
                //CharSequence charSequence = Html.fromHtml(Html.toHtml(diary_write.getText()));
                //Toast.makeText(DiaryWriteActivity.this,charSequence,Toast.LENGTH_SHORT).show();
                diary_write.setEnabled(false);
                font_set.setVisibility(View.INVISIBLE);
                insert_image.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);
                emptyImage.setVisibility(View.INVISIBLE);
                floatingButtons.setVisibility(View.VISIBLE);
                addDiary.setVisibility(View.INVISIBLE);
                enterBottle.setVisibility(View.INVISIBLE);
                like.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.INVISIBLE);
                actionBar.show();
                break;
            case R.id.font_setting:
                font_setting_bottom_sheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.font1:
                set_font1.setBackgroundColor(Color.GRAY);
                set_font2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                set_font3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new TypefaceSpan("monospace"), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_type = 1;
                break;
            case R.id.font2:
                set_font2.setBackgroundColor(Color.GRAY);
                set_font1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                set_font3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new TypefaceSpan("serif"), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_type = 2;
                break;
            case R.id.font3:
                set_font3.setBackgroundColor(Color.GRAY);
                set_font1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                set_font2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new TypefaceSpan("sans-serif"), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_type = 3;
                break;
            case R.id.set_center:
                Editable editable_align_center = diary_write.getText();
                editable_align_center.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                set_center.setBackgroundColor(Color.GRAY);
                set_right.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                set_left.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                style=2;
                break;
            case R.id.set_left:
                Editable editable_align_left = diary_write.getText();
                editable_align_left.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                set_left.setBackgroundColor(Color.GRAY);
                set_right.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                set_center.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                style=3;
                break;
            case R.id.set_right:
                Editable editable_align_right = diary_write.getText();
                editable_align_right.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                set_right.setBackgroundColor(Color.GRAY);
                set_left.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                set_center.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                style=4;
                break;
            case R.id.font_padding1:
                font_padding1.setBackgroundColor(Color.GRAY);
                font_padding2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                font_padding3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    diary_write.setLetterSpacing(0);
                }
                break;
            case R.id.font_padding2:
                font_padding2.setBackgroundColor(Color.GRAY);
                font_padding1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                font_padding3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    diary_write.setLetterSpacing((float) 0.2);
                }
                break;
            case R.id.font_padding3:
                font_padding3.setBackgroundColor(Color.GRAY);
                font_padding1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                font_padding2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    diary_write.setLetterSpacing((float) 0.5);
                }
                break;
            case R.id.line_spacing1:
                line_spacing1.setBackgroundColor(Color.GRAY);
                line_spacing2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                line_spacing3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                diary_write.setLineSpacing(0,1);
                break;
            case R.id.line_spacing2:
                line_spacing2.setBackgroundColor(Color.GRAY);
                line_spacing1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                line_spacing3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                diary_write.setLineSpacing(5,1);
                break;
            case R.id.line_spacing3:
                line_spacing3.setBackgroundColor(Color.GRAY);
                line_spacing1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                line_spacing2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                diary_write.setLineSpacing(10,1);
                break;
            case R.id.underline:
                is_underline=!is_underline;
                if(is_underline) {
                    if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    {
                        Editable editable = diary_write.getText();
                        editable.setSpan(new UnderlineSpan(), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    set_underline.setBackgroundColor(Color.GRAY);
                }
                else {
                    if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    {
                        Editable editable = diary_write.getText();
                        for (Object o : editable.getSpans(diary_write.getSelectionStart(), diary_write.getSelectionEnd(), UnderlineSpan.class)) {
                            editable.removeSpan(o);
                        }
                    }
                    set_underline.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;
            case R.id.bold:
                is_bold=!is_bold;
                if(is_bold){
                    if (diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    {
                        Editable editable = diary_write.getText();
                        editable.setSpan(new StyleSpan(Typeface.BOLD), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    set_bold.setBackgroundColor(Color.GRAY);
                }
                else {
                    if (diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    {
                        Editable editable = diary_write.getText();
                        StyleSpan[] selectedSpans = editable.getSpans(diary_write.getSelectionStart(), diary_write.getSelectionEnd(), StyleSpan.class);
                        for (StyleSpan style : selectedSpans) {
                            if (style.getStyle() == Typeface.BOLD) {
                                editable.removeSpan(style);
                            }
                        }
                    }
                    set_bold.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;
            case R.id.italic:
                is_italic=!is_italic;
                if(is_italic) {
                    if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    {
                        Editable editable = diary_write.getText();
                        editable.setSpan(new StyleSpan(Typeface.ITALIC), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    set_italic.setBackgroundColor(Color.GRAY);
                }
                else {
                    if (diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    {
                        Editable editable = diary_write.getText();
                        StyleSpan[] selectedSpans = editable.getSpans(diary_write.getSelectionStart(), diary_write.getSelectionEnd(), StyleSpan.class);
                        for (StyleSpan style : selectedSpans) {
                            if (style.getStyle() == Typeface.ITALIC) {
                                editable.removeSpan(style);
                            }
                        }
                    }
                    set_italic.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                break;
            case R.id.insert_image:
                //openAlbum();
                Toast.makeText(this, "插入图片出了点小问题，帅气的kx小哥哥正在努力解决中￣ω￣=", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void editchange(Editable editable)
    {
        switch (font_type)
        {
            case 1:
                editable.setSpan(new TypefaceSpan("monospace"), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 2:
                editable.setSpan(new TypefaceSpan("serif"), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 3:
                editable.setSpan(new TypefaceSpan("sans-serif"), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
        }

        switch (style)
        {
            case 1: //缩进
                if(is_retract)
                {
                    editable.append(" ")
                            .append(editable.toString())
                            .append(editable.toString())
                            .append(editable.toString());
                    editable.setSpan(new LeadingMarginSpan.Standard(10, 0), 0, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                {
                    editable.append(" ")
                            .append(editable.toString())
                            .append(editable.toString())
                            .append(editable.toString());
                    editable.setSpan(new LeadingMarginSpan.Standard(0, 0), 0, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                break;
        }
        if(is_underline)
            editable.setSpan(new UnderlineSpan(), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(is_bold){
            editable.setSpan(new StyleSpan(Typeface.BOLD), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(is_italic) {
            editable.setSpan(new StyleSpan(Typeface.ITALIC), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.label_manage:
                if(emptyImage.getVisibility() == View.INVISIBLE){
                    showDialog(ICON_LIST_DIALOG);
                }
                break;
            case R.id.search:
                //Toast.makeText(this, "搜索你的日记", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DiaryWriteActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.delete:
                if(emptyImage.getVisibility() == View.INVISIBLE) {
                    //Toast.makeText(DiaryActivity.this, "删除这一篇日记",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DiaryWriteActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("你确定要删除你的日记吗？");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                            diary.delete(helper);
                            diaryList.remove(diary);
                            if (diaryList.size() == 0) {
                                emptyImage.setVisibility(View.VISIBLE);
                                font_set.setVisibility(View.INVISIBLE);
                                insert_image.setVisibility(View.INVISIBLE);
                                confirm.setVisibility(View.INVISIBLE);
                                addDiary.setVisibility(View.INVISIBLE);
                                enterBottle.setVisibility(View.INVISIBLE);
                                like.setVisibility(View.INVISIBLE);
                                edit.setVisibility(View.INVISIBLE);
                                diary_write.setText("");
                                diaryDate.setText("");
                                diaryWeekday.setText("");
                                getLabelsOfDiary(new Diary(), helper);
                            } else if (index == diaryList.size()) {
                                index = index - 1;
                                diary = diaryList.get(index);
                                String date = (diary.getDate().getYear() + 1900) + "年" + (diary.getDate().getMonth() + 1) + "月" + diary.getDate().getDate() + "日";
                                diaryDate.setText(date);
                                diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                                diary_write.setText(diary.getText());
                                getLabelsOfDiary(diary, helper);
                            } else {
                                diary = diaryList.get(index);
                                String date = (diary.getDate().getYear() + 1900) + "年" + (diary.getDate().getMonth() + 1) + "月" + diary.getDate().getDate() + "日";
                                diaryDate.setText(date);
                                diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                                diary_write.setText(diary.getText());
                                getLabelsOfDiary(diary, helper);
                            }
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
                break;
            default:
        }
        return true;
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
        Uri uri = data.getData();
        Bitmap bitmap = null;
        try {
            bitmap = getBitmapFromUri(DiaryWriteActivity.this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imagePath = saveBitmap(DiaryWriteActivity.this,bitmap);
        SpannableString imageSpan = new SpannableString(imagePath);
        imageSpan.setSpan(new ImageSpan(bitmap) , 0, imageSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable editable = diary_write.getText();
        editable.insert(diary_write.getSelectionStart(), imageSpan);
        diary_write.setText(editable);
        diary_write.append("\n");
    }

    public static Bitmap getBitmapFromUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "/HeartTrace/pic/";
    private static final String IN_PATH = "/HeartTrace/pic/";

    private static String generateFileName() {
        SimpleDateFormat time_format=new SimpleDateFormat("yyyyMMddHHmmss");
        String date=time_format.format(new Date());
        return "image_"+date;
    }

    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
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
            //picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void getImage(String text)
    {
        Pattern pattern = Pattern.compile( Environment.getExternalStorageDirectory().getPath()+"/HeartTrace/pic/image_[0-9]{14}\\.jpg");
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()) {
            Log.i("test","matched");
            Bitmap bitmap = BitmapFactory.decodeFile(matcher.group());
            SpannableString imageSpan = new SpannableString(matcher.group());
            imageSpan.setSpan(new ImageSpan(bitmap) , 0, imageSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Editable editable = diary_write.getText();
            Pattern pattern1 = Pattern.compile("￼");
            Matcher matcher1 = pattern1.matcher(diary_write.getText());
            if(matcher1.find()) {
                editable.delete(matcher1.start(),matcher1.end());
                editable.insert(matcher1.start(), imageSpan);
                Log.i("test","insert");
            }
            diary_write.setText(editable);
        }
    }

    private String colorSpanAdjust(String htmlText)
    {
        int i = 0;
        Pattern pattern = Pattern.compile( "font color =\"#[0-9a-f]{6}\"");
        Matcher matcher = pattern.matcher(htmlText);
        while(matcher.find()) {
            StringBuilder sb=new StringBuilder(htmlText);
            sb.insert(matcher.start() + 12 + 2*i,"\\");
            sb.insert(matcher.start() + 21 + 2*i, "\\");
            htmlText = sb.toString();
            i++;
            Log.i("htmlText",htmlText);
        }
        return htmlText;
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
            case ICON_LIST_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.mood);
                builder.setTitle("给日记贴个标签吧!");
                BaseAdapter adapter = new ListItemAdapter();
                DialogInterface.OnClickListener listener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                labelProperty = false;
                                isExisted = false;
                                if(which >= 0 && which <= 5) labelProperty = true;
                                switch (which){
                                    case 0: label_name = "happy";break;
                                    case 1: label_name = "normal";break;
                                    case 2: label_name = "sad";break;
                                    case 3: label_name = "embarrassed";break;
                                    case 4: label_name = "shocked";break;
                                    case 5: label_name = "foolish";break;
                                    case 6: label_name = "travel";break;
                                    case 7: label_name = "work";break;
                                    case 8: label_name = "study";break;
                                    case 9: label_name = "entertainment";break;
                                    case 10: label_name = "love";break;
                                }
                                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

                                Label label = Label.getByName(helper,label_name);
                                if(label == null) {
                                    label = new Label(label_name);
                                    label.insert(helper);
                                }

                                label_this = null;
                                try {
                                    label_this = diary.getAllLabel(helper);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                if(label_this != null && label_this.size()!= 0 && label_this.get(0).getLabelname() != null)
                                {
                                    if(labelProperty){
                                        for(Label i:label_this)
                                        {
                                            if(isMoodLabel(i.getLabelname()))
                                                diary.deleteLabel(helper, i);
                                        }
                                    }
                                    else{
                                        for(Label i:label_this)
                                        {
                                            if(i.getLabelname().equals(label_name))
                                                isExisted = true;
                                        }
                                    }
                                }
                                if(!isExisted) diary.insertLabel(helper, label);
                                getLabelsOfDiary(diary,helper);

                            }
                        };
                builder.setAdapter(adapter, listener);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    @Override
    public boolean onLongClick(View view) {
        final DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

        label_this = null;
        try {
            label_this = diary.getAllLabel(helper);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(label_this != null && label_this.size()!= 0 && label_this.get(0).getLabelname() != null)
        {
            labelSize = label_this.size();
            switch (view.getId()) {
                case R.id.diary_content_icon:
                    if(labelSize >= 1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setTitle("提示");
                        dialog.setMessage("你确定要删除此标签吗？");
                        dialog.setCancelable(true);

                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                diary.deleteLabel(helper, label_this.get(labelSize - 1));
                                getLabelsOfDiary(diary, helper);
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                    break;
                case R.id.diary_content_icon1:
                    if(labelSize >= 2){
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                        dialog1.setTitle("提示");
                        dialog1.setMessage("你确定要删除此标签吗？");
                        dialog1.setCancelable(true);

                        dialog1.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                diary.deleteLabel(helper, label_this.get(labelSize - 2));
                                getLabelsOfDiary(diary,helper);
                            }
                        });
                        dialog1.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });
                        dialog1.show();
                    }
                    break;
                case R.id.diary_content_icon2:
                    if(labelSize >= 3){
                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                        dialog2.setTitle("提示");
                        dialog2.setMessage("你确定要删除此标签吗？");
                        dialog2.setCancelable(true);

                        dialog2.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                diary.deleteLabel(helper, label_this.get(labelSize - 3));
                                getLabelsOfDiary(diary,helper);
                            }
                        });
                        dialog2.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });
                        dialog2.show();
                    }
                    break;
                case R.id.diary_content_icon3:
                    if(labelSize >= 4){
                        AlertDialog.Builder dialog3 = new AlertDialog.Builder(this);
                        dialog3.setTitle("提示");
                        dialog3.setMessage("你确定要删除此标签吗？");
                        dialog3.setCancelable(true);

                        dialog3.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                diary.deleteLabel(helper, label_this.get(labelSize - 4));
                                getLabelsOfDiary(diary,helper);
                            }
                        });
                        dialog3.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });
                        dialog3.show();
                    }
                    break;
                case R.id.diary_content_icon4:
                    if(labelSize >= 5){
                        AlertDialog.Builder dialog4 = new AlertDialog.Builder(this);
                        dialog4.setTitle("提示");
                        dialog4.setMessage("你确定要删除此标签吗？");
                        dialog4.setCancelable(true);

                        dialog4.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                diary.deleteLabel(helper, label_this.get(labelSize - 5));
                                getLabelsOfDiary(diary,helper);
                            }
                        });
                        dialog4.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });
                        dialog4.show();
                    }
                    break;

            }
        }

        return false;
    }

    class ListItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imgIds.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position,
                            View contentView, ViewGroup parent) {
            TextView textView =
                    new TextView(DiaryWriteActivity.this);
            //获得array.xml中的数组资源getStringArray返回的是一个String数组
            String text = getResources().getStringArray(R.array.mood)[position];
            textView.setText(text);
            //设置字体大小
            textView.setTextSize(25);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            //设置水平方向上居中
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setMinHeight(70);
            //设置文字颜色
            textView.setTextColor(Color.BLACK);
            //设置图标在文字的左边
            textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position], 0, 0, 0);
            //设置textView的左上右下的padding大小
            textView.setPadding(80, 10, 20, 15);
            //设置文字和图标之间的padding大小
            textView.setCompoundDrawablePadding(30);
            return textView;
        }
    }

    public Drawable setTags(String id){
        switch(id)
        {
            case "happy":
                return (getResources().getDrawable(R.drawable.happy));
            case "normal":
                return (getResources().getDrawable(R.drawable.normal));
            case "sad":
                return (getResources().getDrawable(R.drawable.sad));
            case "embarrassed":
                return (getResources().getDrawable(R.drawable.embarrassed));
            case "shocked":
                return (getResources().getDrawable(R.drawable.shocked));
            case "foolish":
                return (getResources().getDrawable(R.drawable.foolish));
            case "travel":
                return (getResources().getDrawable(R.drawable.travel));
            case "work":
                return (getResources().getDrawable(R.drawable.work));
            case "study":
                return (getResources().getDrawable(R.drawable.study));
            case "entertainment":
                return (getResources().getDrawable(R.drawable.entertainment));
            case "love":
                return (getResources().getDrawable(R.drawable.love));
            default:
                return (getResources().getDrawable(R.drawable.normal));
        }
    }

    public boolean isMoodLabel(String id){
        switch(id)
        {
            case "happy": return true;
            case "normal": return true;
            case "sad": return true;
            case "embarrassed": return true;
            case "shocked": return true;
            case "foolish": return true;
            default: return false;
        }
    }

    public void getLabelsOfDiary(Diary diary, DatabaseHelper helper ){
        diaryIcon.setImageDrawable(getResources().getDrawable(R.color.white));
        for(int i = 0; i<=3; i++){
            imageItems.get(i).setImageDrawable(getResources().getDrawable(R.color.white));
        }
        label_this = null;
        try {
            label_this = diary.getAllLabel(helper);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(label_this  == null || label_this.size() == 0 || label_this.get(0).getLabelname() == null){
            tag = "happy";
        }
        else {
            int length = label_this.size();
            tag = label_this.get(length - 1).getLabelname();
            diaryIcon.setImageDrawable(setTags(tag));
            if (length >= 2 && length <= 5)
                for (int i = 0; i <= length - 2; i++) {
                    tag = label_this.get(i).getLabelname();
                    imageItems.get(length - 2 - i).setImageDrawable(setTags(tag));
                }
            else if (length >= 6)
                for (int i = 0; i <= 3; i++) {
                    tag = label_this.get(length - 2 - i).getLabelname();
                    imageItems.get(i).setImageDrawable(setTags(tag));
                }
        }

    }

}
