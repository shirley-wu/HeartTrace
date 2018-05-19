package com.example.dell.diary;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_DATE = "note_date";
    public static final String NOTE_WEAKDAY = "note_weekday";
    public static final String NOTE_CONTENT = "note_content";
    private CoordinatorLayout container;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notetoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                container=(CoordinatorLayout) findViewById(R.id.note_coordinatorLayout);
                Snackbar.make(container, "确认删除？",Snackbar.LENGTH_SHORT).setAction("Undo",new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(NoteActivity.this, "成功拯救一个瓶子",Toast.LENGTH_SHORT).show();
                    }
                }).show();

                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar)findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        String weekday = intent.getStringExtra("note_weekday");
        String date = intent.getStringExtra("note_date");
        String text = intent.getStringExtra("note_content");
        TextView noteDate = (TextView)findViewById(R.id.note_date);
        TextView noteWeekday = (TextView)findViewById(R.id.note_weekday);
        TextView noteContent = (TextView)findViewById(R.id.note_content);
        noteDate.setText(date);
        noteWeekday.setText(weekday);
        noteContent.setText(text);


    }
}
