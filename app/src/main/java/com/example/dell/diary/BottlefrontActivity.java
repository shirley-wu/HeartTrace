package com.example.dell.diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Sentence;
import com.example.dell.db.Sentencebook;

import java.util.ArrayList;
import java.util.List;

public class BottlefrontActivity extends AppCompatActivity {
    private CoordinatorLayout container;
    private static String TAG = "123";
    public static final String BOTTLE_NAME = "bottle_name";
    public static final String BOTTLE_IMAGE_ID = "bottle_image_id";

    private DatabaseHelper databaseHelper = null;
    private List<Sentence> sentenceList = new ArrayList<>();
    private NoteAdapter adapter;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottletoolbar, menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottlefront);
        Intent intent = getIntent();
        String SentencebookName = intent.getStringExtra(BOTTLE_NAME);
       // int bottleImageId  = intent.getIntExtra(BOTTLE_IMAGE_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bottle_front_toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView bottleImageView = (ImageView) findViewById(R.id.bottle_image_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(SentencebookName);
      //  Glide.with(this).load(bottleImageId).into(bottleImageView);
       //悬浮按钮添加纸条
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.note_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(BottlefrontActivity.this, TicketEditActivity.class);
                startActivity(intent);*/
                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                Sentence sentence = new Sentence();
                sentence.setText("123");
                sentence.setDate();
                sentence.insert(helper);
                sentenceList = Sentence.getAll(helper, false);
               /* Log.d(TAG, "onClick: correct");*/
                adapter.update(sentenceList);
            }
        });

        initSententceList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_note_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(sentenceList);
        recyclerView.setAdapter(adapter);

    }

    //初始化sentenceList
    public void initSententceList(){
        sentenceList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        sentenceList= Sentence.getAll(helper,false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                container=(CoordinatorLayout) findViewById(R.id.bottlefront_coordinatorLayout);
                Snackbar.make(container, "确认删除？",Snackbar.LENGTH_SHORT).setAction("Undo",new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(BottlefrontActivity.this, "成功拯救一个瓶子",Toast.LENGTH_SHORT).show();
                    }
                }).show();

                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
