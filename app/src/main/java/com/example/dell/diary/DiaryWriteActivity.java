package com.example.dell.diary;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
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
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CHOOSE_PHOTO = 2;
    private final int ICON_LIST_DIALOG = 1;
    int tagId = 0;
    public String label_name = "happy";
    private int flag = 1;
    private int[] imgIds = {R.drawable.happy_black,
            R.drawable.normal_black, R.drawable.sad_black};
    private String tag= null;
    Diary diary;
    List<Diary> diaryList = new ArrayList<>();
    List<Label> labelList = new ArrayList<>();
    List<Label> label_this = new ArrayList<>();
    private List<ImageView> imageItems = new ArrayList<ImageView>(3);
    int index;
    Button preDiary;
    Button nextDiary;
    ActionBar actionBar;
    private TextView diaryDate;
    private TextView diaryWeekday;
    private ImageView diaryIcon;
    private ImageView diaryIcon1;
    private ImageView diaryIcon2;
    private ImageView diaryIcon3;
    private ImageView diaryIcon4;
    private EditText diary_write;
    private ImageButton more_setting;
    private ImageButton keyboard;
    private ImageButton face_expression;
    private LinearLayout edit_layout;
    private DiscreteSeekBar set_size;
    private ImageButton confirm;
    private int start=0;
    private int count=0;
    private int font_color=8;
    private int font_type=1;
    private int style=0;
    private boolean is_retract=false;
    private boolean is_center=false;
    private boolean is_left=false;
    private boolean is_right=false;
    private boolean is_underline=false;
    private boolean is_bold=false;
    private boolean is_italic=false;
    private ImageButton font_set;
    private BottomSheetBehavior font_setting_bottom_sheet;
    private ImageButton font_red;
    private ImageButton font_orange;
    private ImageButton font_pink;
    private ImageButton font_green;
    private ImageButton font_blue;
    private ImageButton font_dark_blue;
    private ImageButton font_grey;
    private ImageButton font_black;
    private TextView set_font1;
    private TextView set_font2;
    private TextView set_font3;
    private ImageButton set_retract;
    private ImageButton set_center;
    private ImageButton set_left;
    private ImageButton set_right;
    private TextView font_padding1;
    private TextView font_padding2;
    private TextView font_padding3;
    private ImageButton line_spacing1;
    private ImageButton line_spacing2;
    private ImageButton line_spacing3;
    private ImageButton clear_span;
    private ImageButton set_underline;
    private ImageButton set_bold;
    private ImageButton set_italic;
    private ImageButton insert_image;
    private ObjectAnimator objAnimatorX;

    private SpannableStringBuilder spannableString = new SpannableStringBuilder();
    public List<String> weekList = new ArrayList<>(Arrays.asList("周日","周一","周二","周三"," 周四","周五","周六"));

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
        preDiary = (Button)findViewById(R.id.pre_diary);
        nextDiary = (Button)findViewById(R.id.next_diary);
        diary_write = (EditText) findViewById(R.id.diaryWrite);
        more_setting = (ImageButton) findViewById(R.id.more_setting);
        keyboard = (ImageButton) findViewById(R.id.keyboard);
        face_expression = (ImageButton) findViewById(R.id.face_insert);
        edit_layout = (LinearLayout) findViewById(R.id.edit_layout);
        set_size = (DiscreteSeekBar) findViewById(R.id.set_size);
        confirm = (ImageButton) findViewById(R.id.confirm);
        font_set = (ImageButton) findViewById(R.id.font_setting);
        font_setting_bottom_sheet =  BottomSheetBehavior.from(findViewById(R.id.fontSettingBottomSheetLayout));
        font_red = (ImageButton) findViewById(R.id.font_red);
        font_orange = (ImageButton) findViewById(R.id.font_orange);
        font_pink = (ImageButton) findViewById(R.id.font_pink);
        font_green = (ImageButton) findViewById(R.id.font_green);
        font_blue = (ImageButton) findViewById(R.id.font_blue);
        font_dark_blue = (ImageButton) findViewById(R.id.font_dark_blue);
        font_grey = (ImageButton) findViewById(R.id.font_grey);
        font_black = (ImageButton) findViewById(R.id.font_black);
        set_font1 = (TextView) findViewById(R.id.font1);
        set_font2 = (TextView) findViewById(R.id.font2);
        set_font3 = (TextView) findViewById(R.id.font3);
        set_retract = (ImageButton) findViewById(R.id.set_retract);
        set_center = (ImageButton) findViewById(R.id.set_center);
        set_left = (ImageButton) findViewById(R.id.set_left);
        set_right = (ImageButton) findViewById(R.id.set_right);
        font_padding1 = (TextView) findViewById(R.id.font_padding1);
        font_padding2 = (TextView) findViewById(R.id.font_padding2);
        font_padding3 = (TextView) findViewById(R.id.font_padding3);
        line_spacing1 = (ImageButton) findViewById(R.id.line_spacing1);
        line_spacing2 = (ImageButton) findViewById(R.id.line_spacing2);
        line_spacing3 = (ImageButton) findViewById(R.id.line_spacing3);
        clear_span = (ImageButton) findViewById(R.id.clear_span);
        set_underline = (ImageButton) findViewById(R.id.underline);
        set_bold = (ImageButton) findViewById(R.id.bold);
        set_italic = (ImageButton) findViewById(R.id.italic);
        insert_image = (ImageButton) findViewById(R.id.insert_image);
        diaryIcon = (ImageView) findViewById(R.id.diary_content_icon);
        diaryIcon1 = (ImageView) findViewById(R.id.diary_content_icon1);
        diaryIcon2 = (ImageView) findViewById(R.id.diary_content_icon2);
        diaryIcon3 = (ImageView) findViewById(R.id.diary_content_icon3);
        diaryIcon4 = (ImageView) findViewById(R.id.diary_content_icon4);
    }

    public void setOnListener()
    {
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
        confirm.setOnClickListener(this);
        font_set.setOnClickListener(this);
        font_red.setOnClickListener(this);
        font_orange.setOnClickListener(this);
        font_pink.setOnClickListener(this);
        font_green.setOnClickListener(this);
        font_blue.setOnClickListener(this);
        font_dark_blue.setOnClickListener(this);
        font_grey.setOnClickListener(this);
        font_black.setOnClickListener(this);
        set_font1.setOnClickListener(this);
        set_font2.setOnClickListener(this);
        set_font3.setOnClickListener(this);
        set_retract.setOnClickListener(this);
        set_center.setOnClickListener(this);
        set_left.setOnClickListener(this);
        set_right.setOnClickListener(this);
        font_padding1.setOnClickListener(this);
        font_padding2.setOnClickListener(this);
        font_padding3.setOnClickListener(this);
        line_spacing1.setOnClickListener(this);
        line_spacing2.setOnClickListener(this);
        line_spacing3.setOnClickListener(this);
        clear_span.setOnClickListener(this);
        set_underline.setOnClickListener(this);
        set_bold.setOnClickListener(this);
        set_italic.setOnClickListener(this);
        insert_image.setOnClickListener(this);
        preDiary.setOnClickListener(this);
        nextDiary.setOnClickListener(this);
        diaryIcon.setOnClickListener(this);

    }

    public void init()
    {
        imageItems.add(diaryIcon1);
        imageItems.add(diaryIcon2);
        imageItems.add(diaryIcon3);
        imageItems.add(diaryIcon4);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_diary_content);
        diaryDate = (TextView)findViewById(R.id.diary_content_date);
        diaryWeekday = (TextView)findViewById(R.id.diary_content_weekday);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        diary = (Diary) intent.getSerializableExtra("diary_list");
        diaryList.clear();

        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        String originType = intent.getStringExtra("diary_origin");
        if(originType.equals("diary")){
            diaryList = Diary.getAll(helper,true);
            //labelList = Label.getAllLabel(helper);
        }
        else if(originType.equals("search")) {
            // Get the Bundle Object
            Bundle bundleObject = intent.getExtras();
            // Get ArrayList Bundle
            diaryList = (ArrayList<Diary>) bundleObject.getSerializable("search_list");
        }
        index = intent.getIntExtra("diary_index",diaryList.size());
        if(diary == null){
            Date date = new Date();
            String today = (date.getYear()+1900)+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日";
            diaryDate.setText(today);
            diaryWeekday.setText(weekList.get(date.getDay()));
            //diaryIcon.setImageDrawable(setTag(tagId));
            preDiary.setVisibility(View.INVISIBLE);
            nextDiary.setVisibility(View.INVISIBLE);
            actionBar.hide();
        }
        else {
            diary_write.setText(diary.getText());
            label_this = null;
            try {
                label_this = diary.getAllLabel(helper);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(label_this  == null || label_this.size() == 0 || label_this.get(0).getLabelname() == null){
                tag = "happy"; diaryIcon.setImageDrawable(setTags(tag));
            }
            else{
                int length = label_this.size();
                tag = label_this.get(length - 1).getLabelname();
                diaryIcon.setImageDrawable(setTags(tag));
                if(length >= 2 && length <=5)
                    for(int i = 0; i<= length-2; i++){
                        tag = label_this.get(i).getLabelname();
                        imageItems.get(length - 2 - i).setImageDrawable(setTags(tag));
                    }
                else if(length >=6)
                    for(int i = 0; i<=3; i++){
                        tag = label_this.get(length - 2 - i).getLabelname();
                        imageItems.get(i).setImageDrawable(setTags(tag));
                    }
            }

            String date = (diary.getDate().getYear()+1900)+"年"+(diary.getDate().getMonth()+1)+"月"+diary.getDate().getDate()+"日";
            diaryDate.setText(date);
            diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
            diary_write.setEnabled(false);
            more_setting.setVisibility(View.INVISIBLE);
            font_set.setVisibility(View.INVISIBLE);
            face_expression.setVisibility(View.INVISIBLE);
            insert_image.setVisibility(View.INVISIBLE);
            keyboard.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);
        }


        /*
        set_font1.setBackgroundColor(Color.GRAY);
        font_padding1.setBackgroundColor(Color.GRAY);
        line_spacing1.setBackgroundColor(Color.GRAY);
        set_right.setBackgroundColor(Color.GRAY);
        diary_write.setTextSize(20);*/
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

            case R.id.pre_diary:
                if(index == 0){
                    Toast.makeText(DiaryWriteActivity.this, "没有更早的日记了哦",Toast.LENGTH_SHORT).show();
                }
                else{
                    index = index -1;
                    diary = diaryList.get(index);
                    String date = (diary.getDate().getYear())+"."+(diary.getDate().getMonth())+"."+diary.getDate().getDate();
                    diaryDate.setText(date);
                    diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                    diary_write.setText(diary.getText());
                    label_this = null;
                    try {
                        label_this = diary.getAllLabel(helper);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(label_this  == null || label_this.size() == 0 || label_this.get(0).getLabelname() == null){
                        tag = "happy"; diaryIcon.setImageDrawable(setTags(tag));
                    }
                    else{
                        int length = label_this.size();
                        tag = label_this.get(length - 1).getLabelname();
                        diaryIcon.setImageDrawable(setTags(tag));
                        if(length >= 2 && length <=5)
                            for(int i = 0; i<= length-2; i++){
                                tag = label_this.get(i).getLabelname();
                                imageItems.get(length - 2 - i).setImageDrawable(setTags(tag));
                            }
                        else if(length >=6)
                            for(int i = 0; i<=3; i++){
                                tag = label_this.get(length - 2 - i).getLabelname();
                                imageItems.get(i).setImageDrawable(setTags(tag));
                            }
                    }
                }
                break;
            case R.id.next_diary:
                if(index == diaryList.size()-1){
                    Toast.makeText(DiaryWriteActivity.this, "这已经是最新的日记啦",Toast.LENGTH_SHORT).show();
                }
                else{
                    index = index + 1;
                    diary = diaryList.get(index);
                    String date = (diary.getDate().getYear())+"."+(diary.getDate().getMonth())+"."+diary.getDate().getDate();
                    diaryDate.setText(date);
                    diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                    diary_write.setText(diary.getText());
                    label_this = null;
                    try {
                        label_this = diary.getAllLabel(helper);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(label_this  == null || label_this.size() == 0 || label_this.get(0).getLabelname() == null){
                        tag = "happy"; diaryIcon.setImageDrawable(setTags(tag));
                    }
                    else{
                        int length = label_this.size();
                        tag = label_this.get(length - 1).getLabelname();
                        diaryIcon.setImageDrawable(setTags(tag));
                        if(length >= 2 && length <=5)
                            for(int i = 0; i<= length-2; i++){
                                tag = label_this.get(i).getLabelname();
                                imageItems.get(length - 2 - i).setImageDrawable(setTags(tag));
                            }
                        else if(length >=6)
                            for(int i = 0; i<=3; i++){
                                tag = label_this.get(length - 2 - i).getLabelname();
                                imageItems.get(i).setImageDrawable(setTags(tag));
                            }
                    }
                }
            case R.id.edit_layout:
                if(confirm.getVisibility()==View.VISIBLE){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        diary_write.requestFocus();
                        imm.showSoftInput(diary_write,0);
                    }
                }
                break;
            case R.id.confirm:
                //Log.i("test", Html.toHtml(diary_write.getText()));
                if(index == diaryList.size()){
                    diary = new Diary(diary_write.getText().toString());
                    Date date = new Date();
                    diary.setDate(date);
                    diary.insert(helper);
                    diaryList.add(diary);
                }
                else{
                    diary.setText(diary_write.getText().toString());
                    diary.update(helper);
                    diaryList.remove(index);
                    diaryList.add(index,diary);
                }
//                List<Diary> diaryList = Diary.getAll(helper,true);
//                for(Diary i : diaryList){
//                    Log.i("test", i.getText());
//                }
                CharSequence charSequence = Html.fromHtml(Html.toHtml(diary_write.getText()));
                //Toast.makeText(DiaryWriteActivity.this,charSequence,Toast.LENGTH_SHORT).show();
                diary_write.setEnabled(false);
                more_setting.setVisibility(View.INVISIBLE);
                font_set.setVisibility(View.INVISIBLE);
                face_expression.setVisibility(View.INVISIBLE);
                insert_image.setVisibility(View.INVISIBLE);
                keyboard.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);
                preDiary.setVisibility(View.VISIBLE);
                nextDiary.setVisibility(View.VISIBLE);
                actionBar.show();
                break;
            case R.id.font_setting:
                font_setting_bottom_sheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.font_red:
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkred)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 1;
                break;
            case R.id.font_orange:
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkorange)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 2;
                break;
            case R.id.font_pink:
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 3;
                break;
            case R.id.font_green:
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkgreen)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 4;
                break;
            case R.id.font_blue:
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.deepskyblue)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 5;
                break;
            case R.id.font_dark_blue:
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.steelblue)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 6;
                break;
            case R.id.font_grey:
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dimgrey)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 7;
                break;
            case R.id.font_black:
                if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                {
                    Editable editable = diary_write.getText();
                    editable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else
                    font_color = 8;
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
            case R.id.set_retract:
                is_retract=!is_retract;
                if(is_retract)
                    set_retract.setBackgroundColor(Color.GRAY);
                else
                    set_retract.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                style=1;
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
            case R.id.clear_span:
                //Editable editable = diary_write.getEditableText();
                //editable.clearSpans();
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
                    if(diary_write.getSelectionStart() != diary_write.getSelectionEnd())
                    {
                        Editable editable = diary_write.getText();
                        editable.setSpan(new StyleSpan(Typeface.BOLD), diary_write.getSelectionStart(), diary_write.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    set_bold.setBackgroundColor(Color.GRAY);
                }
                else {
                    if (diary_write.getSelectionStart() != diary_write.getSelectionEnd()) {
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
                    if (diary_write.getSelectionStart() != diary_write.getSelectionEnd()) {
                        Editable editable = diary_write.getText();
                        StyleSpan[] selectedSpans = editable.getSpans(diary_write.getSelectionStart(), diary_write.getSelectionEnd(), StyleSpan.class);
                        for (StyleSpan style : selectedSpans) {
                            if (style.getStyle() == Typeface.ITALIC) {
                                editable.removeSpan(style);
                            }
                        }
                        set_italic.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }
                }
                break;
            case R.id.insert_image:
                openAlbum();
                break;
        }
    }

    private void editchange(Editable editable)
    {
        switch (font_color)
        {
            case 1:
                ForegroundColorSpan colorSpan_red = new ForegroundColorSpan(getResources().getColor(R.color.darkred));
                editable.setSpan(colorSpan_red, start, start+count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diary_write.setSelection(start+count);
                break;
            case 2:
                ForegroundColorSpan colorSpan_orange = new ForegroundColorSpan(getResources().getColor(R.color.darkorange));
                editable.setSpan(colorSpan_orange, start, start+count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diary_write.setSelection(start+count);
                break;
            case 3:
                ForegroundColorSpan colorSpan_pink = new ForegroundColorSpan(getResources().getColor(R.color.pink));
                editable.setSpan(colorSpan_pink, start, start+count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diary_write.setSelection(start+count);
                break;
            case 4:
                ForegroundColorSpan colorSpan_green = new ForegroundColorSpan(getResources().getColor(R.color.darkgreen));
                editable.setSpan(colorSpan_green, start, start+count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diary_write.setSelection(start+count);
                break;
            case 5:
                ForegroundColorSpan colorSpan_blue = new ForegroundColorSpan(getResources().getColor(R.color.deepskyblue));
                editable.setSpan(colorSpan_blue, start, start+count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diary_write.setSelection(start+count);
                break;
            case 6:
                ForegroundColorSpan colorSpan_dark_blue = new ForegroundColorSpan(getResources().getColor(R.color.steelblue));
                editable.setSpan(colorSpan_dark_blue, start, start+count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diary_write.setSelection(start+count);
                break;
            case 7:
                ForegroundColorSpan colorSpan_grey = new ForegroundColorSpan(getResources().getColor(R.color.dimgrey));
                editable.setSpan(colorSpan_grey, start, start+count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diary_write.setSelection(start+count);
                break;
            case 8:
                ForegroundColorSpan colorSpan_black = new ForegroundColorSpan(getResources().getColor(R.color.black));
                editable.setSpan(colorSpan_black, start, start+count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diary_write.setSelection(start+count);
                break;
        }
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
        if(is_bold)
            editable.setSpan(new StyleSpan(Typeface.BOLD), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(is_italic)
            editable.setSpan(new StyleSpan(Typeface.ITALIC), start, start + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.label_manage:
                showDialog(ICON_LIST_DIALOG);
                break;
            case R.id.edit:
                diary_write.setEnabled(true);
                more_setting.setVisibility(View.VISIBLE);
                font_set.setVisibility(View.VISIBLE);
                face_expression.setVisibility(View.VISIBLE);
                insert_image.setVisibility(View.VISIBLE);
                keyboard.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
                preDiary.setVisibility(View.INVISIBLE);
                nextDiary.setVisibility(View.INVISIBLE);
                actionBar.hide();
                break;
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                //Toast.makeText(DiaryActivity.this, "删除这一篇日记",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(DiaryWriteActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("你确定要删除你的日记吗？");
                dialog.setCancelable(true);

                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                        diary.delete(helper);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){

                    }
                });
                dialog.show();
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
            bitmap = getBitmapFormUri(DiaryWriteActivity.this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveBitmap(DiaryWriteActivity.this,bitmap);
        SpannableString mSpan = new SpannableString("1");
        int insert_position = diary_write.getSelectionStart();
        mSpan.setSpan(new ImageSpan(bitmap) , mSpan.length() - 1, mSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable editable = diary_write.getText();
        editable.insert(insert_position, mSpan);
        diary_write.setText(editable);
        diary_write.append("\n");
    }

    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
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

    private static final String SD_PATH = "/sdcard/HeartTrace/pic/";
    private static final String IN_PATH = "/HeartTrace/pic/";

    private static String generateFileName() {
        SimpleDateFormat time_format=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
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

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
            case ICON_LIST_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.mood);
                builder.setTitle("今天心情如何？");
                BaseAdapter adapter = new ListItemAdapter();
                DialogInterface.OnClickListener listener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                tagId = which;
                                if(tagId == 0) label_name = "happy";
                                else if(tagId == 1) label_name = "normal";
                                else if(tagId == 2) label_name = "sad";
                                else label_name = "happy";

                                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                                //diary = diaryList.get(index);
                                diary.setid(tagId);

                                Label label = Label.getByName(helper,label_name);
                                if(label == null) {
                                    label = new Label(label_name);
                                    label.insert(helper);
                                }
                                diary.insertLabel(helper, label);

                                label_this = null;
                                try {
                                    label_this = diary.getAllLabel(helper);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if(label_this  == null || label_this.size() == 0 || label_this.get(0).getLabelname() == null){
                                    tag = "happy"; diaryIcon.setImageDrawable(setTags(tag));
                                }
                                else{
                                    int length = label_this.size();
                                    tag = label_this.get(length - 1).getLabelname();
                                    diaryIcon.setImageDrawable(setTags(tag));
                                    if(length >= 2 && length <=5)
                                        for(int i = 0; i<= length-2; i++){
                                            tag = label_this.get(i).getLabelname();
                                            imageItems.get(length - 2 - i).setImageDrawable(setTags(tag));
                                        }
                                    else if(length >=6)
                                        for(int i = 0; i<=3; i++){
                                            tag = label_this.get(length - 2 - i).getLabelname();
                                            imageItems.get(i).setImageDrawable(setTags(tag));
                                        }
                                }

                            }
                        };
                builder.setAdapter(adapter, listener);
                dialog = builder.create();
                break;
        }
        return dialog;
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
            textView.setTextSize(20);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            //设置水平方向上居中
            textView.setGravity(android.view.Gravity.CENTER_VERTICAL);
            textView.setMinHeight(65);
            //设置文字颜色
            textView.setTextColor(Color.BLACK);
            //设置图标在文字的左边
            textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position], 0, 0, 0);
            //设置textView的左上右下的padding大小
            textView.setPadding(30, 10, 30, 10);
            //设置文字和图标之间的padding大小
            textView.setCompoundDrawablePadding(25);
            return textView;
        }
    }

    public Drawable setTag(int id){
        switch(id)
        {
            case 0:
                return (getResources().getDrawable(R.drawable.happy_black));
            case 1:
                return (getResources().getDrawable(R.drawable.normal_black));
            case 2:
                return (getResources().getDrawable(R.drawable.sad_black));
            default:
                return (getResources().getDrawable(R.drawable.normal_black));
        }
    }

    public Drawable setTags(String id){
        switch(id)
        {
            case "happy":
                return (getResources().getDrawable(R.drawable.happy_black));
            case "normal":
                return (getResources().getDrawable(R.drawable.normal_black));
            case "sad":
                return (getResources().getDrawable(R.drawable.sad_black));
            default:
                return (getResources().getDrawable(R.drawable.normal_black));
        }
    }



}
