package com.example.dell.diary;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    public List<Diary> diaryList = new ArrayList<>();
    private DiaryCardAdapter adapter;
    private FloatingActionButton add;
    private RecyclerView recyclerView;

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }
        navView.setCheckedItem(R.id.nav_user);
        //navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.statistics:
                        Intent intent = new Intent(MainActivity.this,StatisticsActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });

        FloatingActionButton test = (FloatingActionButton) findViewById(R.id.test);
        test.setVisibility(View.INVISIBLE);
        final FloatingActionButton addDiary = (FloatingActionButton) findViewById(R.id.add_diary);
        final FloatingActionButton addBottle = (FloatingActionButton) findViewById(R.id.add_bottle);
        addDiary.setVisibility(View.INVISIBLE);
        addBottle.setVisibility(View.INVISIBLE);
        add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addDiary.getVisibility() == View.INVISIBLE) {
                    addDiary.setVisibility(View.VISIBLE);
                    addBottle.setVisibility(View.VISIBLE);
                }
                else{
                    addDiary.setVisibility(View.INVISIBLE);
                    addBottle.setVisibility(View.INVISIBLE);
                }
            }
        });
        addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "写新日记", Toast.LENGTH_SHORT).show();
                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                Diary newDiary = new Diary("日记内容");
                Random r = new Random();
                int randDay = r.nextInt(29)+1;
                newDiary.setDate(new Date(2018,6,randDay));
                diaryList.add(0,newDiary);
                newDiary.insert(helper);
                adapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
                //Intent intent = new Intent(MainActivity.this, DiaryWriteActivity.class);
                //startActivity(intent);
            }
        });
        addBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(MainActivity.this, "写新瓶子", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, TicketEditActivity.class);
                startActivity(intent);
            }
        });
        Button enterBottle = (Button)findViewById(R.id.enter_bottle);
        enterBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BottlesActivity.class);
                startActivity(intent);
            }
        });
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
          diaryList = Diary.getAll(helper);
          if(diaryList == null){
              diaryList = new ArrayList<>();
          }
          else{
//              Log.i("123",String.valueOf(diaryList.size()));
//              int number = diaryList.size();
//              for(int i=0;i<number;i++){
//                  diaryList.get(i).delete(helper);
//                  diaryList.remove(i);
//                  Log.i("234",String.valueOf(diaryList.size()));
//              }
//              Log.i("456",String.valueOf(diaryList.size()));
          }
        Collections.reverse(diaryList);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.calendar:
                Intent intentCalendar = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intentCalendar);
                break;
            case R.id.search:
                //Toast.makeText(this, "搜索你的日记", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                Toast.makeText(this, "写新的日记", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_setting:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    protected void onResume(){
        super.onResume();
        final FloatingActionButton addDiary = (FloatingActionButton) findViewById(R.id.add_diary);
        final FloatingActionButton addBottle = (FloatingActionButton) findViewById(R.id.add_bottle);
        addDiary.setVisibility(View.INVISIBLE);
        addBottle.setVisibility(View.INVISIBLE);
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
            databaseHelper = OpenHelperManager.getHelper(MainActivity.this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
