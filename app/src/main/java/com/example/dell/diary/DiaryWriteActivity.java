package com.example.dell.diary;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.util.Base64;
import android.util.DisplayMetrics;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.auth.MyAccount;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Label;
import com.example.dell.db.Picture;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.dell.diary.R.drawable.background1;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,View.OnLongClickListener, SwipeRefreshLayout.OnRefreshListener {
//    private ViewPager vp;
//    private DiaryFragment diaryFragment;
//    private DiaryFragment preDiaryFragment;
//    private DiaryFragment nextDiaryFragment;
//    private List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<android.support.v4.app.Fragment>();
//    private DiaryFragmentAdapter mFragmentAdapter;

    private static final String TAG = DiaryWriteActivity.class.getSimpleName();

    public static final int CHOOSE_PHOTO = 2;
    private final int ICON_LIST_DIALOG = 1;
    int tagId = 0;
    public String label_name = "happy";
    private int flag = 1;
    private boolean labelProperty;
    private boolean isExisted;
    private boolean isInsertingImg = false;
    private boolean isFling = false;
    private boolean isDeleting = false;
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
    private android.support.design.widget.CoordinatorLayout drawer;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView diaryDate;
    private TextView diaryWeekday;
    private ImageView diaryIcon;
    private ImageView diaryIcon1;
    private ImageView diaryIcon2;
    private ImageView diaryIcon3;
    private ImageView diaryIcon4;
    private ImageView background1;
    private ImageView background2;
    private ImageView background3;
    private ImageView background4;
    private ImageView background5;
    private ImageView background6;
    private ImageView background7;
    private ImageView background8;
    private EditText diary_write;
    private LinearLayout edit_layout;
    private LinearLayout date_layout;
    private LinearLayout set_layout;
    private ConstraintLayout floatingButtons;
    private NumberPicker set_size;
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
    private ImageButton theme_set;
    private BottomSheetBehavior font_setting_bottom_sheet;
    private BottomSheetBehavior theme_setting_bottom_sheet;
    private TextView set_font1;
    private TextView set_font2;
    private TextView set_font3;
    private ImageButton font_red;
    private ImageButton font_orange;
    private ImageButton font_pink;
    private ImageButton font_green;
    private ImageButton font_blue;
    private ImageButton font_dark_blue;
    private ImageButton font_grey;
    private ImageButton font_black;
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
    private static final int FLING_MIN_DISTANCE = 100;// 移动最小距离
    private static final int FLING_MIN_VELOCITY = 200;// 移动最小速度
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

    private CircleImageView headImage;
    private TextView nickName;
    private TextView personalSignature;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);
        getView();
        setOnListener();
        init();
        initNavHeader();
//        //initViewPage();
        mGestureDetector = new GestureDetector(new MyGestureListener()); //使用派生自OnGestureListener

        edit_layout.setOnTouchListener(this);
        //edit_layout.setFocusable(true);
        //edit_layout.setClickable(true);
        //edit_layout.setLongClickable(true);

        diary_write.setOnTouchListener(this);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.personal_information:
                        Intent intent = new Intent(DiaryWriteActivity.this,PersonalInformationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.favorite:
                        Intent intent0 = new Intent(DiaryWriteActivity.this, CollectActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.statistics:
                        Intent intent1 = new Intent(DiaryWriteActivity.this,StatisticsActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.calendar_search:
                        Intent intent2 = new Intent(DiaryWriteActivity.this,CalendarActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.time_line:
                        Intent intent3 = new Intent(DiaryWriteActivity.this,TimeLineActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.delete_all:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DiaryWriteActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("你确定要删除所有数据吗？");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                                helper.clearAll();
                                index = 0;
                                diaryList.clear();
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

                                Intent intent = new Intent(DiaryWriteActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();

                        break;
                    case R.id.sync_setting:
                        Intent intent4 = new Intent(DiaryWriteActivity.this, SyncActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.exit:
                        MyAccount myAccount = new MyAccount(DiaryWriteActivity.this);
                        myAccount.clearSetting();
                        myAccount.clearUser();
                        Intent intent5 = new Intent(DiaryWriteActivity.this,LoginActivity.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent5);

                }
                return true;
            }
        });
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        addDiary.setVisibility(View.INVISIBLE);
        enterBottle.setVisibility(View.INVISIBLE);
        like.setVisibility(View.INVISIBLE);
        edit.setVisibility(View.INVISIBLE);
        initNavHeader();
    }

    public void initNavHeader(){
        MyAccount myAccount = new MyAccount(this);
        //Log.d("123",myAccount.getNickname());
        nickName.setText(myAccount.getNickname());
        String sig = myAccount.getSignature();
        String imageID = myAccount.getHeadimage();
        if(sig == null){
            personalSignature.setText("一切都在慢慢变好。");
        }
        else{
            personalSignature.setText(sig);
        }
        if(imageID == null){
            headImage.setImageResource(R.drawable.panda);
        }
        else{
            //headImage.setImageResource(imageID);
            //headImage.setImageResource(R.drawable.panda);
            if (imageID != "") {
                byte[] bytes = Base64.decode(imageID.getBytes(), 1);
                //  byte[] bytes =headPic.getBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                headImage.setImageBitmap(bitmap);
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev){
        //让GestureDetector响应触碰事件
        mGestureDetector.onTouchEvent(ev);
        //让Activity响应触碰事件
        super.dispatchTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 1000);
        //刷新
        Intent intent = getIntent();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        originType = intent.getStringExtra("diary_origin");
        if(originType.equals("welcome")) {
            diaryList.clear();
            diaryList = Diary.getAll(helper, true);
            if (diaryList.size() == 0) {

            } else {
                index = diaryList.size() - 1;
                diary = diaryList.get(index);
            }

            if (diaryList.size() == 0) {
                emptyImage.setVisibility(View.VISIBLE);
                theme_set.setVisibility(View.INVISIBLE);
                font_set.setVisibility(View.INVISIBLE);
                insert_image.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);

                diary_write.setText("");
                diaryDate.setText("");
                diaryWeekday.setText("");

                diary_write.setEnabled(false);
                mSwipeLayout.setEnabled(true);
                floatingButtons.setVisibility(View.VISIBLE);
                addDiary.setVisibility(View.INVISIBLE);
                enterBottle.setVisibility(View.INVISIBLE);
                like.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.INVISIBLE);

            } else {
                Log.i("show", diary.getHtmlText());
                if (diary.getHtmlText() != null) {
                    diary_write.setText(Html.fromHtml(
                            diary.getHtmlText(),
                            new Html.ImageGetter() {
                                @Override
                                public Drawable getDrawable(String s) {
                                    Picture picture = new Picture(s);
                                    Bitmap bitmap = picture.getBitmap(DiaryWriteActivity.this);
                                    bitmap = scaledImageForScreen(bitmap);
                                    return new BitmapDrawable(bitmap);
                                }
                            },
                            null
                    ));
                    //clear color span
                    ForegroundColorSpan[] colorSpans = diary_write.getText().getSpans(0, diary_write.length(), ForegroundColorSpan.class);
                    for (int i = 0; i < colorSpans.length; i++)
                        diary_write.getText().removeSpan(colorSpans[i]);
                    //set color span
                    int[] colorSpanInfo = getTextColorInfo(diary.getHtmlText());
                    setColorSpan(colorSpanInfo);
                    Log.i("test", Html.toHtml(diary_write.getText()));

                }
                setTextFormmat(diary);
                diary_write.setSelection(0);

                getLabelsOfDiary(diary, helper);
                String date = (diary.getDate().getYear() + 1900) + "年" + (diary.getDate().getMonth() + 1) + "月" + diary.getDate().getDate() + "日";
                diaryDate.setText(date);
                diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                diary_write.setEnabled(false);
                mSwipeLayout.setEnabled(true);
                theme_set.setVisibility(View.INVISIBLE);
                font_set.setVisibility(View.INVISIBLE);
                insert_image.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);

                emptyImage.setVisibility(View.INVISIBLE);
                floatingButtons.setVisibility(View.VISIBLE);
                addDiary.setVisibility(View.INVISIBLE);
                enterBottle.setVisibility(View.INVISIBLE);
                like.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.INVISIBLE);
            }
            font_red.setSelected(false);
            font_pink.setSelected(false);
            font_orange.setSelected(false);
            font_green.setSelected(false);
            font_blue.setSelected(false);
            font_dark_blue.setSelected(false);
            font_grey.setSelected(false);
            font_black.setSelected(true);

            set_font1.setSelected(false);
            set_font2.setSelected(true);
            set_font3.setSelected(false);

            set_left.setSelected(true);
            set_center.setSelected(false);
            set_right.setSelected(false);

            font_padding1.setSelected(false);
            font_padding2.setSelected(true);
            font_padding3.setSelected(false);

            line_spacing1.setSelected(true);
            line_spacing2.setSelected(false);
            line_spacing3.setSelected(false);

            set_underline.setSelected(false);
            set_bold.setSelected(false);
            set_italic.setSelected(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                set_size.setMinValue(1);
                set_size.setMaxValue(10);
                set_size.setValue(5);
            }
        }
        Toast.makeText(DiaryWriteActivity.this, "刷新", Toast.LENGTH_SHORT).show();
        mSwipeLayout.setEnabled(false);
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
            DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
            if(e2.getY() - e1.getY() > 400 && Math.abs(velocityY) > 100)
                mSwipeLayout.setEnabled(true);
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
                        isFling = true;
                        diary_write.setText(Html.fromHtml(diary.getHtmlText()));
                        getImage(diary.getText());
                        setTextFormmat(diary);

                        //clear color span
                        ForegroundColorSpan[] colorSpans= diary_write.getText().getSpans(0, diary_write.length(), ForegroundColorSpan.class);
                        for(int i = 0; i < colorSpans.length; i++)
                            diary_write.getText().removeSpan(colorSpans[i]);
                        //set color span
                        int[] colorSpanInfo = getTextColorInfo(diary.getHtmlText());
                        setColorSpan(colorSpanInfo);
                        Log.i("test", Html.toHtml(diary_write.getText()));

                        diary_write.setSelection(0);
                        getLabelsOfDiary(diary,helper);
                        isFling = false;
                    }
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
                        isFling = true;
                        diary_write.setText(Html.fromHtml(diary.getHtmlText()));
                        getImage(diary.getText());
                        setTextFormmat(diary);

                        //clear color span
                        ForegroundColorSpan[] colorSpans= diary_write.getText().getSpans(0, diary_write.length(), ForegroundColorSpan.class);
                        for(int i = 0; i < colorSpans.length; i++)
                            diary_write.getText().removeSpan(colorSpans[i]);
                        //set color span
                        int[] colorSpanInfo = getTextColorInfo(diary.getHtmlText());
                        setColorSpan(colorSpanInfo);
                        Log.i("test", Html.toHtml(diary_write.getText()));

                        diary_write.setSelection(0);
                        getLabelsOfDiary(diary,helper);
                        isFling = false;
                    }
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
        mSwipeLayout = (VerticalSwipeRefreshLayout) findViewById(R.id.swipe_container);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.nav_view);
        View headerLayout = navView.getHeaderView(0);
        nickName = (TextView)headerLayout.findViewById(R.id.nick_name);
        personalSignature = (TextView)headerLayout.findViewById(R.id.personal_signature);
        headImage = (CircleImageView)headerLayout.findViewById(R.id.icon_image);
        drawer = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.diaryWriteLayout);
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
        set_size = (NumberPicker) findViewById(R.id.set_size);
        confirm = (ImageButton) findViewById(R.id.confirm);
        font_set = (ImageButton) findViewById(R.id.font_setting);
        theme_set = (ImageButton) findViewById(R.id.theme_setting) ;
        font_setting_bottom_sheet =  BottomSheetBehavior.from(findViewById(R.id.fontSettingBottomSheetLayout));
        theme_setting_bottom_sheet =  BottomSheetBehavior.from(findViewById(R.id.themeSettingBottomSheetLayout));
        set_font1 = (TextView) findViewById(R.id.font1);
        set_font2 = (TextView) findViewById(R.id.font2);
        set_font3 = (TextView) findViewById(R.id.font3);
        font_red = (ImageButton) findViewById(R.id.font_red);
        font_orange = (ImageButton) findViewById(R.id.font_orange);
        font_pink = (ImageButton) findViewById(R.id.font_pink);
        font_green = (ImageButton) findViewById(R.id.font_green);
        font_blue = (ImageButton) findViewById(R.id.font_blue);
        font_dark_blue = (ImageButton) findViewById(R.id.font_dark_blue);
        font_grey = (ImageButton) findViewById(R.id.font_grey);
        font_black = (ImageButton) findViewById(R.id.font_black);
        background1 = (ImageView) findViewById(R.id.background1);
        background2 = (ImageView) findViewById(R.id.background2);
        background3 = (ImageView) findViewById(R.id.background3);
        background4 = (ImageView) findViewById(R.id.background4);
        background5 = (ImageView) findViewById(R.id.background5);
        background6 = (ImageView) findViewById(R.id.background6);
        background7 = (ImageView) findViewById(R.id.background7);
        background8 = (ImageView) findViewById(R.id.background8);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            set_size.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    diary_write.setTextSize(newVal*4);
                }
            });
        }
        mSwipeLayout.setOnRefreshListener(this);
        headImage.setOnClickListener(this);
        nickName.setOnClickListener(this);
        add.setOnClickListener(this);
        addDiary.setOnClickListener(this);
        enterBottle.setOnClickListener(this);
        like.setOnClickListener(this);
        edit.setOnClickListener(this);
        confirm.setOnClickListener(this);
        font_set.setOnClickListener(this);
        theme_set.setOnClickListener(this);
        set_font1.setOnClickListener(this);
        set_font2.setOnClickListener(this);
        set_font3.setOnClickListener(this);
        font_red.setOnClickListener(this);
        font_orange.setOnClickListener(this);
        font_pink.setOnClickListener(this);
        font_green.setOnClickListener(this);
        font_blue.setOnClickListener(this);
        font_dark_blue.setOnClickListener(this);
        font_grey.setOnClickListener(this);
        font_black.setOnClickListener(this);
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
        background1.setOnClickListener(this);
        background2.setOnClickListener(this);
        background3.setOnClickListener(this);
        background4.setOnClickListener(this);
        background5.setOnClickListener(this);
        background6.setOnClickListener(this);
        background7.setOnClickListener(this);
        background8.setOnClickListener(this);
    }

    public void init()
    {
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setEnabled(false);
        imageItems.add(diaryIcon1);
        imageItems.add(diaryIcon2);
        imageItems.add(diaryIcon3);
        imageItems.add(diaryIcon4);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_diary_content);
        toolbar.setTitle("心·迹");
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
        else if(originType.equals("like")){
            diaryList = Diary.getAllLike(helper, true);
            diary = (Diary) intent.getSerializableExtra("diary_like");
            index = intent.getIntExtra("diarylike_index",diaryList.size());
            diary = diaryList.get(index);
        }

        if(originType.equals("add_diary")){
            mSwipeLayout.setEnabled(false);
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
            theme_set.setVisibility(View.INVISIBLE);
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
            if(diary.getHtmlText() != null) {
                diary_write.setText(Html.fromHtml(diary.getHtmlText()));
                getImage(diary.getText());
                //clear color span
                ForegroundColorSpan[] colorSpans = diary_write.getText().getSpans(0, diary_write.length(), ForegroundColorSpan.class);
                for (int i = 0; i < colorSpans.length; i++)
                    diary_write.getText().removeSpan(colorSpans[i]);
                //set color span
                int[] colorSpanInfo = getTextColorInfo(diary.getHtmlText());
                setColorSpan(colorSpanInfo);
                Log.i("test", Html.toHtml(diary_write.getText()));

            }
            setTextFormmat(diary);
            diary_write.setSelection(0);

            getLabelsOfDiary(diary,helper);
            String date = (diary.getDate().getYear()+1900)+"年"+(diary.getDate().getMonth()+1)+"月"+diary.getDate().getDate()+"日";
            diaryDate.setText(date);
            diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
            diary_write.setEnabled(false);
            theme_set.setVisibility(View.INVISIBLE);
            font_set.setVisibility(View.INVISIBLE);
            insert_image.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);

            emptyImage.setVisibility(View.INVISIBLE);
            floatingButtons.setVisibility(View.VISIBLE);
            addDiary.setVisibility(View.INVISIBLE);
            enterBottle.setVisibility(View.INVISIBLE);
            like.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
        }
        font_red.setSelected(false);
        font_pink.setSelected(false);
        font_orange.setSelected(false);
        font_green.setSelected(false);
        font_blue.setSelected(false);
        font_dark_blue.setSelected(false);
        font_grey.setSelected(false);
        font_black.setSelected(true);

        set_font1.setSelected(false);
        set_font2.setSelected(true);
        set_font3.setSelected(false);

        set_left.setSelected(true);
        set_center.setSelected(false);
        set_right.setSelected(false);

        font_padding1.setSelected(false);
        font_padding2.setSelected(true);
        font_padding3.setSelected(false);

        line_spacing1.setSelected(true);
        line_spacing2.setSelected(false);
        line_spacing3.setSelected(false);

        set_underline.setSelected(false);
        set_bold.setSelected(false);
        set_italic.setSelected(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            set_size.setMinValue(1);
            set_size.setMaxValue(10);
            set_size.setValue(5);
        }

    }

    private void initTextFormmat()
    {
        diary_write.setTextSize((float) 20.0);
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
            case R.id.nick_name:
            case R.id.icon_image:
                Intent intentP = new Intent(DiaryWriteActivity.this,PersonalInformationActivity.class);
                startActivity(intentP);
                break;
            case R.id.diary_content_icon:
                DisplayMetrics displayMetrics1 = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics1);
                int screenWidth = displayMetrics1.widthPixels;
                int screenHeight = displayMetrics1.heightPixels;
                Log.d("debug", "screenWidth = "+screenWidth+"|screenHeight = "+screenHeight);

                for(int i=0;i<imageItems.size();i++) {
                    float radius = (float) screenWidth / 12;
                    float distanceX = (float) (flag * radius * (i + 1));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        objAnimatorX = ObjectAnimator.ofFloat(imageItems.get(i), "x", imageItems.get(i).getX(), imageItems.get(i).getX() + distanceX);
                        objAnimatorX.setDuration(120);
                        objAnimatorX.setStartDelay(50);
                        objAnimatorX.start();
                    }
                    if(flag == 1) imageItems.get(i).setVisibility(View.VISIBLE);
                    else if(flag == -1) imageItems.get(i).setVisibility(View.INVISIBLE);
                }
                flag = -flag;
                break;
            case R.id.add:
                if(addDiary.getVisibility() == View.INVISIBLE) {
                    if(emptyImage.getVisibility() == View.VISIBLE || !diary.getIslike()){
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
                    mSwipeLayout.setEnabled(false);
                    diary_write.setSelection(diary_write.getText().length());
                    theme_set.setVisibility(View.VISIBLE);
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
                    if(diary.getIslike()) {
                        like.setImageResource(R.drawable.unlike);
                        diary.setIslike(false);
                        diary.update(helper);
                    }
                    else{
                        like.setImageResource(R.drawable.like);
                        diary.setIslike(true);
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
            case R.id.diaryWrite:
                if(confirm.getVisibility()==View.VISIBLE){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        diary_write.requestFocus();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                            imm.showSoftInput(diary_write,0);
                        }
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
                DisplayMetrics displayMetrics=new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                if(originType.equals("add_diary")){
                    diary = new Diary();
                    diary.setText(diary_write.getText().toString());
                    //htmlText = colorSpanAdjust(Html.toHtml(diary_write.getText()));
                    diary.setHtmlText(Html.toHtml(diary_write.getText()));
                    diary.setTextSize(diary_write.getTextSize()/displayMetrics.density);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        diary.setLetterSpacing(diary_write.getLetterSpacing());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        diary.setLineSpacingMultiplier((int)diary_write.getLineSpacingMultiplier());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        diary.setLineSpacingExtra((int)diary_write.getLineSpacingExtra());
                    }
                    Date date = new Date();
                    diary.setDate(date);
                    diary.insert(helper);
                    diaryList.add(diary);

                    originType = "welcome";
                }
                else{
                    diary.setText(diary_write.getText().toString());
                    //htmlText = colorSpanAdjust(Html.toHtml(diary_write.getText()));
                    diary.setHtmlText(Html.toHtml(diary_write.getText()));
                    diary.setTextSize(diary_write.getTextSize()/displayMetrics.density);
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
                diary_write.setEnabled(false);
                theme_set.setVisibility(View.INVISIBLE);
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
            case R.id.theme_setting:
                theme_setting_bottom_sheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.background1:
                drawer.setBackgroundResource(R.drawable.background1);
                break;
            case R.id.background2:
                drawer.setBackgroundResource(R.drawable.background2);
                break;
            case R.id.background3:
                drawer.setBackgroundResource(R.drawable.background3);
                break;
            case R.id.background4:
                drawer.setBackgroundResource(R.drawable.background4);
                break;
            case R.id.background5:
                drawer.setBackgroundResource(R.drawable.background5);
                break;
            case R.id.background6:
                drawer.setBackgroundResource(R.drawable.background6);
                break;
            case R.id.background7:
                drawer.setBackgroundResource(R.drawable.background7);
                break;
            case R.id.background8:
                drawer.setBackgroundResource(R.drawable.background8);
                break;
            case R.id.font1:
                if(set_font1.isSelected() == false){
                    set_font1.setSelected(true);
                    set_font2.setSelected(false);
                    set_font3.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new TypefaceSpan("monospace"), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_type = 1;
                break;
            case R.id.font2:
                if(set_font2.isSelected() == false){
                    set_font2.setSelected(true);
                    set_font1.setSelected(false);
                    set_font3.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new TypefaceSpan("serif"), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_type = 2;
                break;
            case R.id.font3:
                if(set_font3.isSelected() == false){
                    set_font3.setSelected(true);
                    set_font1.setSelected(false);
                    set_font2.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new TypefaceSpan("sans-serif"), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_type = 3;
                break;
            case R.id.font_red:
                if(font_red.isSelected() == false){
                    font_red.setSelected(true);
                    font_orange.setSelected(false);
                    font_pink.setSelected(false);
                    font_green.setSelected(false);
                    font_blue.setSelected(false);
                    font_dark_blue.setSelected(false);
                    font_grey.setSelected(false);
                    font_black.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkred)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 1;
                break;
            case R.id.font_orange:
                if(font_orange.isSelected() == false){
                    font_orange.setSelected(true);
                    font_red.setSelected(false);
                    font_pink.setSelected(false);
                    font_green.setSelected(false);
                    font_blue.setSelected(false);
                    font_dark_blue.setSelected(false);
                    font_grey.setSelected(false);
                    font_black.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkorange)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 2;
                break;
            case R.id.font_pink:
                if(font_pink.isSelected() == false){
                    font_pink.setSelected(true);
                    font_red.setSelected(false);
                    font_orange.setSelected(false);
                    font_green.setSelected(false);
                    font_blue.setSelected(false);
                    font_dark_blue.setSelected(false);
                    font_grey.setSelected(false);
                    font_black.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 3;
                break;
            case R.id.font_green:
                if(font_green.isSelected() == false){
                    font_green.setSelected(true);
                    font_red.setSelected(false);
                    font_orange.setSelected(false);
                    font_pink.setSelected(false);
                    font_blue.setSelected(false);
                    font_dark_blue.setSelected(false);
                    font_grey.setSelected(false);
                    font_black.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkgreen)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 4;
                break;
            case R.id.font_blue:
                if(font_blue.isSelected() == false){
                    font_blue.setSelected(true);
                    font_red.setSelected(false);
                    font_orange.setSelected(false);
                    font_pink.setSelected(false);
                    font_green.setSelected(false);
                    font_dark_blue.setSelected(false);
                    font_grey.setSelected(false);
                    font_black.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.deepskyblue)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 5;
                break;
            case R.id.font_dark_blue:
                if(font_dark_blue.isSelected() == false){
                    font_dark_blue.setSelected(true);
                    font_red.setSelected(false);
                    font_orange.setSelected(false);
                    font_pink.setSelected(false);
                    font_green.setSelected(false);
                    font_blue.setSelected(false);
                    font_grey.setSelected(false);
                    font_black.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.steelblue)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 6;
                break;
            case R.id.font_grey:
                if(font_grey.isSelected() == false){
                    font_grey.setSelected(true);
                    font_red.setSelected(false);
                    font_orange.setSelected(false);
                    font_pink.setSelected(false);
                    font_green.setSelected(false);
                    font_blue.setSelected(false);
                    font_dark_blue.setSelected(false);
                    font_black.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dimgrey)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 7;
                break;
            case R.id.font_black:
                if(font_black.isSelected() == false){
                    font_black.setSelected(true);
                    font_red.setSelected(false);
                    font_orange.setSelected(false);
                    font_pink.setSelected(false);
                    font_green.setSelected(false);
                    font_blue.setSelected(false);
                    font_dark_blue.setSelected(false);
                    font_grey.setSelected(false);
                }
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 8;
                break;
            case R.id.set_center:
                if(set_center.isSelected() == false) {
                    set_center.setSelected(true);
                    set_left.setSelected(false);
                    set_right.setSelected(false);
                }
                Editable editable_align_center = diary_write.getText();
                editable_align_center.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                style=2;
                break;
            case R.id.set_left:
                if(set_left.isSelected() == false) {
                    set_left.setSelected(true);
                    set_center.setSelected(false);
                    set_right.setSelected(false);
                }
                Editable editable_align_left = diary_write.getText();
                editable_align_left.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                style=3;
                break;
            case R.id.set_right:
                if(set_right.isSelected() == false) {
                    set_right.setSelected(true);
                    set_left.setSelected(false);
                    set_center.setSelected(false);
                }
                Editable editable_align_right = diary_write.getText();
                editable_align_right.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                style=4;
                break;
            case R.id.font_padding1:
                if(font_padding1.isSelected() == false){
                    font_padding1.setSelected(true);
                    font_padding2.setSelected(false);
                    font_padding3.setSelected(false);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    diary_write.setLetterSpacing(0);
                }
                break;
            case R.id.font_padding2:
                if(font_padding2.isSelected() == false){
                    font_padding2.setSelected(true);
                    font_padding1.setSelected(false);
                    font_padding3.setSelected(false);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    diary_write.setLetterSpacing((float) 0.2);
                }
                break;
            case R.id.font_padding3:
                if(font_padding3.isSelected() == false){
                    font_padding3.setSelected(true);
                    font_padding1.setSelected(false);
                    font_padding2.setSelected(false);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    diary_write.setLetterSpacing((float) 0.5);
                }
                break;
            case R.id.line_spacing1:
                if(line_spacing1.isSelected() == false){
                    line_spacing1.setSelected(true);
                    line_spacing2.setSelected(false);
                    line_spacing3.setSelected(false);
                }
                diary_write.setLineSpacing(0,1);
                break;
            case R.id.line_spacing2:
                if(line_spacing2.isSelected() == false){
                    line_spacing2.setSelected(true);
                    line_spacing1.setSelected(false);
                    line_spacing3.setSelected(false);
                }
                diary_write.setLineSpacing(5,1);
                break;
            case R.id.line_spacing3:
                if(line_spacing3.isSelected() == false){
                    line_spacing3.setSelected(true);
                    line_spacing1.setSelected(false);
                    line_spacing2.setSelected(false);
                }
                diary_write.setLineSpacing(10,1);
                break;
            case R.id.underline:
                is_underline=!is_underline;
                set_underline.setSelected(is_underline);
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    setSelectedUnderstyleStyle();
                break;
            case R.id.bold:
                is_bold=!is_bold;
                set_bold.setSelected(is_bold);
                if (diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    setSelectedBoldStyle();
                break;
            case R.id.italic:
                is_italic=!is_italic;
                set_italic.setSelected(is_italic);
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    setSelectedItalicStyle();
                break;
            case R.id.insert_image:
                verifyStoragePermissions(DiaryWriteActivity.this);
                openAlbum();
                break;
        }
    }

    private void editchange(Editable editable)
    {
        Log.i("test",start+" "+count);
        if(isInsertingImg == false && isFling == false && isDeleting == false) {
            switch (font_color) {
                case 1:
                    ForegroundColorSpan colorSpan_red = new ForegroundColorSpan(getResources().getColor(R.color.darkred));
                    editable.setSpan(colorSpan_red, start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diary_write.setSelection(start + count);
                    break;
                case 2:
                    ForegroundColorSpan colorSpan_orange = new ForegroundColorSpan(getResources().getColor(R.color.darkorange));
                    editable.setSpan(colorSpan_orange, start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diary_write.setSelection(start + count);
                    break;
                case 3:
                    ForegroundColorSpan colorSpan_pink = new ForegroundColorSpan(getResources().getColor(R.color.pink));
                    editable.setSpan(colorSpan_pink, start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diary_write.setSelection(start + count);
                    break;
                case 4:
                    ForegroundColorSpan colorSpan_green = new ForegroundColorSpan(getResources().getColor(R.color.darkgreen));
                    editable.setSpan(colorSpan_green, start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diary_write.setSelection(start + count);
                    break;
                case 5:
                    ForegroundColorSpan colorSpan_blue = new ForegroundColorSpan(getResources().getColor(R.color.deepskyblue));
                    editable.setSpan(colorSpan_blue, start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diary_write.setSelection(start + count);
                    break;
                case 6:
                    ForegroundColorSpan colorSpan_dark_blue = new ForegroundColorSpan(getResources().getColor(R.color.steelblue));
                    editable.setSpan(colorSpan_dark_blue, start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diary_write.setSelection(start + count);
                    break;
                case 7:
                    ForegroundColorSpan colorSpan_grey = new ForegroundColorSpan(getResources().getColor(R.color.dimgrey));
                    editable.setSpan(colorSpan_grey, start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diary_write.setSelection(start + count);
                    break;
                case 8:
                    ForegroundColorSpan colorSpan_black = new ForegroundColorSpan(getResources().getColor(R.color.black));
                    editable.setSpan(colorSpan_black, start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diary_write.setSelection(start + count);
                    break;
            }
            switch (font_type) {
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

            switch (style) {
                case 1: //缩进
                    if (is_retract) {
                        editable.append(" ")
                                .append(editable.toString())
                                .append(editable.toString())
                                .append(editable.toString());
                        editable.setSpan(new LeadingMarginSpan.Standard(10, 0), 0, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        editable.append(" ")
                                .append(editable.toString())
                                .append(editable.toString())
                                .append(editable.toString());
                        editable.setSpan(new LeadingMarginSpan.Standard(0, 0), 0, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    break;
            }
            if (is_underline)
                editable.setSpan(new UnderlineSpan(), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (is_bold) {
                editable.setSpan(new StyleSpan(Typeface.BOLD), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (is_italic) {
                editable.setSpan(new StyleSpan(Typeface.ITALIC), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
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
                                index = index -  1;
                                diary = diaryList.get(index);
                                String date = (diary.getDate().getYear()+1900)+"年"+(diary.getDate().getMonth()+1)+"月"+diary.getDate().getDate()+"日";
                                diaryDate.setText(date);
                                diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                                isDeleting = true;
                                diary_write.setText(Html.fromHtml(diary.getHtmlText()));
                                getImage(diary.getText());

                                //clear color span
                                ForegroundColorSpan[] colorSpans= diary_write.getText().getSpans(0, diary_write.length(), ForegroundColorSpan.class);
                                for(int i = 0; i < colorSpans.length; i++)
                                    diary_write.getText().removeSpan(colorSpans[i]);
                                //set color span
                                int[] colorSpanInfo = getTextColorInfo(diary.getHtmlText());
                                setColorSpan(colorSpanInfo);
                                Log.i("test", Html.toHtml(diary_write.getText()));

                                setTextFormmat(diary);
                                getLabelsOfDiary(diary,helper);
                                isDeleting = false;
                            } else {
                                diary = diaryList.get(index);
                                String date = (diary.getDate().getYear()+1900)+"年"+(diary.getDate().getMonth()+1)+"月"+diary.getDate().getDate()+"日";
                                diaryDate.setText(date);
                                diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                                isDeleting = true;
                                diary_write.setText(Html.fromHtml(diary.getHtmlText()));
                                getImage(diary.getText());
                                setTextFormmat(diary);

                                //clear color span
                                ForegroundColorSpan[] colorSpans= diary_write.getText().getSpans(0, diary_write.length(), ForegroundColorSpan.class);
                                for(int i = 0; i < colorSpans.length; i++)
                                    diary_write.getText().removeSpan(colorSpans[i]);
                                //set color span
                                int[] colorSpanInfo = getTextColorInfo(diary.getHtmlText());
                                setColorSpan(colorSpanInfo);
                                Log.i("test", Html.toHtml(diary_write.getText()));

                                getLabelsOfDiary(diary,helper);
                                isDeleting = false;
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
                //openAlbum();
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
        // 开始
        isInsertingImg = true;

        // 获取并保存bitmap
        Uri uri = data.getData();
        Bitmap bitmap = PicUtils.getBitmap(this, uri);
        if (bitmap == null) {
            Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT);
            return ;
        }
        String imagePath = saveBitmap(bitmap);

        // 新建imageSpan
        SpannableString imageSpan = new SpannableString(imagePath);
        bitmap = scaledImageForScreen(bitmap);
        imageSpan.setSpan(new ImageSpan(bitmap) , 0, imageSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 显示
        Editable editable = diary_write.getText();
        editable.insert(diary_write.getSelectionStart(), imageSpan);
        diary_write.setText(editable);
        diary_write.append("\n");
        diary_write.setSelection(diary_write.getText().length());

        // 结束
        isInsertingImg = false;
    }

    public Bitmap scaledImageForScreen(Bitmap bitmap) {
        // 获取屏幕参数
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthPixels = displayMetrics.widthPixels * 0.8F;
        Log.d(TAG, "scaledImage: widthPixels = " + widthPixels);

        float density = displayMetrics.density;
        Log.d(TAG, "scaledImage: density = " + density);

        return scaledImage(bitmap, widthPixels * density);
    }

    public Bitmap scaledImage(Bitmap image, float targetWidth) {
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

    public String saveBitmap(Bitmap mBitmap) {
        try {
            Picture picture = new Picture();
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            boolean status = picture.saveBitmap(DiaryWriteActivity.this, databaseHelper, mBitmap);
            if (status) return picture.getParentPath() + picture.getFileName();
            else return null;
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
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
        Editable editable = diary_write.getText();
        String filePath = Picture.getParentPath(this);
        Pattern pattern = Pattern.compile( filePath + "img_[0-9]{0,}\\.jpg");
        Matcher matcher = pattern.matcher(text);
        int matcher_end = 0;
        while(matcher.find()) {
            Log.i("file name",matcher.group().substring(filePath.length(),matcher.group().length()));

            Picture picture = new Picture(matcher.group().substring(filePath.length(),matcher.group().length()));
            // 获取原始图像
            Bitmap bitmap = picture.getBitmap(DiaryWriteActivity.this);
            // 获取缩放图像
            bitmap = scaledImageForScreen(bitmap);

            SpannableString imageSpan = new SpannableString(matcher.group());
            imageSpan.setSpan(new ImageSpan(bitmap) , 0, imageSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Pattern pattern1 = Pattern.compile("￼");
            Matcher matcher1 = pattern1.matcher(diary_write.getText());
            if(matcher1.find()) {
                editable.delete(matcher1.start(),matcher1.end());
                editable.insert(matcher1.start(), imageSpan);
            }
            matcher_end += matcher.end();
            String new_text = text.substring(matcher_end,text.length());
            matcher = pattern.matcher(new_text);
        }
        diary_write.setText(editable);
    }

    private int[] getTextColorInfo(String htmlText)
    {
        Editable editable = (Editable) Html.fromHtml(htmlText);
        ForegroundColorSpan[] colorSpans= editable.getSpans(0, editable.length(), ForegroundColorSpan.class);
        int arraySpan[] = new int[diary_write.getText().length()];
        for(int i = 0; i < arraySpan.length ;i++)
            arraySpan[i] = 8;
        for(int i = 0; i < colorSpans.length ; i++) {
            int styleStart = editable.getSpanStart(colorSpans[i]);
            int styleEnd = editable.getSpanEnd(colorSpans[i]);
            if(colorSpans[i].getForegroundColor() == new ForegroundColorSpan(getResources().getColor(R.color.darkred)).getForegroundColor())
                for (int j = styleStart; j < styleEnd ;j++) arraySpan[j] = 1;
            if(colorSpans[i].getForegroundColor() == new ForegroundColorSpan(getResources().getColor(R.color.darkorange)).getForegroundColor())
                for (int j = styleStart; j < styleEnd ;j++) arraySpan[j] = 2;
            if(colorSpans[i].getForegroundColor() == new ForegroundColorSpan(getResources().getColor(R.color.pink)).getForegroundColor())
                for (int j = styleStart; j < styleEnd ;j++) arraySpan[j] = 3;
            if(colorSpans[i].getForegroundColor() == new ForegroundColorSpan(getResources().getColor(R.color.darkgreen)).getForegroundColor())
                for (int j = styleStart; j < styleEnd ;j++) arraySpan[j] = 4;
            if(colorSpans[i].getForegroundColor() == new ForegroundColorSpan(getResources().getColor(R.color.deepskyblue)).getForegroundColor())
                for (int j = styleStart; j < styleEnd ;j++) arraySpan[j] = 5;
            if(colorSpans[i].getForegroundColor() == new ForegroundColorSpan(getResources().getColor(R.color.steelblue)).getForegroundColor())
                for (int j = styleStart; j < styleEnd ;j++) arraySpan[j] = 6;
            if(colorSpans[i].getForegroundColor() == new ForegroundColorSpan(getResources().getColor(R.color.dimgrey)).getForegroundColor())
                for (int j = styleStart; j < styleEnd ;j++) arraySpan[j] = 7;
            if(colorSpans[i].getForegroundColor() == new ForegroundColorSpan(getResources().getColor(R.color.black)).getForegroundColor())
                for (int j = styleStart; j < styleEnd ;j++) arraySpan[j] = 8;
        }

        return arraySpan;
    }

    private void setColorSpan(int [] colorSpanInfo){
        int colorSpanType = 8;
        int colorSpanStart = 0;
        int colorSpanEnd = 0;
        int i = 0;
        Editable editable = diary_write.getText();
        while(i < colorSpanInfo.length) {
            for (; i < colorSpanInfo.length; i++)
                if (colorSpanInfo[i] != 8) break;
            if(i == colorSpanInfo.length) break;
            colorSpanStart = i;
            colorSpanType = colorSpanInfo[i];
            for (; i < colorSpanInfo.length; i++)
                if (colorSpanInfo[i] != colorSpanType) break;
            colorSpanEnd = i;
            switch (colorSpanType){
                case 1:
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkred)), colorSpanStart, colorSpanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case 2:
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkorange)), colorSpanStart, colorSpanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case 3:
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), colorSpanStart, colorSpanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case 4:
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkgreen)), colorSpanStart, colorSpanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case 5:
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.deepskyblue)), colorSpanStart, colorSpanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case 6:
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.steelblue)), colorSpanStart, colorSpanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case 7:
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dimgrey)), colorSpanStart, colorSpanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            }
        }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position], 0, 0, 0);
            }
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

    private boolean isMoodLabel(String id){
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
        diaryIcon.setImageDrawable(getResources().getDrawable(R.drawable.transparent));
        for(int i = 0; i<=3; i++){
            imageItems.get(i).setImageDrawable(getResources().getDrawable(R.drawable.transparent));
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

    private void setSelectedUnderstyleStyle()
    {
        int selectedStart = diary_write.getSelectionStart();
        int selectedEnd = diary_write.getSelectionEnd();
        Editable editableText = diary_write.getText();
        UnderlineSpan[] spans = editableText.getSpans(0, editableText.length(), UnderlineSpan.class);
        int[] arraySpan = new int[editableText.length()];
        for(int i = 0; i < arraySpan.length ;i++)
            arraySpan[i] = 0;
        for(int i = 0; i < spans.length ; i++)
        {
            int styleStart = editableText.getSpanStart(spans[i]);
            int styleEnd = editableText.getSpanEnd(spans[i]);
            for (int j = styleStart; j < styleEnd ;j++)
                arraySpan[j] = 1;

        }
        boolean allZeroSign = false;
        boolean allOneSign = false;
        int judgeNum = 0;
        for (int i = selectedStart; i < selectedEnd; i++)
            judgeNum += arraySpan[i];
        if(judgeNum == 0) allZeroSign = true;
        else if(judgeNum == selectedEnd - selectedStart) allOneSign = true;
        if(allZeroSign == true) {
            if(is_underline)
                editableText.setSpan(new UnderlineSpan(), selectedStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if(allOneSign == true) {
            int i = selectedStart;
            while(i > 0) {
                if (arraySpan[i] == 0) break;
                i--;
            }
            int preStyleStart;
            if(arraySpan[i] == 0) preStyleStart = i + 1;
            else preStyleStart = 0;
            i = selectedEnd;
            while (i < arraySpan.length) {
                if (arraySpan[i] == 0) break;
                i++;
            }
            int nextStyleEnd = i;
            for(int j = 0; j < spans.length ; j++)
            {
                int styleStart = editableText.getSpanStart(spans[j]);
                int styleEnd = editableText.getSpanEnd(spans[j]);
                if(styleStart >= preStyleStart && styleEnd <= nextStyleEnd && !is_underline)
                    editableText.removeSpan(spans[j]);
            }
            if(!is_underline) {
                if(preStyleStart != selectedStart)
                    editableText.setSpan(new UnderlineSpan(), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if(selectedEnd != nextStyleEnd)
                    editableText.setSpan(new UnderlineSpan(), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        else {
            if(arraySpan[selectedStart] == 0 && arraySpan[selectedEnd - 1] == 0) {
                for(int i = 0; i < spans.length; i++)
                {
                    int styleStart = editableText.getSpanStart(spans[i]);
                    int styleEnd = editableText.getSpanEnd(spans[i]);
                    if(styleStart > selectedStart && styleEnd < selectedEnd)
                        editableText.removeSpan(spans[i]);
                }
                if(is_underline)
                    editableText.setSpan(new UnderlineSpan(), selectedStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(arraySpan[selectedStart] == 1 && arraySpan[selectedEnd - 1] == 1){
                int i = selectedStart;
                while(i > 0) {
                    if (arraySpan[i] == 0) break;
                    i--;
                }
                int preStyleStart;
                if(arraySpan[i] == 0) preStyleStart = i + 1;
                else preStyleStart = 0;
                i = selectedEnd;
                while (i < arraySpan.length) {
                    if (arraySpan[i] == 0) break;
                    i++;
                }
                int nextStyleEnd = i;
                Log.i("test", preStyleStart + " " + nextStyleEnd);
                for(int j = 0; j < spans.length; j++)
                {
                    int styleStart = editableText.getSpanStart(spans[j]);
                    int styleEnd = editableText.getSpanEnd(spans[j]);
                    if(styleStart >= preStyleStart && styleEnd <= nextStyleEnd)
                        editableText.removeSpan(spans[j]);
                }
                if(is_underline)
                    editableText.setSpan(new UnderlineSpan(), preStyleStart, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else{
                    if(preStyleStart != selectedStart)
                        editableText.setSpan(new UnderlineSpan(), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if(selectedEnd != nextStyleEnd)
                        editableText.setSpan(new UnderlineSpan(), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            else if(arraySpan[selectedStart] == 0 && arraySpan[selectedEnd - 1] == 1){
                int i = selectedEnd;
                while (i < arraySpan.length) {
                    if (arraySpan[i] == 0) break;
                    i++;
                }
                int nextStyleEnd = i;
                for(int j = 0; j < spans.length; j++)
                {
                    int styleStart = editableText.getSpanStart(spans[j]);
                    int styleEnd = editableText.getSpanEnd(spans[j]);
                    if(styleStart > selectedStart && styleEnd <= nextStyleEnd)
                        editableText.removeSpan(spans[j]);
                }
                if(is_underline)
                    editableText.setSpan(new UnderlineSpan(), selectedStart, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else
                    editableText.setSpan(new UnderlineSpan(), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(arraySpan[selectedStart] == 1 && arraySpan[selectedEnd - 1] == 0){
                int i = selectedStart;
                while(i > 0) {
                    if (arraySpan[i] == 0) break;
                    i--;
                }
                int preStyleStart;
                if(arraySpan[i] == 0) preStyleStart = i + 1;
                else preStyleStart = 0;
                for(int j = 0; j < spans.length; j++)
                {
                    int styleStart = editableText.getSpanStart(spans[j]);
                    int styleEnd = editableText.getSpanEnd(spans[j]);
                    if(styleStart >= preStyleStart && styleEnd < selectedEnd)
                        editableText.removeSpan(spans[j]);
                }
                if(is_underline)
                    editableText.setSpan(new UnderlineSpan(), preStyleStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else
                    editableText.setSpan(new UnderlineSpan(), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void setSelectedBoldStyle()
    {
        int selectedStart = diary_write.getSelectionStart();
        int selectedEnd = diary_write.getSelectionEnd();
        Editable editableText = diary_write.getText();
        StyleSpan[] spans = editableText.getSpans(0, editableText.length(), StyleSpan.class);
        int[] arraySpan = new int[editableText.length()];
        for(int i = 0; i < arraySpan.length ;i++)
            arraySpan[i] = 0;
        for(int i = 0; i < spans.length ; i++)
        {
            if(spans[i].getStyle() == Typeface.BOLD) {
                int styleStart = editableText.getSpanStart(spans[i]);
                int styleEnd = editableText.getSpanEnd(spans[i]);
                for (int j = styleStart; j < styleEnd ;j++)
                    arraySpan[j] = 1;
            }
        }
        boolean allZeroSign = false;
        boolean allOneSign = false;
        int judgeNum = 0;
        for (int i = selectedStart; i < selectedEnd; i++)
            judgeNum += arraySpan[i];
        if(judgeNum == 0) allZeroSign = true;
        else if(judgeNum == selectedEnd - selectedStart) allOneSign = true;
        if(allZeroSign == true) {
            if(is_bold)
                editableText.setSpan(new StyleSpan(Typeface.BOLD), selectedStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if(allOneSign == true) {
            int i = selectedStart;
            while(i > 0) {
                if (arraySpan[i] == 0) break;
                i--;
            }
            int preStyleStart;
            if(arraySpan[i] == 0) preStyleStart = i + 1;
            else preStyleStart = 0;
            i = selectedEnd;
            while (i < arraySpan.length) {
                if (arraySpan[i] == 0) break;
                i++;
            }
            int nextStyleEnd = i;
            for(int j = 0; j < spans.length ; j++)
            {
                if(spans[j].getStyle() == Typeface.BOLD) {
                    int styleStart = editableText.getSpanStart(spans[j]);
                    int styleEnd = editableText.getSpanEnd(spans[j]);
                    if(styleStart >= preStyleStart && styleEnd <= nextStyleEnd && !is_bold)
                        editableText.removeSpan(spans[j]);
                }
            }
            if(!is_bold) {
                if(preStyleStart != selectedStart)
                    editableText.setSpan(new StyleSpan(Typeface.BOLD), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if(selectedEnd != nextStyleEnd)
                    editableText.setSpan(new StyleSpan(Typeface.BOLD), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        else {
            if(arraySpan[selectedStart] == 0 && arraySpan[selectedEnd - 1] == 0) {
                for(int i = 0; i < spans.length; i++)
                {
                    if(spans[i].getStyle() == Typeface.BOLD) {
                        int styleStart = editableText.getSpanStart(spans[i]);
                        int styleEnd = editableText.getSpanEnd(spans[i]);
                        if(styleStart > selectedStart && styleEnd < selectedEnd)
                            editableText.removeSpan(spans[i]);
                    }
                }
                if(is_bold)
                    editableText.setSpan(new StyleSpan(Typeface.BOLD), selectedStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(arraySpan[selectedStart] == 1 && arraySpan[selectedEnd - 1] == 1){
                int i = selectedStart;
                while(i > 0) {
                    if (arraySpan[i] == 0) break;
                    i--;
                }
                int preStyleStart;
                if(arraySpan[i] == 0) preStyleStart = i + 1;
                else preStyleStart = 0;
                i = selectedEnd;
                while (i < arraySpan.length) {
                    if (arraySpan[i] == 0) break;
                    i++;
                }
                int nextStyleEnd = i;
                Log.i("test", preStyleStart + " " + nextStyleEnd);
                for(int j = 0; j < spans.length; j++)
                {
                    if(spans[j].getStyle() == Typeface.BOLD) {
                        int styleStart = editableText.getSpanStart(spans[j]);
                        int styleEnd = editableText.getSpanEnd(spans[j]);
                        if(styleStart >= preStyleStart && styleEnd <= nextStyleEnd)
                            editableText.removeSpan(spans[j]);
                    }
                }
                if(is_bold)
                    editableText.setSpan(new StyleSpan(Typeface.BOLD), preStyleStart, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else{
                    if(preStyleStart != selectedStart)
                        editableText.setSpan(new StyleSpan(Typeface.BOLD), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if(selectedEnd != nextStyleEnd)
                        editableText.setSpan(new StyleSpan(Typeface.BOLD), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            else if(arraySpan[selectedStart] == 0 && arraySpan[selectedEnd - 1] == 1){
                int i = selectedEnd;
                while (i < arraySpan.length) {
                    if (arraySpan[i] == 0) break;
                    i++;
                }
                int nextStyleEnd = i;
                for(int j = 0; j < spans.length; j++)
                {
                    if(spans[j].getStyle() == Typeface.BOLD) {
                        int styleStart = editableText.getSpanStart(spans[j]);
                        int styleEnd = editableText.getSpanEnd(spans[j]);
                        if(styleStart > selectedStart && styleEnd <= nextStyleEnd)
                            editableText.removeSpan(spans[j]);
                    }
                }
                if(is_bold)
                    editableText.setSpan(new StyleSpan(Typeface.BOLD), selectedStart, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else
                    editableText.setSpan(new StyleSpan(Typeface.BOLD), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(arraySpan[selectedStart] == 1 && arraySpan[selectedEnd - 1] == 0){
                int i = selectedStart;
                while(i > 0) {
                    if (arraySpan[i] == 0) break;
                    i--;
                }
                int preStyleStart;
                if(arraySpan[i] == 0) preStyleStart = i + 1;
                else preStyleStart = 0;
                for(int j = 0; j < spans.length; j++)
                {
                    if(spans[j].getStyle() == Typeface.BOLD) {
                        int styleStart = editableText.getSpanStart(spans[j]);
                        int styleEnd = editableText.getSpanEnd(spans[j]);
                        if(styleStart >= preStyleStart && styleEnd < selectedEnd)
                            editableText.removeSpan(spans[j]);
                    }
                }
                if(is_bold)
                    editableText.setSpan(new StyleSpan(Typeface.BOLD), preStyleStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else
                    editableText.setSpan(new StyleSpan(Typeface.BOLD), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void setSelectedItalicStyle()
    {
        int selectedStart = diary_write.getSelectionStart();
        int selectedEnd = diary_write.getSelectionEnd();
        Editable editableText = diary_write.getText();
        StyleSpan[] spans = editableText.getSpans(0, editableText.length(), StyleSpan.class);
        int[] arraySpan = new int[editableText.length()];
        for(int i = 0; i < arraySpan.length ;i++)
            arraySpan[i] = 0;
        for(int i = 0; i < spans.length ; i++)
        {
            if(spans[i].getStyle() == Typeface.ITALIC) {
                int styleStart = editableText.getSpanStart(spans[i]);
                int styleEnd = editableText.getSpanEnd(spans[i]);
                for (int j = styleStart; j < styleEnd ;j++)
                    arraySpan[j] = 1;
            }
        }
        boolean allZeroSign = false;
        boolean allOneSign = false;
        int judgeNum = 0;
        for (int i = selectedStart; i < selectedEnd; i++)
            judgeNum += arraySpan[i];
        if(judgeNum == 0) allZeroSign = true;
        else if(judgeNum == selectedEnd - selectedStart) allOneSign = true;
        if(allZeroSign == true) {
            if(is_italic)
                editableText.setSpan(new StyleSpan(Typeface.ITALIC), selectedStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if(allOneSign == true) {
            int i = selectedStart;
            while(i > 0) {
                if (arraySpan[i] == 0) break;
                i--;
            }
            int preStyleStart;
            if(arraySpan[i] == 0) preStyleStart = i + 1;
            else preStyleStart = 0;
            i = selectedEnd;
            while (i < arraySpan.length) {
                if (arraySpan[i] == 0) break;
                i++;
            }
            int nextStyleEnd = i;
            for(int j = 0; j < spans.length ; j++)
            {
                if(spans[j].getStyle() == Typeface.ITALIC) {
                    int styleStart = editableText.getSpanStart(spans[j]);
                    int styleEnd = editableText.getSpanEnd(spans[j]);
                    if(styleStart >= preStyleStart && styleEnd <= nextStyleEnd && !is_italic)
                        editableText.removeSpan(spans[j]);
                }
            }
            if(!is_italic) {
                if(preStyleStart != selectedStart)
                    editableText.setSpan(new StyleSpan(Typeface.ITALIC), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if(selectedEnd != nextStyleEnd)
                    editableText.setSpan(new StyleSpan(Typeface.ITALIC), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        else {
            if(arraySpan[selectedStart] == 0 && arraySpan[selectedEnd - 1] == 0) {
                for(int i = 0; i < spans.length; i++)
                {
                    if(spans[i].getStyle() == Typeface.ITALIC) {
                        int styleStart = editableText.getSpanStart(spans[i]);
                        int styleEnd = editableText.getSpanEnd(spans[i]);
                        if(styleStart > selectedStart && styleEnd < selectedEnd)
                            editableText.removeSpan(spans[i]);
                    }
                }
                if(is_italic)
                    editableText.setSpan(new StyleSpan(Typeface.ITALIC), selectedStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(arraySpan[selectedStart] == 1 && arraySpan[selectedEnd - 1] == 1){
                int i = selectedStart;
                while(i > 0) {
                    if (arraySpan[i] == 0) break;
                    i--;
                }
                int preStyleStart;
                if(arraySpan[i] == 0) preStyleStart = i + 1;
                else preStyleStart = 0;
                i = selectedEnd;
                while (i < arraySpan.length) {
                    if (arraySpan[i] == 0) break;
                    i++;
                }
                int nextStyleEnd = i;
                Log.i("test", preStyleStart + " " + nextStyleEnd);
                for(int j = 0; j < spans.length; j++)
                {
                    if(spans[j].getStyle() == Typeface.ITALIC) {
                        int styleStart = editableText.getSpanStart(spans[j]);
                        int styleEnd = editableText.getSpanEnd(spans[j]);
                        if(styleStart >= preStyleStart && styleEnd <= nextStyleEnd)
                            editableText.removeSpan(spans[j]);
                    }
                }
                if(is_italic)
                    editableText.setSpan(new StyleSpan(Typeface.ITALIC), preStyleStart, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else{
                    if(preStyleStart != selectedStart)
                        editableText.setSpan(new StyleSpan(Typeface.ITALIC), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if(selectedEnd != nextStyleEnd)
                        editableText.setSpan(new StyleSpan(Typeface.ITALIC), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            else if(arraySpan[selectedStart] == 0 && arraySpan[selectedEnd - 1] == 1){
                int i = selectedEnd;
                while (i < arraySpan.length) {
                    if (arraySpan[i] == 0) break;
                    i++;
                }
                int nextStyleEnd = i;
                for(int j = 0; j < spans.length; j++)
                {
                    if(spans[j].getStyle() == Typeface.ITALIC) {
                        int styleStart = editableText.getSpanStart(spans[j]);
                        int styleEnd = editableText.getSpanEnd(spans[j]);
                        if(styleStart > selectedStart && styleEnd <= nextStyleEnd)
                            editableText.removeSpan(spans[j]);
                    }
                }
                if(is_italic)
                    editableText.setSpan(new StyleSpan(Typeface.ITALIC), selectedStart, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else
                    editableText.setSpan(new StyleSpan(Typeface.ITALIC), selectedEnd, nextStyleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(arraySpan[selectedStart] == 1 && arraySpan[selectedEnd - 1] == 0){
                int i = selectedStart;
                while(i > 0) {
                    if (arraySpan[i] == 0) break;
                    i--;
                }
                int preStyleStart;
                if(arraySpan[i] == 0) preStyleStart = i + 1;
                else preStyleStart = 0;
                for(int j = 0; j < spans.length; j++)
                {
                    if(spans[j].getStyle() == Typeface.ITALIC) {
                        int styleStart = editableText.getSpanStart(spans[j]);
                        int styleEnd = editableText.getSpanEnd(spans[j]);
                        if(styleStart >= preStyleStart && styleEnd < selectedEnd)
                            editableText.removeSpan(spans[j]);
                    }
                }
                if(is_italic)
                    editableText.setSpan(new StyleSpan(Typeface.ITALIC), preStyleStart, selectedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else
                    editableText.setSpan(new StyleSpan(Typeface.ITALIC), preStyleStart, selectedStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

}



class PicUtils {

    final private static String TAG = "PicUtils";

    static Bitmap getBitmap(Activity activity, Uri uri) {
        try {
            Log.d(TAG, "getBitmap");

            // 从选取相册的Activity中返回后
            String imagePath = getFilePathByUri(activity, uri);
            Log.d(TAG, "getBitmap: imagePath = " + imagePath);
            if (imagePath == null) return null;

            // 最宽1000
            float widthLim = 1000;
            Log.d(TAG, "getBitmap: widthLim = " + widthLim);

            // 设置参数
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;              // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
            BitmapFactory.decodeFile(imagePath, options);
            int width = options.outWidth;
            Log.d(TAG, "getBitmap: width = " + width);

            int ratio = ((int)(width / widthLim)) + 1;
            Log.d(TAG, "getBitmap: ratio = " + ratio);

            options.inJustDecodeBounds = false;
            options.inSampleSize = ratio; // 设置为刚才计算的压缩比例
            return BitmapFactory.decodeFile(imagePath, options); // 解码文件
        }
        catch (Exception e) {
            Log.e(TAG, "getBitmap: ", e);
            return null;
        }
    }

    private static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}

