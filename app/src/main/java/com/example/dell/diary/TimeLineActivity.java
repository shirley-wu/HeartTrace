package com.example.dell.diary;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    public List<Diary> diaryList = new ArrayList<>();
    private DiaryCardAdapter adapter;
    private FloatingActionButton add;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("时间轴");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }

        initDiaryCard();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryCardAdapter(diaryList);
        recyclerView.setAdapter(adapter);
    }

    private void initDiaryCard(){

          diaryList.clear();
          DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

          //diaryList = Diary.getAll(helper,false);

          diaryList = Diary.getAll(helper,false);
          if(diaryList == null){
              diaryList = new ArrayList<>();
          }
          else{
//              Log.i("123",String.valueOf(diaryList.size()));
//              int number = diaryList.size();
//              for(int i=0;i< number; i++){
//                  diaryList.get(0).delete(helper);
//                  diaryList.remove(0);
//                  Log.i("234",String.valueOf(diaryList.size()));
//              }
//              Log.i("456",String.valueOf(diaryList.size()));
          }
        //Collections.reverse(diaryList);
//        for(int i = 1;i<4;i++){
//            Diary newDiary = new Diary("一二三四"+i);
//            newDiary.setDate(new Date(2018,6,i));
//            diaryList.add(0,newDiary);
//            newDiary.insert(helper);
//        }
//        for(int i = 4;i<8;i++){
//            Diary newDiary = new Diary("五六七八"+i);
//            newDiary.setDate(new Date(2018,6,i));
//            diaryList.add(0,newDiary);
//            newDiary.insert(helper);
//        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.search:
                //Toast.makeText(this, "搜索你的日记", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TimeLineActivity.this, SearchActivity.class);
                startActivity(intent);
                break;

            default:
        }
        return true;
    }

    protected void onRestart(){
        super.onRestart();
        diaryList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        diaryList = Diary.getAll(helper,false);
        adapter = new DiaryCardAdapter(diaryList);
        recyclerView.setAdapter(adapter);
//        diaryList.addAll(Diary.getAll(helper,false));
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(databaseHelper != null){
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getDataBaseHelper(){
        if(databaseHelper == null){
            databaseHelper = OpenHelperManager.getHelper(TimeLineActivity.this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
