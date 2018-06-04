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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.SearchHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    public List<Diary> diaryList = new ArrayList<>();
    private SearchResultAdapter adapter;
    private RecyclerView recyclerView;

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("");

        final SearchView searchView = (SearchView)findViewById(R.id.search_view);
        //设置我们的SearchView
        searchView.setIconifiedByDefault(true);//设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
        searchView.onActionViewExpanded();// 写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
        searchView.setIconified(false);//输入框内icon不显示
        searchView.requestFocus();//输入焦点
        //searchView.setSubmitButtonEnabled(true);//添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
        //searchView.setFocusable(true);//将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
        //searchView.requestFocusFromTouch();//模拟焦点点击事件

        //searchView.setFocusable(false);//禁止弹出输入法，在某些情况下有需要
        //searchView.clearFocus();//禁止弹出输入法，在某些情况下有需要

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(SearchResultActivity.this, "begin search", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                //String seachText = searchView.getQuery().toString();
                //intent.putExtra("search_text",seachText);
                //startActivity(intent);
                //searchText.setText(query);
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 0) {
                    //currentSearchTip = newText;
                    //showSearchTip(newText);
                    Toast.makeText(SearchResultActivity.this, newText, Toast.LENGTH_SHORT).show();
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
        diaryList = Diary.getAll(helper);
        if(diaryList == null){
            diaryList = new ArrayList<>();
        }
        Collections.reverse(diaryList);
    }
}



