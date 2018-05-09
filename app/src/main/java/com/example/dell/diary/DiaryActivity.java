package com.example.dell.diary;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_diary_content);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        int year = intent.getIntExtra("diary_year",2018);
        int month = intent.getIntExtra("diary_month",1);
        int day = intent.getIntExtra("diary_day",1);
        String date = year+"."+month+"."+day;
        String weekday = intent.getStringExtra("diary_weekday");
        int icon = intent.getIntExtra("diary_icon",0);
        String text = intent.getStringExtra("diary_text");
        TextView diaryDate = (TextView)findViewById(R.id.diary_content_date);
        TextView diaryWeekday = (TextView)findViewById(R.id.diary_content_weekday);
        TextView diaryContent = (TextView)findViewById(R.id.diary_content_text);
        ImageView diaryIcon = (ImageView)findViewById(R.id.diary_content_icon);
        diaryDate.setText(date);
        diaryWeekday.setText(weekday);
        diaryContent.setText(text);
        Glide.with(this).load(icon).into(diaryIcon);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
