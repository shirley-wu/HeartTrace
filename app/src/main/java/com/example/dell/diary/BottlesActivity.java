package com.example.dell.diary;

import android.app.Dialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BottlesActivity extends AppCompatActivity {
    private DrawerLayout mDrawLayout;
    private CoordinatorLayout container;
    private Bottle[] bottles = {new Bottle("碎片拾遗", R.drawable.bottle_pink1), new Bottle("奇思妙想", R.drawable.bottle_pink2), new Bottle("追踪坏习惯", R.drawable.bottle_gray1)};

    private List<Bottle> bottleList = new ArrayList<>();

    private BottleAdapter adapter;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottlestoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //mDrawLayout.openDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.delete:
                container=(CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                Snackbar.make(container, "确认删除？",Snackbar.LENGTH_SHORT).setAction("Undo",new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(BottlesActivity.this, "成功拯救一个瓶子",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_bottles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bottles_toolbar);
        setSupportActionBar(toolbar);
        mDrawLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView =(NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }
        actionBar.setTitle("Bottles");
        navView.setCheckedItem(R.id.nav_user);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawLayout.closeDrawers();
                return true;
            }
        });

        initBottles();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_bottle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BottleAdapter(bottleList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_bottle_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_save_pop:
                                finish();
                                break;
                        }
                    }
                };
                NewBottleDialog dialog = new NewBottleDialog(BottlesActivity.this,R.style.AppTheme,onClickListener);
                dialog.setTitle("添加一个新的瓶子");
                dialog.show();

            }
        });

    }

    private void initBottles(){
        bottleList.clear();
        for( int i = 0; i<2; i++){
            bottleList.add(bottles[i]);
        }
    }
}
