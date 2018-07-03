package com.example.dell.diary;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Sentencebook;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {
    private DrawerLayout mDrawLayout;
    private CoordinatorLayout container;
    private CollectAdapter adapter;
    private List<Diary> collect_diaryList =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.collect_toolbar);
        setSupportActionBar(toolbar);
        mDrawLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView =(NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }
        actionBar.setTitle("我的收藏");
        navView.setCheckedItem(R.id.nav_user);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawLayout.closeDrawers();
                return true;
            }
        });

        initCollectList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_collect_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
      /*  adapter = new CollectAdapter(collect_diaryList);
        recyclerView.setAdapter(adapter);*/


    }


    public void initCollectList() {
        collect_diaryList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        collect_diaryList = Diary.getAllLike(helper,false);
    }
}
