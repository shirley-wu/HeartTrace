package com.example.dell.diary;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Search");
        final CheckedTextView checkTag = (CheckedTextView)findViewById(R.id.check_tag);
        final CheckedTextView checkTime = (CheckedTextView)findViewById(R.id.check_time);
        final LinearLayout timeSelect = (LinearLayout)findViewById(R.id.time_select);
        timeSelect.setVisibility(View.INVISIBLE);
        checkTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                CheckedTextView checkedTextView = (CheckedTextView)v;
                checkedTextView.toggle();
                if(checkTag.isChecked() == true){

                }
            }
        });
        checkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                CheckedTextView checkedTextView = (CheckedTextView)v;
                checkedTextView.toggle();
                if(checkTime.isChecked() == true){
                    timeSelect.setVisibility(View.VISIBLE);
                }
                else{
                    timeSelect.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calender:
                Toast.makeText(this, "打开日历", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
