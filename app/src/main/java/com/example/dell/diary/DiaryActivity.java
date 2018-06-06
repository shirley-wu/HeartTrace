package com.example.dell.diary;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {
    Diary diary;
    List<Diary> diaryList = new ArrayList<>();
    int index;
    TextView diaryDate;
    TextView diaryWeekday;
    TextView diaryContent;

    public List<String> weekList = new ArrayList<>(Arrays.asList("周日","周一","周二","周三"," 周四","周五","周六"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_diary_content);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Button preDiary = (Button)findViewById(R.id.pre_diary);
        Button nextDiary = (Button)findViewById(R.id.next_diary);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        diary = (Diary) intent.getSerializableExtra("diary_list");
        diaryDate = (TextView)findViewById(R.id.diary_content_date);
        diaryWeekday = (TextView)findViewById(R.id.diary_content_weekday);
        diaryContent = (TextView)findViewById(R.id.diary_content_text);
        String date = diary.getDate().getYear()+"."+diary.getDate().getMonth()+"."+diary.getDate().getDate();
        diaryDate.setText(date);
        diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
        diaryContent.setText(diary.getText());
        //Glide.with(this).load(diaryCard.getEmotionImageId()).into(diaryIcon);
        diaryList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        diaryList.addAll(Diary.getAll(helper,false));
        index = diaryList.indexOf(diary);
        preDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == 0){
                    Toast.makeText(DiaryActivity.this, "没有更早的日记了哦",Toast.LENGTH_SHORT).show();
                }
                else{
                    index = index -1;
                    diary = diaryList.get(index);
                    String date = diary.getDate().getYear()+"."+diary.getDate().getMonth()+"."+diary.getDate().getDate();
                    diaryDate.setText(date);
                    diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                    diaryContent.setText(diary.getText());
                }

            }
        });
        nextDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == diaryList.size()-1){
                    Toast.makeText(DiaryActivity.this, "这已经是最新的日记啦",Toast.LENGTH_SHORT).show();
                }
                else{
                    index = index + 1;
                    diary = diaryList.get(index);
                    String date = diary.getDate().getYear()+"."+diary.getDate().getMonth()+"."+diary.getDate().getDate();
                    diaryDate.setText(date);
                    diaryWeekday.setText(weekList.get(diary.getDate().getDay()));
                    diaryContent.setText(diary.getText());
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.label_manage:
                Intent intent1 = new Intent(DiaryActivity.this, LabelManageActivity.class);
                intent1.putExtra("diary",diary);
                startActivity(intent1);
                break;
            case R.id.edit:
                Intent intent2 = new Intent(DiaryActivity.this, DiaryWriteActivity.class);
                intent2.putExtra("diary",diary);
                startActivity(intent2);
                break;
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                Toast.makeText(DiaryActivity.this, "删除这一篇日记",Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }
}
