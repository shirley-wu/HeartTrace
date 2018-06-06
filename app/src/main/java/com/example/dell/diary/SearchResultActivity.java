package com.example.dell.diary;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.SearchHistory;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    public List<Diary> diaryList = new ArrayList<>();
    private SearchResultAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private DatabaseHelper databaseHelper = null;
    private Date startDate;
    private Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("");
        searchView = (SearchView)findViewById(R.id.search_view);
        //设置我们的SearchView
        searchView.setIconifiedByDefault(false);//设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
        searchView.onActionViewExpanded();// 写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
        searchView.setIconified(false);//输入框内icon不显示
        searchView.requestFocus();//输入焦点
        //searchView.setSubmitButtonEnabled(true);//添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
        //searchView.setFocusable(true);//将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
        //searchView.requestFocusFromTouch();//模拟焦点点击事件

        //searchView.setFocusable(false);//禁止弹出输入法，在某些情况下有需要
        //searchView.clearFocus();//禁止弹出输入法，在某些情况下有需要
        EditText seachTextView = (EditText) searchView.findViewById(R.id.search_src_text);
        seachTextView.setTextColor(getResources().getColor(R.color.black));
        seachTextView.setHintTextColor(getResources().getColor(R.color.lightgray));
        seachTextView.setTextSize(17);
//        int imgId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon",null,null);
//获取ImageView
        ImageView searchButton = (ImageView)searchView.findViewById(R.id.search_mag_icon);
//设置图片
        searchButton.setImageResource(R.drawable.search_gray);
        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);
//设置图片
        closeButton.setImageResource(R.drawable.close_gray);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query) {
                diaryList.clear();
                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                try{
                    diaryList.clear();
                    if(startDate.getYear() == 1900){
                        diaryList.addAll(Diary.getByRestrict(helper,query,null,null,null,false));
                    }
                    else{
                        diaryList.addAll(Diary.getByRestrict(helper,query,startDate,endDate,null,false));
                    }

                    //adapter = new SearchResultAdapter(diaryList);
                    adapter.notifyDataSetChanged();
                    }
                catch (Exception e){}
                if(diaryList.size() == 0){
                    Toast.makeText(SearchResultActivity.this,"没有搜索到相关日记",Toast.LENGTH_LONG).show();
                }
                searchView.clearFocus();
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 0) {
                    //currentSearchTip = newText;
                    //showSearchTip(newText);
                    //Toast.makeText(SearchResultActivity.this, newText, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        initSearchResult();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchResultAdapter(diaryList);
        recyclerView.setAdapter(adapter);
    }

    private void initSearchResult(){
        diaryList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        Intent intent = getIntent();
        String searchText = intent.getStringExtra("search_text");
        startDate = (Date)intent.getSerializableExtra("start_date");
        endDate = (Date)intent.getSerializableExtra("end_date");
        Log.i("start_year",String.valueOf(startDate.getYear())+startDate.getMonth()+startDate.getDate());
        Log.i("end_year",String.valueOf(endDate.getYear())+endDate.getMonth()+endDate.getDate());
        searchView.setQuery(searchText,false);
        searchView.clearFocus();
        try{
            if(startDate.getYear() == 1900){
                diaryList = Diary.getByRestrict(helper,searchText,null,null,null,false);
            }
            else{
                diaryList = Diary.getByRestrict(helper,searchText,startDate,endDate,null,false);
            //diaryList = Diary.getByRestrict(helper,searchText,new Date(2018,6,1),new Date(2018,6,2),null,false);
            }
        }
        catch (Exception e){}
        if(diaryList.size() == 0){
           Toast.makeText(SearchResultActivity.this,"没有搜索到相关日记",Toast.LENGTH_LONG).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    //    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return true;
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
            databaseHelper = OpenHelperManager.getHelper(SearchResultActivity.this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}



