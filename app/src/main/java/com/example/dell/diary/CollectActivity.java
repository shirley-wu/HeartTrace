package com.example.dell.diary;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Sentence;
import com.example.dell.db.Sentencebook;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {
    private DrawerLayout mDrawLayout;
    private CoordinatorLayout container;
    private RecyclerView.Adapter adapterD;
    private RecyclerView.Adapter adapterS;
    private TextView spinnertext;
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private List<Diary> collect_diaryList =  new ArrayList<>();
    private List<Sentence>collect_sentenceList = new ArrayList<>();
    private boolean OptionOnDiary = true;
    private Menu amenu ;

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
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_collect_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapterD = new CollectDAdapter(collect_diaryList);
        recyclerView.setAdapter(adapterD);

        class SpinnerXMLSelectedListener implements AdapterView.OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                spinnertext.setText("选择："+spinnerAdapter.getItem(arg2));
                if(arg2 == 0 ){
                    if(adapterD != null) {
                       recyclerView.setAdapter(adapterD);
                    }
                    else{
                        adapterD = new CollectDAdapter(collect_diaryList);
                        recyclerView.setAdapter(adapterD);
                    }
                }
                else if(arg2 == 1){
                    Toast.makeText(CollectActivity.this,"暂时还没有纸条噢", Toast.LENGTH_LONG).show();
                    if(adapterS != null){
                        recyclerView.setAdapter(adapterS);
                    }
                    else {
                        Toast.makeText(CollectActivity.this,"暂时还没有纸条噢", Toast.LENGTH_LONG).show();
                        adapterS = new CollectSAdapter(collect_sentenceList);
                        recyclerView.setAdapter(adapterS);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        }
        spinner = findViewById(R.id.collect_spinner);
        spinnertext = findViewById(R.id.spinnerText);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.like_select, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
        spinner.setVisibility(View.VISIBLE);


    }


    public void initCollectList() {
        collect_diaryList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        collect_diaryList = Diary.getAllLike(helper,false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collectoolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //mDrawLayout.openDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.search:
        }
        return true;
    }
}
