package com.example.dell.diary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diarybook;
import com.example.dell.db.Sentencebook;

import java.util.ArrayList;
import java.util.List;

public class BottlesActivity extends AppCompatActivity {
    private DrawerLayout mDrawLayout;
    private CoordinatorLayout container;
    private DatabaseHelper databaseHelper = null;
   private final List<Sentencebook> sentencebookList =  new ArrayList<>();


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
                container=(CoordinatorLayout) findViewById(R.id.bottles_coordinatorLayout);
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


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_bottle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
       adapter = new BottleAdapter(sentencebookList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab =  findViewById(R.id.add_bottle_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

                final EditText inputBottleName = new EditText(BottlesActivity.this);
                inputBottleName.setId(R.id.edit_In_BottleName);
                inputBottleName.setHint("请输入瓶子的名字");
                inputBottleName.setFocusable(true);
                final EditText inputBottleDescribe = new EditText(BottlesActivity.this);
                inputBottleDescribe.setHint("请输入瓶子的描述");
                inputBottleDescribe.setFocusable(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(BottlesActivity.this);
                builder.setTitle("新建一个瓶子##")
                        .setView(inputBottleName)                //设置一个控件（其他控件同理）
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String inputName = inputBottleName.getText().toString();
                                String inputDescribe = inputBottleDescribe.getText().toString();
                                Sentencebook sentencebook = new Sentencebook(inputName);
                                adapter.addSentencebook(sentencebookList.size(),sentencebook);
                            }
                        });
                builder.show();
            }
        });


    }
    public void initSententcebookList() {
        sentencebookList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        sentencebookList = Sentencebook.getAll(helper);
    /*    Diarybook.class
    }*/
    }

}
