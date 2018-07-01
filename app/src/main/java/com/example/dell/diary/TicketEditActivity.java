package com.example.dell.diary;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Label;
import com.example.dell.db.Sentence;
import com.example.dell.db.Sentencebook;
import com.example.dell.diary.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TicketEditActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    public static final String BOTTLE_NAME = "bottle_name";
    public static final String SENTENCE_THIS = "sentence_this";
    public static final String NOTE_EDITABLE = "note_editable";
    public static final String NOTE_NEW = "true";
    private static final String TAG = "TicketEditActivity";
    private String sentence_name = "travel";
    private String tag= null;
    private boolean ifExisted;
    private List<ImageView> imageItems = new ArrayList<ImageView>(3);
    private int[] imgIds = {R.drawable.travel, R.drawable.work, R.drawable.study, R.drawable.entertainment, R.drawable.love};
    List<Sentence> sentenceList = new ArrayList<>();
    List<Label> labelList = new ArrayList<>();
    List<Label> label_this = new ArrayList<>();
    private final int ICON_LIST_DIALOG = 1;
    ActionBar actionBar;
    private TextView sentenceDate;
    private TextView sentenceWeekday;
    private ImageView sentenceIcon;
    private ImageView sentenceIcon1;
    private ImageView sentenceIcon2;
    private ImageView sentenceIcon3;
    private ImageView sentenceIcon4;
    private FloatingActionButton ticket_confirm;
    private FloatingActionButton ticket_edit;
    private EditText editText ;
    private String note_editable = "true";
    private Sentence sentence;
    private ObjectAnimator objAnimatorX;
    private int flag = 1;
    private int labelSize;
    private String note_new;
    public List<String> weekList = new ArrayList<>(Arrays.asList("周日","周一","周二","周三"," 周四","周五","周六"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: correct");
        setContentView(R.layout.activity_ticket_edit);
        Intent intent = getIntent();
        note_editable = intent.getStringExtra(NOTE_EDITABLE);
        sentence = (Sentence) intent.getSerializableExtra(SENTENCE_THIS);
        note_new = intent.getStringExtra(NOTE_NEW);
        ticket_confirm = (FloatingActionButton)findViewById(R.id.ticket_confirm);
        editText = findViewById(R.id.ticket_text);
        ticket_edit = (FloatingActionButton) findViewById(R.id.ticket_edit);
        ticket_confirm.setOnClickListener(this);
        ticket_edit.setOnClickListener(this);
        sentenceIcon = (ImageView) findViewById(R.id.sentence_content_icon);
        sentenceIcon1 = (ImageView) findViewById(R.id.sentence_content_icon1);
        sentenceIcon2 = (ImageView) findViewById(R.id.sentence_content_icon2);
        sentenceIcon3 = (ImageView) findViewById(R.id.sentence_content_icon3);
        sentenceIcon4 = (ImageView) findViewById(R.id.sentence_content_icon4);
        sentenceIcon.setOnClickListener(this);
        sentenceIcon.setOnLongClickListener(this);
        sentenceIcon1.setOnLongClickListener(this);
        sentenceIcon2.setOnLongClickListener(this);
        sentenceIcon3.setOnLongClickListener(this);
        sentenceIcon4.setOnLongClickListener(this);

        Init();
    }

    void Init(){
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        imageItems.add(sentenceIcon1);
        imageItems.add(sentenceIcon2);
        imageItems.add(sentenceIcon3);
        imageItems.add(sentenceIcon4);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_sentence_content);
        sentenceDate = (TextView)findViewById(R.id.sentence_date);
        sentenceWeekday = (TextView)findViewById(R.id.sentence_weekday);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(note_editable.equals("false") ){
            editText.setText(sentence.getText());
            getLabelsOfSentence(sentence,helper);
            String date = (sentence.getDate().getYear()+1900)+"年"+(sentence.getDate().getMonth()+1)+"月"+sentence.getDate().getDate()+"日";
            sentenceDate.setText(date);
            sentenceWeekday.setText(weekList.get(sentence.getDate().getDay()));

            ticket_confirm.setVisibility(View.INVISIBLE);
            editText.setEnabled(false);
            actionBar.show();
        }
        else if (note_editable.equals("true")){
            Date date = new Date();
            String today = (date.getYear()+1900)+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日";
            sentenceDate.setText(today);
            sentenceWeekday.setText(weekList.get(date.getDay()));

            ticket_edit.setVisibility(View.INVISIBLE);
            editText.setEnabled(true);
            editText.setText(sentence.getText());
            actionBar.hide();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.ticket_confirm:
                actionBar.show();
                ticket_edit.setVisibility(View.VISIBLE);
                ticket_confirm.setVisibility(View.INVISIBLE);
                editText.setEnabled(false);
                //添加新的纸条
                if(note_new.equals("true")) {
                    note_new = "false";
                    String sentenceContent = editText.getText().toString();
                    DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                    sentence.setText(sentenceContent);
                    sentence.setDate();
                    sentence.insert(helper);
                    Log.d(TAG, "onClick: 保存新添加的纸条");
                }
                //修改原有纸条
                if(note_new.equals("false")){
                    DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                    String sentenceContent = editText.getText().toString();
                    sentence.setText(sentenceContent);
                    sentence.update(helper);
                }
                break;
            case R.id.ticket_edit:
                actionBar.hide();
                ticket_edit.setVisibility(View.INVISIBLE);
                ticket_confirm.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                break;
            case R.id.sentence_content_icon:
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

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sentence_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.label_sentence:
                showDialog(ICON_LIST_DIALOG);
                break;

            case android.R.id.home:
                finish();
                return true;

            default:
        }
        return true;
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
            case ICON_LIST_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.mood);
                builder.setTitle("给小纸条贴个标签？");
                BaseAdapter adapter = new TicketEditActivity.ListItemAdapter();
                DialogInterface.OnClickListener listener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                switch (which){
                                    case 0: sentence_name = "travel";break;
                                    case 1: sentence_name = "study";break;
                                    case 2: sentence_name = "work";break;
                                    case 3: sentence_name = "entertainment";break;
                                    case 4: sentence_name = "love";break;
                                }
                                ifExisted = false;
                                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

                                Label label = Label.getByName(helper,sentence_name);
                                if(label == null) {
                                    label = new Label(sentence_name);
                                    label.insert(helper);
                                }
                                label_this = null;
                                try {
                                    label_this = sentence.getAllLabel(helper);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                if(label_this != null && label_this.size()!= 0 && label_this.get(0).getLabelname() != null)
                                {
                                    for(Label i:label_this)
                                    {
                                        if(i.getLabelname().equals(sentence_name))
                                            ifExisted = true;
                                    }
                                }
                                if(!ifExisted) sentence.insertLabel(helper, label);
                                getLabelsOfSentence(sentence,helper);
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
            label_this = sentence.getAllLabel(helper);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(label_this != null && label_this.size()!= 0 && label_this.get(0).getLabelname() != null)
        {
            labelSize = label_this.size();
            switch (view.getId()) {
                case R.id.sentence_content_icon:
                    if(labelSize >= 1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setTitle("提示");
                        dialog.setMessage("你确定要删除此标签吗？");
                        dialog.setCancelable(true);

                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sentence.deleteLabel(helper, label_this.get(labelSize - 1));
                                getLabelsOfSentence(sentence, helper);
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                    break;
                case R.id.sentence_content_icon1:
                    if(labelSize >= 2){
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                        dialog1.setTitle("提示");
                        dialog1.setMessage("你确定要删除此标签吗？");
                        dialog1.setCancelable(true);

                        dialog1.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                sentence.deleteLabel(helper, label_this.get(labelSize - 2));
                                getLabelsOfSentence(sentence, helper);
                            }
                        });
                        dialog1.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });
                        dialog1.show();
                    }
                    break;
                case R.id.sentence_content_icon2:
                    if(labelSize >= 3){
                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                        dialog2.setTitle("提示");
                        dialog2.setMessage("你确定要删除此标签吗？");
                        dialog2.setCancelable(true);

                        dialog2.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                sentence.deleteLabel(helper, label_this.get(labelSize - 3));
                                getLabelsOfSentence(sentence, helper);
                            }
                        });
                        dialog2.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });
                        dialog2.show();
                    }
                    break;
                case R.id.sentence_content_icon3:
                    if(labelSize >= 4){
                        AlertDialog.Builder dialog3 = new AlertDialog.Builder(this);
                        dialog3.setTitle("提示");
                        dialog3.setMessage("你确定要删除此标签吗？");
                        dialog3.setCancelable(true);

                        dialog3.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                sentence.deleteLabel(helper, label_this.get(labelSize - 4));
                                getLabelsOfSentence(sentence, helper);
                            }
                        });
                        dialog3.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });
                        dialog3.show();
                    }
                    break;
                case R.id.sentence_content_icon4:
                    if(labelSize >= 5){
                        AlertDialog.Builder dialog4 = new AlertDialog.Builder(this);
                        dialog4.setTitle("提示");
                        dialog4.setMessage("你确定要删除此标签吗？");
                        dialog4.setCancelable(true);

                        dialog4.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                sentence.deleteLabel(helper, label_this.get(labelSize - 5));
                                getLabelsOfSentence(sentence, helper);
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
                    new TextView(TicketEditActivity.this);
            //获得array.xml中的数组资源getStringArray返回的是一个String数组
            String text = getResources().getStringArray(R.array.property)[position];
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
            case "travel":
                return (getResources().getDrawable(R.drawable.travel));
            case "study":
                return (getResources().getDrawable(R.drawable.study));
            case "work":
                return (getResources().getDrawable(R.drawable.work));
            case "entertainment":
                return (getResources().getDrawable(R.drawable.entertainment));
            case "love":
                return (getResources().getDrawable(R.drawable.love));
            default:
                return (getResources().getDrawable(R.drawable.travel));
        }
    }

    public void getLabelsOfSentence(Sentence sentence, DatabaseHelper helper ){
        sentenceIcon.setImageDrawable(getResources().getDrawable(R.color.white));
        for(int i = 0; i<=3; i++){
            imageItems.get(i).setImageDrawable(getResources().getDrawable(R.color.white));
        }
        label_this = null;
        try {
            label_this = sentence.getAllLabel(helper);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(label_this  == null || label_this.size() == 0 || label_this.get(0).getLabelname() == null){
            tag = "travel";
        }
        else{
            int length = label_this.size();
            tag = label_this.get(length - 1).getLabelname();
            sentenceIcon.setImageDrawable(setTags(tag));
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
}
