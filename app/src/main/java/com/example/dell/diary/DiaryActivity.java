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

import com.bumptech.glide.Glide;

public class DiaryActivity extends AppCompatActivity {

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
        DiaryCard diaryCard = (DiaryCard) intent.getSerializableExtra("diary_list");
        //int year = intent.getIntExtra("diary_year",2018);
        //int month = intent.getIntExtra("diary_month",1);
        //int day = intent.getIntExtra("diary_day",1);
        //String date = year+"."+month+"."+day;
        //String weekday = intent.getStringExtra("diary_weekday");
        //int icon = intent.getIntExtra("diary_icon",0);
        //String text = intent.getStringExtra("diary_text");
        TextView diaryDate = (TextView)findViewById(R.id.diary_content_date);
        TextView diaryWeekday = (TextView)findViewById(R.id.diary_content_weekday);
        TextView diaryContent = (TextView)findViewById(R.id.diary_content_text);
        ImageView diaryIcon = (ImageView)findViewById(R.id.diary_content_icon);
        String date = diaryCard.getYear()+"."+diaryCard.getMonth()+"."+diaryCard.getDay();
        diaryDate.setText(date);
        diaryWeekday.setText(diaryCard.getWeekDay());
        diaryContent.setText(diaryCard.getContent());
        Glide.with(this).load(diaryCard.getEmotionImageId()).into(diaryIcon);

        preDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiaryActivity.this, "前一篇日记",Toast.LENGTH_SHORT).show();
            }
        });
        nextDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiaryActivity.this, "后一篇日记",Toast.LENGTH_SHORT).show();
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
            case R.id.edit:
                Toast.makeText(this, "编辑", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return true;
    }
}
